package com.example.drivercar.model;

public class ModelRole {
    String AccountType,ID,UID,MobileNo;

    public ModelRole(String accountType, String ID, String UID, String mobileNo) {
        AccountType = accountType;
        this.ID = ID;
        this.UID = UID;
        MobileNo = mobileNo;
    }

    public ModelRole() {
    }

    public String getAccountType() {
        return AccountType;
    }

    public void setAccountType(String accountType) {
        AccountType = accountType;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getMobileNo() {
        return MobileNo;
    }

    public void setMobileNo(String mobileNo) {
        MobileNo = mobileNo;
    }
}
