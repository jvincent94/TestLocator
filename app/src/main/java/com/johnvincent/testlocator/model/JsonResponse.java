package com.johnvincent.testlocator.model;

import com.google.gson.annotations.SerializedName;

public class JsonResponse {

    @SerializedName("id")
    public String mId;

    @SerializedName("title")
    public String mTitle;

    @SerializedName("address")
    public String[] mAddress;

    @SerializedName("telephone")
    public String mTelephone;

    @SerializedName("website")
    public String mWebsite;

    @SerializedName("lat")
    public String mLat;

    @SerializedName("lon")
    public String mLon;

    @SerializedName("distance")
    public int mDistance;

    @SerializedName("mapLink")
    public Maplink mMapLink;

    @SerializedName("openingtimes")
    public String[] mOpeningTimes;

    public JsonResponse(){ /*empty constructor*/ }

    public String getmId() {
        return mId;
    }

    public void setmId(String mId) {
        this.mId = mId;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String[] getmAddress() {
        return mAddress;
    }

    public void setmAddress(String[] mAddress) {
        this.mAddress = mAddress;
    }

    public String getmTelephone() {
        return mTelephone;
    }

    public void setmTelephone(String mTelephone) {
        this.mTelephone = mTelephone;
    }

    public String getmWebsite() {
        return mWebsite;
    }

    public void setmWebsite(String mWebsite) {
        this.mWebsite = mWebsite;
    }

    public String getmLat() {
        return mLat;
    }

    public void setmLat(String mLat) {
        this.mLat = mLat;
    }

    public String getmLon() {
        return mLon;
    }

    public void setmLon(String mLon) {
        this.mLon = mLon;
    }

    public int getmDistance() {
        return mDistance;
    }

    public void setmDistance(int mDistance) {
        this.mDistance = mDistance;
    }

    public Maplink getmMapLink() {
        return mMapLink;
    }

    public void setmMapLink(Maplink mMapLink) {
        this.mMapLink = mMapLink;
    }

    public String[] getmOpeningTimes() {
        return mOpeningTimes;
    }

    public void setmOpeningTimes(String[] mOpeningTimes) {
        this.mOpeningTimes = mOpeningTimes;
    }
}
