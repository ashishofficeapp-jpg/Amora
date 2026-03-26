package com.amora.app.response;

import android.os.Parcel;
import android.os.Parcelable;

public class GiftImageModel implements Parcelable {

    private int id;
    private int image;

    public GiftImageModel(){

    }
    protected GiftImageModel(Parcel in) {
        id = in.readInt();
        image = in.readInt();
    }

    public static final Creator<GiftImageModel> CREATOR = new Creator<GiftImageModel>() {
        @Override
        public GiftImageModel createFromParcel(Parcel in) {
            return new GiftImageModel(in);
        }

        @Override
        public GiftImageModel[] newArray(int size) {
            return new GiftImageModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(image);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
