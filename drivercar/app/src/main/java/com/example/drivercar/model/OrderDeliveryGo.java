package com.example.drivercar.model;

public class OrderDeliveryGo {
    String AreaLocation,CargoInfo,DeliveryDate,DeliveryDateGo,ID,Latitude,Longitude,NameCarInfo,NameLoGoInfo,NameLoInfo,PhoneCus,PhoneDriver,PriceDriverOrder,ProductInfo,RankingTimeInfo,Services,Services1,Services2,Services3,Services4,Status,TotalPrice,UID_Customer,UID_Driver;

    public OrderDeliveryGo() {
    }

    public OrderDeliveryGo(String areaLocation, String cargoInfo, String deliveryDate, String deliveryDateGo, String ID, String latitude, String longitude, String nameCarInfo, String nameLoGoInfo, String nameLoInfo, String phoneCus, String phoneDriver, String priceDriverOrder, String productInfo, String rankingTimeInfo, String services, String services1, String services2, String services3, String services4, String status, String totalPrice, String UID_Customer, String UID_Driver) {
        AreaLocation = areaLocation;
        CargoInfo = cargoInfo;
        DeliveryDate = deliveryDate;
        DeliveryDateGo = deliveryDateGo;
        this.ID = ID;
        Latitude = latitude;
        Longitude = longitude;
        NameCarInfo = nameCarInfo;
        NameLoGoInfo = nameLoGoInfo;
        NameLoInfo = nameLoInfo;
        PhoneCus = phoneCus;
        PhoneDriver = phoneDriver;
        PriceDriverOrder = priceDriverOrder;
        ProductInfo = productInfo;
        RankingTimeInfo = rankingTimeInfo;
        Services = services;
        Services1 = services1;
        Services2 = services2;
        Services3 = services3;
        Services4 = services4;
        Status = status;
        TotalPrice = totalPrice;
        this.UID_Customer = UID_Customer;
        this.UID_Driver = UID_Driver;
    }

    public String getAreaLocation() {
        return AreaLocation;
    }

    public void setAreaLocation(String areaLocation) {
        AreaLocation = areaLocation;
    }

    public String getCargoInfo() {
        return CargoInfo;
    }

    public void setCargoInfo(String cargoInfo) {
        CargoInfo = cargoInfo;
    }

    public String getDeliveryDate() {
        return DeliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        DeliveryDate = deliveryDate;
    }

    public String getDeliveryDateGo() {
        return DeliveryDateGo;
    }

    public void setDeliveryDateGo(String deliveryDateGo) {
        DeliveryDateGo = deliveryDateGo;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
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

    public String getNameCarInfo() {
        return NameCarInfo;
    }

    public void setNameCarInfo(String nameCarInfo) {
        NameCarInfo = nameCarInfo;
    }

    public String getNameLoGoInfo() {
        return NameLoGoInfo;
    }

    public void setNameLoGoInfo(String nameLoGoInfo) {
        NameLoGoInfo = nameLoGoInfo;
    }

    public String getNameLoInfo() {
        return NameLoInfo;
    }

    public void setNameLoInfo(String nameLoInfo) {
        NameLoInfo = nameLoInfo;
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

    public String getPriceDriverOrder() {
        return PriceDriverOrder;
    }

    public void setPriceDriverOrder(String priceDriverOrder) {
        PriceDriverOrder = priceDriverOrder;
    }

    public String getProductInfo() {
        return ProductInfo;
    }

    public void setProductInfo(String productInfo) {
        ProductInfo = productInfo;
    }

    public String getRankingTimeInfo() {
        return RankingTimeInfo;
    }

    public void setRankingTimeInfo(String rankingTimeInfo) {
        RankingTimeInfo = rankingTimeInfo;
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

    public String getTotalPrice() {
        return TotalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        TotalPrice = totalPrice;
    }

    public String getUID_Customer() {
        return UID_Customer;
    }

    public void setUID_Customer(String UID_Customer) {
        this.UID_Customer = UID_Customer;
    }

    public String getUID_Driver() {
        return UID_Driver;
    }

    public void setUID_Driver(String UID_Driver) {
        this.UID_Driver = UID_Driver;
    }
}
