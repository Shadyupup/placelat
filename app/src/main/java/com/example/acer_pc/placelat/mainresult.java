package com.example.acer_pc.placelat;

public class mainresult {
    private String placeId;
    private String name;
    private String address;
    private String imageUrl;

    public mainresult(String placeId,String name,String address,String imageUrl) {
        this.imageUrl = imageUrl;
        this.name = name;
        this.address = address;
        this.placeId = placeId;

    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getName() {
        return name;
    }

    public String getPlaceId() {
        return placeId;
    }

    public String getAddress() {
        return address;
    }
}
