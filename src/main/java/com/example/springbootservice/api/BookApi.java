package com.example.springbootservice.api;
import com.example.springbootservice.jpa.Book;
import com.example.springbootservice.jpa.Library;
import com.example.springbootservice.repository.BookRepository;
import com.example.springbootservice.repository.LibraryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/books")
public class BookApi {
    private final LibraryRepository libraryRepository;
    private final BookRepository bookRepository;

    public BookApi(LibraryRepository libraryRepository, BookRepository bookRepository) {
        this.libraryRepository = libraryRepository;
        this.bookRepository = bookRepository;
    }

    @GetMapping //read data
    public ResponseEntity<Page<Book>> getAll(Pageable pageable)
    {
        return ResponseEntity.ok(bookRepository.findAll(pageable));
    }

    @GetMapping("{id}")
    public ResponseEntity<Book> getById(@PathVariable int id){

        Optional<Book> optionalBook= bookRepository.findById(id);

        if(!optionalBook.isPresent()){
            return ResponseEntity.unprocessableEntity().build();
        }
        return ResponseEntity.ok(optionalBook.get());
    }

    @PostMapping
    public ResponseEntity<Book> createBook(@Valid @RequestBody Book book){
        Optional<Library> optionalLibrary = libraryRepository.findById(book.getLibrary().getId());
        if(!optionalLibrary.isPresent()){
            return ResponseEntity.unprocessableEntity().build();
        }

        book.setLibrary(optionalLibrary.get());
        Book bookSaved = bookRepository.save(book);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(bookSaved.getId())
                .toUri();

        return ResponseEntity.created(location).body(bookSaved);

    }

    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable int id,
                                           @Valid @RequestBody Book book){
        Optional<Library> optionalLibrary = libraryRepository.findById(book.getLibrary().getId());
        if(!optionalLibrary.isPresent()){
            return ResponseEntity.unprocessableEntity().build();
        }
        Optional<Book> optionalBook= bookRepository.findById(id);

        if(!optionalBook.isPresent()){
            return ResponseEntity.unprocessableEntity().build();
        }
        book.setLibrary(optionalLibrary.get());
        book.setId(optionalBook.get().getId());
        bookRepository.save(book);
        return ResponseEntity.noContent().build();

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Book> deleteBook(@PathVariable int id,
                                           @Valid @RequestBody Book book){
        Optional<Book> optionalBook= bookRepository.findById(id);

        if(!optionalBook.isPresent()){
            return ResponseEntity.unprocessableEntity().build();
        }
        bookRepository.delete(optionalBook.get());
        return ResponseEntity.noContent().build();
    }

}
