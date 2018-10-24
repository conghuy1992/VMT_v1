package com.monamedia.vmt.model;

public class ProductModel {
    public int qtyBuy;
    public String name;
    public String image;
    public String properties;
    public boolean isFullProperties;
    public String price;
    public String currency;

    public ProductModel() {
        qtyBuy = 1;
        isFullProperties=true;
        properties="";
    }
}
