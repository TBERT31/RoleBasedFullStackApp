package com.example.librarymanagementsystem;

import static org.junit.jupiter.api.Assertions.*;
import com.example.librarymanagementsystem.entity.Book;
import com.example.librarymanagementsystem.service.BookService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;


@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
public class BookRepositoryTest {

    @Autowired
    private BookService bookService;

    @Test
    public void GivenCreateBookThenExceptSameBook(){
        Book book = Book.builder()
                .name("Book 1")
                .description("This is book 1")
                .price(12.99).build();
        Book actual = bookService.createBook(book);

        assertEquals(book.getName(), actual.getName());
        assertEquals(book.getDescription(), actual.getDescription());
        assertEquals(book.getPrice(), actual.getPrice());

    }
}
