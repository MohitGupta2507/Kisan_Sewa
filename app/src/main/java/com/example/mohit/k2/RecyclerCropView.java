package com.example.mohit.k2;

public class RecyclerCropView {

    private String Product,ProfileImage,Name,Product_Image,QuantityUnit,Quantity,MaximumPrice,State,City,Tehsil,Uid;

    public RecyclerCropView(String state, String city, String tehsil,String uid) {
        State = state;
        City = city;
        Tehsil = tehsil;
        Uid = uid;
    }

    public RecyclerCropView(String uid) {
        Uid = uid;
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getTehsil() {
        return Tehsil;
    }

    public void setTehsil(String tehsil) {
        Tehsil = tehsil;
    }

    public RecyclerCropView(String profileImage, String name, String product_Image, String quantityUnit, String quantity, String maximumPrice) {
        ProfileImage = profileImage;
        Name = name;
        Product_Image = product_Image;
        QuantityUnit = quantityUnit;
        Quantity = quantity;
        MaximumPrice = maximumPrice;
    }

    public RecyclerCropView() {
    }

    public String getProfileImage() {
        return ProfileImage;
    }

    public void setProfileImage(String profileImage) {
        ProfileImage = profileImage;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getProduct_Image() {
        return Product_Image;
    }

    public void setProduct_Image(String product_Image) {
        Product_Image = product_Image;
    }

    public String getQuantityUnit() {
        return QuantityUnit;
    }

    public void setQuantityUnit(String quantityUnit) {
        QuantityUnit = quantityUnit;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public String getMaximumPrice() {
        return MaximumPrice;
    }

    public String getProduct() {
        return Product;
    }

    public void setProduct(String product) {
        Product = product;
    }

/*    public void setMaximumPrice(String maximumPrice) {
        MaximumPrice = maximumPrice;
    }*/
}
