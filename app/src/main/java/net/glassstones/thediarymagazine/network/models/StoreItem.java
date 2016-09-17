package net.glassstones.thediarymagazine.network.models;

/**
 * Created by Thompson on 16/09/2016.
 * For The Diary Magazine
 */
public class StoreItem {
    private String objectId, title, description, imageUri, extUrl;
    private double price;

    public String getObjectId () {
        return objectId;
    }

    public void setObjectId (String objectId) {
        this.objectId = objectId;
    }

    public String getTitle () {
        return title;
    }

    public void setTitle (String title) {
        this.title = title;
    }

    public String getDescription () {
        return description;
    }

    public void setDescription (String description) {
        this.description = description;
    }

    public String getImageUri () {
        return imageUri;
    }

    public void setImageUri (String imageUri) {
        this.imageUri = imageUri;
    }

    public String getExtUrl () {
        return extUrl;
    }

    public void setExtUrl (String extUrl) {
        this.extUrl = extUrl;
    }

    public double getPrice () {
        return price;
    }

    public void setPrice (double price) {
        this.price = price;
    }

    public StoreItem objectId (String objectId) {
        this.objectId = objectId;
        return this;
    }

    public StoreItem title (String title) {
        this.title = title;
        return this;
    }

    public StoreItem description (String description) {
        this.description = description;
        return this;
    }

    public StoreItem imageUri (String imageUri) {
        this.imageUri = imageUri;
        return this;
    }

    public StoreItem extUrl (String extUrl) {
        this.extUrl = extUrl;
        return this;
    }

    public StoreItem price (double price) {
        this.price = price;
        return this;
    }
}
