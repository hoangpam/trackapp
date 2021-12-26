package com.example.drivercar.model;

public class ModelInfomation {
    String Latitude,Longitude,cargoInfo,infomationId,nameCarInfo,nameLoGoInfo,nameLoInfo,productInfo,rankingTimeInfo,timestamp,uid;

    public ModelInfomation() {
    }

    public ModelInfomation(String latitude, String longitude, String cargoInfo, String infomationId, String nameCarInfo, String nameLoGoInfo, String nameLoInfo, String productInfo, String rankingTimeInfo, String timestamp, String uid) {
        Latitude = latitude;
        Longitude = longitude;
        this.cargoInfo = cargoInfo;
        this.infomationId = infomationId;
        this.nameCarInfo = nameCarInfo;
        this.nameLoGoInfo = nameLoGoInfo;
        this.nameLoInfo = nameLoInfo;
        this.productInfo = productInfo;
        this.rankingTimeInfo = rankingTimeInfo;
        this.timestamp = timestamp;
        this.uid = uid;
    }

    public String getLatitude() {
        return Latitude;
    }

    public void setLatitude(String latitude) {
        Latitude = latitude;
    }

    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String longitude) {
        Longitude = longitude;
    }

    public String getCargoInfo() {
        return cargoInfo;
    }

    public void setCargoInfo(String cargoInfo) {
        this.cargoInfo = cargoInfo;
    }

    public String getInfomationId() {
        return infomationId;
    }

    public void setInfomationId(String infomationId) {
        this.infomationId = infomationId;
    }

    public String getNameCarInfo() {
        return nameCarInfo;
    }

    public void setNameCarInfo(String nameCarInfo) {
        this.nameCarInfo = nameCarInfo;
    }

    public String getNameLoGoInfo() {
        return nameLoGoInfo;
    }

    public void setNameLoGoInfo(String nameLoGoInfo) {
        this.nameLoGoInfo = nameLoGoInfo;
    }

    public String getNameLoInfo() {
        return nameLoInfo;
    }

    public void setNameLoInfo(String nameLoInfo) {
        this.nameLoInfo = nameLoInfo;
    }

    public String getProductInfo() {
        return productInfo;
    }

    public void setProductInfo(String productInfo) {
        this.productInfo = productInfo;
    }

    public String getRankingTimeInfo() {
        return rankingTimeInfo;
    }

    public void setRankingTimeInfo(String rankingTimeInfo) {
        this.rankingTimeInfo = rankingTimeInfo;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
