package sg.edu.nus.AssessmentSSF.models;

import java.io.Serializable;

import jakarta.json.JsonObject;

public class Book implements Serializable {
    private String works_id;
    private String bookTitle;
    private String bookDescription;
    private String bookCoverURL;
    private String bookExcerpt;

    public Book() {
        
    }

    public static Book create(JsonObject jO) {
        final Book eachBook = new Book();
        eachBook.setWorks_ID(jO.getString("key"));
        eachBook.setBookTitle(jO.getString("title"));
        return eachBook;
    }

    public static Book cast(JsonObject jO) {
        final Book eachBook = new Book();
        eachBook.setBookExcerpt(jO.getString("excerpt"));
        return eachBook;
    }

    public String getWorks_ID() {
        return this.works_id;
    }

    public String getBookTitle() {
        return this.bookTitle;
    }

    public String getBookDescription() {
        return this.bookDescription;
    }

    public String getBookCoverURL() {
        return this.bookCoverURL;
    }

    public String getBookExcerpt() {
        return this.bookExcerpt;
    }

    public void setWorks_ID(String works_id) {
        this.works_id = works_id;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public void setBookDescription(String bookDescription) {
        this.bookDescription = bookDescription;
    }

    public void setBookCoverURL(String bookCoverURL) {
        this.bookCoverURL = bookCoverURL;
    }

    public void setBookExcerpt(String bookExcerpt) {
        this.bookExcerpt = bookExcerpt;
    }
}
