package com.monamedia.vmt.model;

public class CategoryModel {
    public int id;
    public String name;

    public CategoryModel(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
