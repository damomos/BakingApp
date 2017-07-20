package com.example.princess.bakingapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Princess on 7/14/2017.
 */

public class Steps implements Parcelable{

    private int id;
    private String shortDescription;
    private String fullDescription;
    private String videoURL;
    private String thumbnailURL;


    public Steps(JSONObject steps) {
        try {
            this.id = steps.getInt("id");
            this.shortDescription = steps.optString("shortDescription");
            this.fullDescription = steps.optString("description");
            this.videoURL = steps.optString("videoURL");
            this.thumbnailURL = steps.getString("thumbnailURL");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    protected Steps(Parcel in) {
        id = in.readInt();
        shortDescription = in.readString();
        fullDescription = in.readString();
        videoURL = in.readString();
        thumbnailURL = in.readString();
    }

    public static final Creator<Steps> CREATOR = new Creator<Steps>() {
        @Override
        public Steps createFromParcel(Parcel in) {
            return new Steps(in);
        }

        @Override
        public Steps[] newArray(int size) {
            return new Steps[size];
        }
    };

    public String getFullDescription() {
        return fullDescription;
    }

    public void setDescription(String description) {
        this.fullDescription = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getThumbnailURL() {
        return thumbnailURL;
    }

    public void setThumbnailURL(String thumbnailURL) {
        this.thumbnailURL = thumbnailURL;
    }

    public String getVideoURL() {
        return videoURL;
    }

    public void setVideoURL(String videoURL) {
        this.videoURL = videoURL;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(shortDescription);
        dest.writeString(fullDescription);
        dest.writeString(videoURL);
        dest.writeString(thumbnailURL);
    }

    public boolean hasVideo(){
        return !getVideoURL().isEmpty();
    }
}
