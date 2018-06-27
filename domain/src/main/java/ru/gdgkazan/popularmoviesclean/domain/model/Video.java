package ru.gdgkazan.popularmoviesclean.domain.model;

import java.io.Serializable;

/**
 * @author Artur Vasilov
 */
public class Video implements Serializable {

    private String mKey;
    private String mName;

    public Video(String mKey, String mName){
        this.mKey = mKey;
        this.mName = mName;
    }

    public Video(){}

    public String getKey() {
        return mKey;
    }

    public void setKey(String key) {
        mKey = key;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }
}
