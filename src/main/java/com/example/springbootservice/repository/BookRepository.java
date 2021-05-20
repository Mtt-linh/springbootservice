package com.example.springbootservice.repository;

import com.example.springbootservice.jpa.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
public interface BookRepository extends JpaRepository<Book,Integer> {
    List<Book> findAllByStatus(int status);

    @Query("SELECT p FROM Book p WHERE library_id = ?1 AND status=1")
    List<Book> findAllByLibrary(int library_id);

    @Query("SELECT p FROM Book p WHERE status = 1 OR status = 2")
    List<Book> getAllBookStatus();

    @Query("SELECT p FROM Book p WHERE status = 1 OR status = 2")
    Page<Book> findPaginateBookStatus(Pageable pageable);

    @Query("SELECT p FROM Book p WHERE status = 1 ")
    Page<Book> findPaginateBookStatusShow(Pageable pageable);

    @Query("SELECT p FROM Book p WHERE status = 2 ")
    Page<Book> findPaginateBookStatusHidden(Pageable pageable);

    @Query("SELECT p FROM Book p WHERE name = ?1 AND library_id = ?2")
    Book findByBookName(String name,int library_id);
}
