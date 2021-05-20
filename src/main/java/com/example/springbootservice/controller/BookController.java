package com.example.springbootservice.controller;

import com.example.springbootservice.jpa.Book;
import com.example.springbootservice.jpa.Library;
import com.example.springbootservice.service.Bookservice;
import com.example.springbootservice.service.LibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping(path = "/admin/book")
public class BookController {

    @Autowired
    private Bookservice bookService;

    @Autowired
    private LibraryService libraryService;

    @InitBinder
    public void InitBinder(WebDataBinder data)
    {
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
        data.registerCustomEditor(Date.class, new CustomDateEditor(s, true));
    }

    @RequestMapping("")
    public String getBooks(Model model)
    {
        return findPaginated(1,model);
    }

    @RequestMapping("show")
    public String getBookShow(Model model)
    {
        return findPagShow(1,model);
    }

    @RequestMapping("hidden")
    public String getBookHidden(Model model)
    {
        return findPagHidden(1,model);
    }

    @RequestMapping("/insertBook")
    public String insertBook(Model model)
    {
        Book book = new Book();
        List<Library> libraries = libraryService.listLibrariesByStatus(1);
        model.addAttribute("bookNew",book);
        model.addAttribute("libraries",libraries);
        return "admin/book/insertBook";
    }

    @RequestMapping(value = "/saveBook",method = RequestMethod.POST)
    public String savePro(@ModelAttribute("bookNew")@Valid Book book, BindingResult result,Model model)throws IOException
    {
        if (result.hasErrors())
        {
            List<Library> libraries = libraryService.listLibraries();
            model.addAttribute("listLibraries",libraries);
            model.addAttribute("bookNew",book);
            return "admin/book/insertBook";
        }
        boolean checkBookName = bookService.checkBookName(book.getName(),book.getLibrary().getId());
        if (checkBookName==false)
        {
            return "redirect:/admin/book/insertBook?errorbookname=Book Name is existed";
        }

        boolean bl = bookService.saveBook(book);
        if(bl)
        {
            return "redirect:/admin/book/";
        }
        return "redirect:/admin/book?error=Add New Book error";
    }

    @RequestMapping(value = "/editBook")
    public String editBook(@RequestParam("id")Integer id,Model model)
    {
        Book book = bookService.getBookById(id);
        List<Library> libraries = libraryService.listLibrariesByStatus(1);
        model.addAttribute("bookEdit",book);
        model.addAttribute("libraries",libraries);
        return "admin/book/editBook";
    }

    @RequestMapping(value = "/updateBook",method = RequestMethod.POST)
    public String updateBook(@ModelAttribute("bookEdit")Book book,Model model)throws IOException
    {
        boolean checkName = checkBookNameEdit(book.getName(),book.getId(),book.getLibrary().getId());
        if (checkName==false)
        {
            return "redirect:/admin/book/editBook?id="+book.getId()+"&&errorbookname=Book Name is existed";
        }
        boolean bl = bookService.updateBook(book);
        if (bl)
        {
            return "redirect:/admin/book/";
        }
        return "redirect:/admin/book?error=Add New Book error";
    }

    @RequestMapping(value = "/detailBook")
    public String detailBookById(@RequestParam("id")Integer id,Model model)
    {
        Book book = bookService.getBookById(id);
        model.addAttribute("bookDetail",book);
        return "admin/book/detailBook";
    }

    @RequestMapping(value = "/deleteBook")
    public String deleteProduct(@RequestParam("id")Integer id)
    {
        boolean bl = bookService.deleteBook(id);
        if (bl)
        {
            return "redirect:/admin/book/";
        }
        return "redirect:/admin/book?error=Delete Book error";
    }

    @RequestMapping("/page/{pageNo}")
    public String findPaginated(@PathVariable(value = "pageNo")int pageNo, Model model)
    {
        int pageSize = 10;
        List<Library> libraries = libraryService.listLibraries();
        Page<Book> page = bookService.findPaginated(pageNo,pageSize);
        List<Book> books = page.getContent();
        model.addAttribute("currentPage",pageNo);
        model.addAttribute("totalPages",page.getTotalPages());
        model.addAttribute("totalItems",page.getTotalElements());
        model.addAttribute("listBooks",books);
        model.addAttribute("libraries",libraries);
        return "admin/book/bookList";
    }
    @RequestMapping("/pageShow/{pageNo}")
    public String findPagShow(@PathVariable(value = "pageNo")int pageNo, Model model)
    {
        int pageSize = 10;
        List<Library> libraries = libraryService.listLibraries();
        Page<Book> page = bookService.findPaginatedShow(pageNo,pageSize);
        List<Book> books = page.getContent();
        model.addAttribute("currentPage",pageNo);
        model.addAttribute("totalPages",page.getTotalPages());
        model.addAttribute("totalItems",page.getTotalElements());
        model.addAttribute("listBooks",books);
        model.addAttribute("libraries",libraries);
        return "admin/book/bookList";
    }
    @RequestMapping("/pageHidden/{pageNo}")
    public String findPagHidden(@PathVariable(value = "pageNo")int pageNo, Model model)
    {
        int pageSize = 10;
        List<Library> libraries = libraryService.listLibraries();
        Page<Book> page = bookService.findPaginatedHidden(pageNo,pageSize);
        List<Book> books = page.getContent();
        model.addAttribute("currentPage",pageNo);
        model.addAttribute("totalPages",page.getTotalPages());
        model.addAttribute("totalItems",page.getTotalElements());
        model.addAttribute("listBooks",books);
        model.addAttribute("libraries",libraries);
        return "/admin/book/bookList";
    }


    public boolean checkBookNameEdit(String name,int id,int library_id)
    {
        Book book = bookService.getBookById(id);
        boolean checkBookName = bookService.checkBookName(name,library_id);
        if (checkBookName==false)
        {
            if (name.equals(book.getName()) && book.getLibrary().getId()==library_id)
            {
                return true;
            }
            else {
                return false;
            }
        }
        return true;
    }
}
