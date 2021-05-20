package com.example.springbootservice.controller;

import com.example.springbootservice.jpa.Library;
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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping(path = "/admin/library")
public class LibraryController {
    @Autowired
    private LibraryService libraryService;

    @InitBinder
    public void InitBinder(WebDataBinder data)
    {
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
        data.registerCustomEditor(Date.class, new CustomDateEditor(s, true));
    }

    @RequestMapping("")
    public String getLibraries(Model model)
    {
        Library library = new Library();

        return findPaginatedLib(1,model,library);
    }


    @RequestMapping("show")
    public String getLibrariesShow(Model model)
    {
        Library library = new Library();
        return findPagLibShow(1,model, library);
    }

    @RequestMapping("hidden")
    public String getLibrariesHidden(Model model)
    {
        Library library = new Library();
        return findPagLibHidden(1,model, library);
    }

    @RequestMapping(path = "/saveLibrary",method = RequestMethod.POST)
    public String saveLibrary(@ModelAttribute("libraryNew")@Valid Library library, BindingResult result, Model model)
    {
        if (result.hasErrors())
        {
            return findPaginatedLib(1,model,library);
        }
        boolean checkLibraryName = libraryService.checkLibraryName(library.getName());
        if (checkLibraryName==false)
        {
            return "redirect:/admin/library?errorlibraryname=LibraryName is existed";
        }

        boolean bl = libraryService.saveLibrary(library);
        if (bl)
        {
            return "redirect:/admin/library/";
        }
        return "redirect:/admin/library?error=Add New Library error";

    }

    @RequestMapping(path = "/editLibrary")
    public String editLibrary(@RequestParam("id")Integer id, Model model)
    {
        Library library = libraryService.getLibraryById(id);
        model.addAttribute("libraryEdit",library);
        return "admin/library/editLibrary";
    }

    @RequestMapping(path = "/updateLibrary",method = RequestMethod.POST)
    public String updateLibrary(@ModelAttribute("libraryEdit")Library library)
    {
        boolean checkLibraryName = checkLibraryNameEdit(library.getName(),library.getId());
        if (checkLibraryName==false)
        {
            return "redirect:/admin/library/editLibrary?id="+library.getId()+"&&errorlibraryname=Library Name is existed";
        }
        boolean bl = libraryService.updateLibrary(library);
        if (bl)
        {
            return "redirect:/admin/library/";
        }
        return "redirect:/admin/library?error=Update Library error";

    }

    @RequestMapping(path = "/deleteLibrary")
    public String deleteLibrary(@RequestParam("id")Integer id)
    {
        boolean bl = libraryService.deleteLibrary(id);
        if (bl)
        {
            return "redirect:/admin/library/";
        }
        return "redirect:admin/library?error=Delete Library error";
    }

    @RequestMapping("/page/{pageNo}")
    public String findPaginatedLib(@PathVariable(value = "pageNo")int pageNo, Model model, Library library)
    {
        int pageSize = 10;
        Page<Library> page = libraryService.findPaginatedLibraries(pageNo,pageSize);
        List<Library> libraries = page.getContent();
        model.addAttribute("currentPage",pageNo);
        model.addAttribute("totalPages",page.getTotalPages());
        model.addAttribute("totalItems",page.getTotalElements());
        model.addAttribute("libraryNew", library);
        model.addAttribute("listLibraries",libraries);
        return "admin/library/libraryList";
    }

    @RequestMapping("/pageShow/{pageNo}")
    public String findPagLibShow(@PathVariable(value = "pageNo")int pageNo, Model model,  Library library)
    {
        int pageSize = 10;
        Page<Library> page = libraryService.findPaginatedLibrariesShow(pageNo,pageSize);
        List<Library> libraries = page.getContent();
        model.addAttribute("currentPage",pageNo);
        model.addAttribute("totalPages",page.getTotalPages());
        model.addAttribute("totalItems",page.getTotalElements());
        model.addAttribute("libraryNew", library);
        model.addAttribute("listLibraries",libraries);
        return "admin/library/libraryList";
    }
    @RequestMapping("/pageHidden/{pageNo}")
    public String findPagLibHidden(@PathVariable(value = "pageNo")int pageNo, Model model,Library library)
    {
        int pageSize = 10;
        Page<Library> page = libraryService.findPaginatedLibrariesHidden(pageNo,pageSize);
        List<Library> libraries = page.getContent();
        model.addAttribute("currentPage",pageNo);
        model.addAttribute("totalPages",page.getTotalPages());
        model.addAttribute("totalItems",page.getTotalElements());
        model.addAttribute("libraryNew", library);
        model.addAttribute("listLibraries",libraries);
        return "admin/library/libraryList";
    }

    public boolean checkLibraryNameEdit(String name, int id)
    {
        Library library = libraryService.getLibraryById(id);
        boolean checkLibraryName = libraryService.checkLibraryName(name);
        if (checkLibraryName==false)
        {
            if (name.equals(library.getName()))
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
