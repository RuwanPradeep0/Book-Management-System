package com.ruwan.BookNetwork.book;

import org.springframework.stereotype.Service;

@Service
public class BookMapper {
    public Book toBook(BookRequest request) {
        return Book.builder()
                .id(request.id())
                .title(request.title())
                .authorName(request.authorName())
                .synopsis(request.synopsis())
                .archived(false)
                .shareable(request.shareable())
                .build();

    }

    public BookResponse toBookResponse(Book book) {
        return BookResponse.builder().build();
    }
}
