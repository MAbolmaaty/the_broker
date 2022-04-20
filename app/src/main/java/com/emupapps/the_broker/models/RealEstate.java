package com.emupapps.the_broker.models;

import java.util.List;

public class RealEstate {
    private List<RealEstateImage> images;
    private String _id;
    private String title;
    private String description;
    private String latitude;
    private String longitude;
    private String published_at;
    private String createdAt;
    private String updatedAt;
    private int status;
    private String address;
    private String price;
    private String insuranceAmount;
    private String ownerId;
    private float realEstateStatus;
    private String id;

    public RealEstate(List<RealEstateImage> images, String _id, String title, String description,
                      String latitude, String longitude, String published_at, String createdAt,
                      String updatedAt, int status, String address, String price,
                      String insuranceAmount, String ownerId, float realEstateStatus, String id) {
        this.images = images;
        this._id = _id;
        this.title = title;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
        this.published_at = published_at;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.status = status;
        this.address = address;
        this.price = price;
        this.insuranceAmount = insuranceAmount;
        this.ownerId = ownerId;
        this.realEstateStatus = realEstateStatus;
        this.id = id;
    }

    public List<RealEstateImage> getImages() {
        return images;
    }

    public String get_id() {
        return _id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getPublished_at() {
        return published_at;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public int getStatus() {
        return status;
    }

    public String getAddress() {
        return address;
    }

    public String getPrice() {
        return price;
    }

    public String getInsuranceAmount() {
        return insuranceAmount;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public float getRealEstateStatus() {
        return realEstateStatus;
    }

    public String getId() {
        return id;
    }
}
