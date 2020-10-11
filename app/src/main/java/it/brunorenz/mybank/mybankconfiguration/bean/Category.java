package it.brunorenz.mybank.mybankconfiguration.bean;

import java.io.Serializable;

public class Category implements Serializable {
    private String category;
    private String description;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
