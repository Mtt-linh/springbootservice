package com.example.springbootservice.api;

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
@RequestMapping("api/v1/libraries")
public class LibraryApi {
    private final LibraryRepository libraryRepository;
    private final BookRepository bookRepository;

    public LibraryApi(LibraryRepository libraryRepository, BookRepository bookRepository) {
        this.libraryRepository = libraryRepository;
        this.bookRepository = bookRepository;
    }

    @GetMapping //read data
    public ResponseEntity<Page<Library>> getAll(Pageable pageable)
    {
        return ResponseEntity.ok(libraryRepository.findAll(pageable));
    }

    @GetMapping("{id}")
    public ResponseEntity<Library> getById(@PathVariable int id){
        Optional<Library> optionalLibrary= libraryRepository.findById(id);

        if(!optionalLibrary.isPresent()){
            return ResponseEntity.unprocessableEntity().build();
        }
        return ResponseEntity.ok(optionalLibrary.get());
    }

    @PostMapping
    public ResponseEntity<Library> createLibrary(@Valid @RequestBody Library library){
        Library librarySaved = libraryRepository.save(library);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(librarySaved.getId())
                .toUri();

        return ResponseEntity.created(location).body(librarySaved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Library> updateLibrary(@PathVariable int id,
                                                 @Valid @RequestBody Library library){
        Optional<Library> optionalLibrary= libraryRepository.findById(id);

        if(!optionalLibrary.isPresent()){
            return ResponseEntity.unprocessableEntity().build();
        }
        library.setId(optionalLibrary.get().getId());
        libraryRepository.save(library);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Library> deleteLibrary(@PathVariable int id,
                                                 @Valid @RequestBody Library library){
        Optional<Library> optionalLibrary= libraryRepository.findById(id);

        if(!optionalLibrary.isPresent()){
            return ResponseEntity.unprocessableEntity().build();
        }
        libraryRepository.delete(optionalLibrary.get());
        return ResponseEntity.noContent().build();
    }
}
