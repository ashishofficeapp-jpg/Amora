package com.amora.app.response;

import android.os.Parcel;
import android.os.Parcelable;

public class BannerImageModel implements Parcelable {

    private int id;
    private String image;
    private String status;
    private int images;

    public BannerImageModel(Parcel in) {
        id = in.readInt();
        image = in.readString();
        status = in.readString();
    }

    public static final Creator<BannerImageModel> CREATOR = new Creator<BannerImageModel>() {
        @Override
        public BannerImageModel createFromParcel(Parcel in) {
            return new BannerImageModel(in);
        }

        @Override
        public BannerImageModel[] newArray(int size) {
            return new BannerImageModel[size];
        }
    };

    public BannerImageModel(int images) {
        this.images = images;
    }

    public int getImages() {
        return images;
    }

    public void setImages(int images) {
        this.images = images;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(image);
        dest.writeString(status);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
