package com.ruwan.BookNetwork.file;

import com.ruwan.BookNetwork.book.Book;
import org.springframework.lang.NonNull;
import org.springframework.web.multipart.MultipartFile;

public class FileStorageService {
    public String saveFile(@NonNull MultipartFile file,
                           @NonNull Book book,
                           @NonNull Integer id) {


        return null;
    }
}
