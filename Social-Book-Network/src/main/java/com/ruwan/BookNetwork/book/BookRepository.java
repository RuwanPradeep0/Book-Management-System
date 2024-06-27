package com.ruwan.BookNetwork.book;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book , Integer> {

}
