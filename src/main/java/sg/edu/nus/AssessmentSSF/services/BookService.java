package sg.edu.nus.AssessmentSSF.services;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import sg.edu.nus.AssessmentSSF.AssessmentSsfApplication;
import sg.edu.nus.AssessmentSSF.models.Book;
import sg.edu.nus.AssessmentSSF.repositories.BookRepository;

@Service
public class BookService {

    private final Logger logger = Logger.getLogger(AssessmentSsfApplication.class.getName());

    public List<Book> search(String queryPattern) {
        final String urlAddQuery = UriComponentsBuilder
            .fromUriString("http://openlibrary.org//search.json")
            .queryParam("q", queryPattern.trim().replace(" ", "+"))
            .queryParam("fields", "key,title") //Returns only 'key' and 'title' field
            .queryParam("key", "works/OL27448W")
            .queryParam("limit", "20") //Returns only Maximum 20 books
            .toUriString();
        logger.log(Level.INFO, "Search Book URL: %s".formatted(urlAddQuery));

        final RequestEntity<Void> req = RequestEntity
            .get(urlAddQuery)
            .accept(MediaType.APPLICATION_JSON)
            .build();
        final RestTemplate temp = new RestTemplate();
        final ResponseEntity<String> resp = temp.exchange(req, String.class);

        if (resp.getStatusCode() != HttpStatus.OK) {
            throw new IllegalArgumentException(
                "Error Code %s".formatted(resp.getStatusCode().toString()));
        }
        final String respbody = resp.getBody();

        try (InputStream iS = new ByteArrayInputStream(respbody.getBytes())) {
            final JsonReader reader = Json.createReader(iS);
            final JsonObject data = reader.readObject();
            final JsonArray reading = data.getJsonArray("docs");
            return reading.stream()
                .map(var -> (JsonObject)var)
                .map(Book::create)
                .map(eachBookInfo -> {
                    return eachBookInfo;
                }).collect(Collectors.toList());
        } catch (Exception ex) { }
        logger.log(Level.INFO, "Search Collection: %s".formatted(Collections.EMPTY_LIST));
        return Collections.EMPTY_LIST;
    }

    public List<String> retrieve(String worksID) {
        final String urlAddQuery = UriComponentsBuilder
            .fromUriString("http://openlibrary.org//works/" + worksID + ".json")
            .toUriString();
        logger.log(Level.INFO, "Book Work URL: %s".formatted(urlAddQuery)+".");

        final RequestEntity<Void> req = RequestEntity
            .get(urlAddQuery)
            .accept(MediaType.APPLICATION_JSON)
            .build();
        final RestTemplate temp = new RestTemplate();
        final ResponseEntity<String> resp = temp.exchange(req, String.class);

        if (resp.getStatusCode() != HttpStatus.OK) {
            throw new IllegalArgumentException(
                "Error Code %s".formatted(resp.getStatusCode().toString()));
        }
        final String respbody = resp.getBody();

        List<String> bookInfo = new ArrayList<String>();
        
        try (InputStream iS = new ByteArrayInputStream(respbody.getBytes())) {
            final JsonReader reader = Json.createReader(iS);
            final JsonObject data = reader.readObject();
            final JsonArray reading = data.getJsonArray("excerpts");
            bookInfo.add(data.getString("title").toString());
            bookInfo.add(data.getJsonObject("description").getString("value").toString());
            if (null != reading) { //Note: Can't retrieve excerpts...
                bookInfo.add(reading.stream().findFirst().toString());
            } else {
                bookInfo.add("Excerpts not found");
            }
            return bookInfo;
        } catch (Exception ex) { }
        return bookInfo;
    }

}
