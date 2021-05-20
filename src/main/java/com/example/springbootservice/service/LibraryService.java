package com.example.springbootservice.service;

import com.example.springbootservice.jpa.Library;
import org.springframework.data.domain.Page;

import java.util.List;
public interface LibraryService {
    List<Library> listLibraries();
    List<Library> listLibrariesByStatus(int status);
    Library getLibraryById(int id);
    boolean saveLibrary(Library library);
    boolean deleteLibrary(int id);
    boolean updateLibrary(Library library);
    boolean checkLibraryName(String name);
    Page<Library> findPaginatedLibraries(int pageNo, int pageSize);
    Page<Library> findPaginatedLibrariesShow(int pageNo,int pageSize);
    Page<Library> findPaginatedLibrariesHidden(int pageNo,int pageSize);
}
