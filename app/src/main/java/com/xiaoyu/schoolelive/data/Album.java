package com.xiaoyu.schoolelive.data;

import java.util.ArrayList;

/**
 * Created by lenovo on 2017/8/18.
 */

public class Album {

    public ArrayList<String> photos;
    public String photoDate;
    public String photoUri;
    public String photoBmp;
    public String photoPath;

    public Album(String date, ArrayList<String> photos) {
        this.photoDate = date;
        this.photos = photos;
    }

    public ArrayList<String> getPhotos() {
        return photos;
    }

    public void setPhotos(ArrayList<String> photos) {
        this.photos = photos;
    }

    public String getPhotoDate() {
        return photoDate;
    }

    public void setPhotoDate(String photoDate) {
        this.photoDate = photoDate;
    }

    public String getPhotoUri() {
        return photoUri;
    }

    public void setPhotoUri(String photoUri) {
        this.photoUri = photoUri;
    }

    public String getPhotoBmp() {
        return photoBmp;
    }

    public void setPhotoBmp(String photoBmp) {
        this.photoBmp = photoBmp;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

}
