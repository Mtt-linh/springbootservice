package com.example.springbootservice.service;

import com.example.springbootservice.jpa.Library;
import com.example.springbootservice.repository.LibraryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LibraryServiceImpl implements LibraryService {
    @Autowired
    private LibraryRepository libraryRepository;

    @Override
    public List<Library> listLibraries() {
        try{
            List<Library> libraries = libraryRepository.getAllLibraryStatus();
            return libraries;
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Library> listLibrariesByStatus(int status) {
        try{
            List<Library> libraries = libraryRepository.findAllByStatus(status);
            return libraries;
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Library getLibraryById(int id) {
        try{
            Library library = libraryRepository.findById(id).get();
            return library;
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean saveLibrary(Library library) {
        try{
            libraryRepository.save(library);
            return true;
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean deleteLibrary(int id) {
        try{
            Library library = libraryRepository.findById(id).get();
            library.setStatus(3);
            libraryRepository.save(library);
            return true;
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean updateLibrary(Library library) {
        try{
            libraryRepository.save(library);
            return true;
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return false;
    }


    @Override
    public boolean checkLibraryName(String name) {
        Library library = libraryRepository.findByLibraryName(name);
        if (library==null)
        {
            return true;
        }else{
            return false;
        }
    }

    @Override
    public Page<Library> findPaginatedLibraries(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo-1,pageSize);
        return this.libraryRepository.findPaginateLibraryStatus(pageable);
    }

    @Override
    public Page<Library> findPaginatedLibrariesShow(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo-1,pageSize);
        return this.libraryRepository.findPaginateLibraryStatusShow(pageable);
    }

    @Override
    public Page<Library> findPaginatedLibrariesHidden(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        return this.libraryRepository.findPaginateLibraryStatusHidden(pageable);
    }
}
