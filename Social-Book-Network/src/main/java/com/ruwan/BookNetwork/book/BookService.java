package com.ruwan.BookNetwork.book;

import com.ruwan.BookNetwork.common.PageResponse;
import com.ruwan.BookNetwork.exception.OperationNotPermittedException;
import com.ruwan.BookNetwork.file.FileStorageService;
import com.ruwan.BookNetwork.history.BookTransactionHistory;
import com.ruwan.BookNetwork.history.BookTransactionHistoryRepository;
import com.ruwan.BookNetwork.user.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final BookTransactionHistoryRepository bookTransactionHistoryRepository;
    private final BookMapper bookMapper;
    private final FileStorageService fileStorageService;

    public Integer save(BookRequest request, Authentication connectedUser) {

        User user = ((User) connectedUser.getPrincipal());
        Book book = bookMapper.toBook(request);
        book.setOwner(user);

        return bookRepository.save(book).getId();
    }

    public BookResponse findById(Integer bookId) {

        return bookRepository.findById(bookId)
                .map(bookMapper::toBookResponse)
                .orElseThrow(() -> new EntityNotFoundException("No Book find with the id ::" + bookId));
    }


    public PageResponse<BookResponse> findAllBooks(int page, int size, Authentication connectedUser) {

        User user = ((User) connectedUser.getPrincipal());

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<Book> books = bookRepository.findAllDisplayableBooks(pageable, user.getId());
        List<BookResponse> bookResponse = books.stream()
                .map(bookMapper::toBookResponse)
                .toList();

        return new PageResponse<>(
                bookResponse,
                books.getNumber(),
                books.getSize(),
                books.getTotalElements(),
                books.getTotalPages(),
                books.isFirst(),
                books.isLast()
        );
    }

    public PageResponse<BookResponse> findAllBooksByOwner(int page, int size, Authentication connectedUser) {

        User user = ((User) connectedUser.getPrincipal());
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<Book> books = bookRepository.findAll(BookSpecification.withOwnerId(user.getId()), pageable);

        List<BookResponse> bookResponse = books.stream()
                .map(bookMapper::toBookResponse)
                .toList();

        return new PageResponse<>(
                bookResponse,
                books.getNumber(),
                books.getSize(),
                books.getTotalElements(),
                books.getTotalPages(),
                books.isFirst(),
                books.isLast()
        );
    }

    public PageResponse<BorrowedBookResponse> findAllBorrowedBooks(int page, int size, Authentication connectedUser) {
        User user = ((User) connectedUser.getPrincipal());
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());

        Page<BookTransactionHistory> allBorrowedBooks = bookTransactionHistoryRepository.findAllBorrowedBooks(pageable , user.getId());

        List<BorrowedBookResponse> bookResponse = allBorrowedBooks.stream()
                .map(bookMapper :: toBorrowedBookResponse)
                .toList();

        return new PageResponse<>(
                bookResponse,
                allBorrowedBooks.getNumber(),
                allBorrowedBooks.getSize(),
                allBorrowedBooks.getTotalElements(),
                allBorrowedBooks.getTotalPages(),
                allBorrowedBooks.isFirst(),
                allBorrowedBooks.isLast()
        );
    }

    public PageResponse<BorrowedBookResponse> findAllReturnedBooks(int page, int size, Authentication connectedUser) {
        User user = ((User) connectedUser.getPrincipal());
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());

        Page<BookTransactionHistory> allBorrowedBooks = bookTransactionHistoryRepository.findAllReturnedBooks(pageable , user.getId());

        List<BorrowedBookResponse> bookResponse = allBorrowedBooks.stream()
                .map(bookMapper :: toBorrowedBookResponse)
                .toList();

        return new PageResponse<>(
                bookResponse,
                allBorrowedBooks.getNumber(),
                allBorrowedBooks.getSize(),
                allBorrowedBooks.getTotalElements(),
                allBorrowedBooks.getTotalPages(),
                allBorrowedBooks.isFirst(),
                allBorrowedBooks.isLast()
        );
    }

    public Integer updateShareableStatus(Integer bookId, Authentication connectedUser) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("No book found"));

        User user = ((User) connectedUser.getPrincipal());
        if(!Objects.equals(book.getOwner().getId() ,  user.getId())){
            throw new OperationNotPermittedException("You can not update books shareable status");
        }
        book.setShareable(!book.isShareable());
        bookRepository.save(book);
        return bookId;
    }

    public Integer updateArchivedStatus(Integer bookId, Authentication connectedUser) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("No book found"));

        User user = ((User) connectedUser.getPrincipal());
        if(!Objects.equals(book.getOwner().getId() ,  user.getId())){
            throw new OperationNotPermittedException("You can not update books archived status");
        }
        book.setArchived(!book.isArchived());
        bookRepository.save(book);
        return bookId;
    }

    public Integer borrowBook(Integer bookId, Authentication connectedUser) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("No book found with the provided ID :: " + bookId));
        if(book.isArchived() || !book.isShareable()){
            throw new OperationNotPermittedException("The requested book can noy be borrowed since it is archived or not shareable");
        }

        User user = ((User) connectedUser.getPrincipal());
        if(Objects.equals(book.getOwner().getId() , user.getId())){
            throw new OperationNotPermittedException("You cannot borrow your own book");
        }
       final boolean isAlreadyBorrowed = bookTransactionHistoryRepository.isAlreadyBorrowedByUser(bookId , user.getId());
        if(isAlreadyBorrowed){
            throw new OperationNotPermittedException("The requested book is already borrowed");
        }

        BookTransactionHistory bookTransactionHistory = BookTransactionHistory.builder()
                .user(user)
                .book(book)
                .returned(false)
                .returnApproved(false)
                .build();

        return bookTransactionHistoryRepository.save(bookTransactionHistory).getId();
    }

    public Integer returnBorrowedBook(Integer bookId, Authentication connectedUser) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("No book found with the provided ID :: " + bookId));

        if(book.isArchived() || !book.isShareable()){
            throw new OperationNotPermittedException("The requested book can noy be borrowed since it is archived or not shareable");
        }

        User user = ((User) connectedUser.getPrincipal());
        if(Objects.equals(book.getOwner().getId() , user.getId())){
            throw new OperationNotPermittedException("You cannot borrow or return your own book");
        }

        BookTransactionHistory bookTransactionHistory = bookTransactionHistoryRepository.findByBookIdAndUserId(book , user.getId())
                .orElseThrow(() -> new OperationNotPermittedException("You did not borrow this book"));

        bookTransactionHistory.setReturned(true);

        return bookTransactionHistoryRepository.save(bookTransactionHistory).getId();
    }

    public Integer approveReturnBorrowedBook(Integer bookId, Authentication connectedUser) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("No book found with the provided ID :: " + bookId));

        if(book.isArchived() || !book.isShareable()){
            throw new OperationNotPermittedException("The requested book can noy be borrowed since it is archived or not shareable");
        }

        User user = ((User) connectedUser.getPrincipal());
        if(Objects.equals(book.getOwner().getId() , user.getId())){
            throw new OperationNotPermittedException("You cannot borrow or return your own book");
        }

        BookTransactionHistory bookTransactionHistory = bookTransactionHistoryRepository.findByBookIdAndOwnerId(book , user.getId())
                .orElseThrow(() -> new OperationNotPermittedException("The Book is not returned yet. You cannon approve its return"));

        return bookTransactionHistoryRepository.save(bookTransactionHistory).getId();
    }

    public void uploadBookCoverPicture(MultipartFile file, Authentication connectedUser, Integer bookId) {

         Book book = bookRepository.findById(bookId)
                 .orElseThrow(() -> new EntityNotFoundException("No Book found with The ID :: " + bookId));

         User user = ((User) connectedUser.getPrincipal());
         var bookCover = fileStorageService.saveFile(file,user.getId());
         bookRepository.save(book);
        return;
    }
}
