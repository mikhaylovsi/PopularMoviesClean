package ru.gdgkazan.popularmoviesclean.domain.model;

import java.io.Serializable;

/**
 * @author Artur Vasilov
 */
public class Review implements Serializable {

    private String mAuthor;
    private String mContent;

    public Review(String mAuthor, String mContent){

        this.mAuthor = mAuthor;
        this.mContent = mContent;

    }

    public Review(){

    };


    public String getAuthor() {
        return mAuthor;
    }

    public void setAuthor(String author) {
        mAuthor = author;
    }

    public String getContent() {
        return mContent;
    }

    public void setContent(String content) {
        mContent = content;
    }
}
