package com.example.drivercar.model;

public class ModelInfomation {
    String AreaLocation,Latitude,Longitude,PhoneCus,PhoneDriver,Services,Services1,Services2,Services3,Services4,Status,cargoInfo,infomationId,nameCarInfo,nameLoGoInfo,nameLoInfo,productInfo,rankingTimeInfo,timestamp,uid;

    public ModelInfomation() {
    }

    public ModelInfomation(String areaLocation, String latitude, String longitude, String phoneCus, String phoneDriver, String services, String services1, String services2, String services3, String services4, String status, String cargoInfo, String infomationId, String nameCarInfo, String nameLoGoInfo, String nameLoInfo, String productInfo, String rankingTimeInfo, String timestamp, String uid) {
        AreaLocation = areaLocation;
        Latitude = latitude;
        Longitude = longitude;
        PhoneCus = phoneCus;
        PhoneDriver = phoneDriver;
        Services = services;
        Services1 = services1;
        Services2 = services2;
        Services3 = services3;
        Services4 = services4;
        Status = status;
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

    public String getAreaLocation() {
        return AreaLocation;
    }

    public void setAreaLocation(String areaLocation) {
        AreaLocation = areaLocation;
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

    public String getPhoneCus() {
        return PhoneCus;
    }

    public void setPhoneCus(String phoneCus) {
        PhoneCus = phoneCus;
    }

    public String getPhoneDriver() {
        return PhoneDriver;
    }

    public void setPhoneDriver(String phoneDriver) {
        PhoneDriver = phoneDriver;
    }

    public String getServices() {
        return Services;
    }

    public void setServices(String services) {
        Services = services;
    }

    public String getServices1() {
        return Services1;
    }

    public void setServices1(String services1) {
        Services1 = services1;
    }

    public String getServices2() {
        return Services2;
    }

    public void setServices2(String services2) {
        Services2 = services2;
    }

    public String getServices3() {
        return Services3;
    }

    public void setServices3(String services3) {
        Services3 = services3;
    }

    public String getServices4() {
        return Services4;
    }

    public void setServices4(String services4) {
        Services4 = services4;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
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
