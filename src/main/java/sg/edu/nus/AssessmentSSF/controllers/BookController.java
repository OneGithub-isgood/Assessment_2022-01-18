package sg.edu.nus.AssessmentSSF.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import sg.edu.nus.AssessmentSSF.AssessmentSsfApplication;
import sg.edu.nus.AssessmentSSF.models.Book;
import sg.edu.nus.AssessmentSSF.repositories.BookRepository;
import sg.edu.nus.AssessmentSSF.services.BookService;

@Controller
public class BookController {

    private final Logger logger = Logger.getLogger(AssessmentSsfApplication.class.getName());

    @Autowired
    private BookService serviceLogic;

    @Autowired
    BookRepository bookCache;
    

    @GetMapping("/book/{worksid}")
    public String showBook(Model model, @PathVariable(value="worksid") String worksid) {
        List<String> bookQuery = serviceLogic.retrieve(worksid);
        Map<String, String> bookMap = new HashMap<>();
        bookMap.put("Book Title", (bookQuery.get(0)).toString());
        bookMap.put("Book Description", (bookQuery.get(1)).toString());
        logger.log(Level.INFO, "Book Work URL: %s".formatted(bookQuery.get(1))+".");
        bookMap.put("Book Excerpt", (bookQuery.get(2)).toString());
        bookCache.store(worksid, bookMap);
        model.addAttribute("bookInfo", bookQuery);
        return "book";
    }
}
