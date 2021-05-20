package com.example.springbootservice.repository;

import com.example.springbootservice.jpa.Library;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
public interface LibraryRepository extends JpaRepository<Library,Integer> {

    List<Library> findAllByStatus(int status);

    @Query("SELECT c FROM Library c WHERE  id = ?1")
    List<Library> getByLibraryId(int id);

    @Query("SELECT c FROM Library c WHERE status = 1 OR status = 2")
    List<Library> getAllLibraryStatus();

    @Query("SELECT c FROM Library c WHERE status = 1 OR status = 2")
    Page<Library> findPaginateLibraryStatus(Pageable pageable);

    @Query("SELECT p FROM Library p WHERE status = 1 ")
    Page<Library> findPaginateLibraryStatusShow(Pageable pageable);

    @Query("SELECT p FROM Library p WHERE status = 2 ")
    Page<Library> findPaginateLibraryStatusHidden(Pageable pageable);


    @Query("SELECT c FROM Library c WHERE name = ?1")
    Library findByLibraryName(String name);

}
