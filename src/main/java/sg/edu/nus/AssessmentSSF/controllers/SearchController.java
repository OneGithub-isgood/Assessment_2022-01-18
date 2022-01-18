package sg.edu.nus.AssessmentSSF.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import sg.edu.nus.AssessmentSSF.AssessmentSsfApplication;
import sg.edu.nus.AssessmentSSF.models.Book;
import sg.edu.nus.AssessmentSSF.services.BookService;

@Controller
@RequestMapping(path="/search", produces=MediaType.TEXT_HTML_VALUE)
public class SearchController {

    private final Logger logger = Logger.getLogger(AssessmentSsfApplication.class.getName());
    
    @Autowired
    private BookService serviceLogic;

    @GetMapping
    public String searchResult(@RequestParam(required = true) String queryPattern, Model model) {
        //logger.log(Level.INFO, "User Search Term: %s".formatted(queryPattern));

        List<Book> searchResult = serviceLogic.search(queryPattern);
        List<String> bookList = new ArrayList<>();
        for (int i=0; i < searchResult.size(); i++) {
            bookList.add((searchResult.get(i).getWorks_ID().replace("/works/", "")).toString() + "|" + searchResult.get(i).getBookTitle());
        }
        model.addAttribute("queryPattern", queryPattern);
        model.addAttribute("bookList", bookList);

        return "search";
    }
}
