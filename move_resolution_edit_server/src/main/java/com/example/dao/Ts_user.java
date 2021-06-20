package com.example.dao;

public class Ts_user {
    public int id;
    public String keywords;
    public String description;
    public String str_content;
    public String entity_content;

    public Ts_user(int id, String keywords, String description, String str_content, String entity_content) {
        this.id = id;
        this.keywords = keywords;
        this.description = description;
        this.str_content = str_content;
        this.entity_content = entity_content;
    }

    public int getId() {
        return id;
    }

    public String getKeywords() {
        return keywords;
    }

    public String getDescription() {
        return description;
    }

    public String getStr_content() {
        return str_content;
    }

    public String getEntity_content() {
        return entity_content;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStr_content(String str_content) {
        this.str_content = str_content;
    }

    public void setEntity_content(String entity_content) {
        this.entity_content = entity_content;
    }
}
