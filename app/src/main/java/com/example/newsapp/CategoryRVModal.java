package com.example.newsapp;

public class CategoryRVModal {
    private String category;
    private String categoryImgUrl;

    public CategoryRVModal(String category, String categoryImgUrl) {
        this.category = category;
        this.categoryImgUrl = categoryImgUrl;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory() {
        this.category = category;
    }

    public String getCategoryImgUrl() {
        return categoryImgUrl;
    }

    public void setCategoryImgUrl(String categoryImgUrl) {
        this.categoryImgUrl = categoryImgUrl;
    }
}
