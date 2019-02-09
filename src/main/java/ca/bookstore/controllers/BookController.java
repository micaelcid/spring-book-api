package ca.bookstore.controllers;

import ca.bookstore.model.Book;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@RestController
public class BookController {
    private List<Book> books = new ArrayList<>();
    private AtomicLong nextId = new AtomicLong();
    @GetMapping("/")
    public String getHome(){
        return "Hello world";
    }

    @PostMapping("/book")
    @ResponseStatus(HttpStatus.CREATED)
    public Book create(@RequestBody Book book){
        book.setId(nextId.incrementAndGet());
        books.add(book);
        return book;
    }

    @GetMapping("/books")
    public List<Book> all(){
        return books;
    }

    @GetMapping("/books/{id}")
    public Book getById(@PathVariable("id") long bookId){
        for(Book book : books){
            if(book.getId() == bookId){
                return book;
            }
        }
        throw new IllegalArgumentException();
    }

    @PutMapping("/books/{id}")
    public Book update(
            @PathVariable("id") long bookId,
            @RequestBody Book newBook
    ){
        for(Book book : books){
            if(book.getId() == bookId){
                book.setTitle(newBook.getTitle());
                book.setDescription(newBook.getDescription());
                book.setPrice(newBook.getPrice());
                return book;
            }
        }
        throw new IllegalArgumentException();
    }

    @DeleteMapping("/books/{id}")
    public boolean delete(@PathVariable("id") long bookId)
    {
        books.removeIf(book -> book.getId() == bookId);
        return true;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(
            value = HttpStatus.BAD_REQUEST,
            reason = "Request ID not found."
    )
    public void badIdExceptionHandler(){}
}
