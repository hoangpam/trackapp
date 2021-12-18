package com.example.drivercar.model;

public class ModelDriver {
    private String AccountType,UID,MobileNo,FirstName,LastName,EmailId,City,Area,Password,CompleteAddress,State,ConfirmPassword,House,ImageURL,ImageURL1,ImageURL2,ImageURL3,Latitude,Longitude,Timestamp,Online;

    public ModelDriver() {
    }

    public ModelDriver(String accountType, String UID, String mobileNo, String firstName, String lastName, String emailId, String city, String area, String password, String completeAddress, String state, String confirmPassword, String house, String imageURL, String imageURL1, String imageURL2, String imageURL3, String latitude, String longitude, String timestamp, String online) {
        AccountType = accountType;
        this.UID = UID;
        MobileNo = mobileNo;
        FirstName = firstName;
        LastName = lastName;
        EmailId = emailId;
        City = city;
        Area = area;
        Password = password;
        CompleteAddress = completeAddress;
        State = state;
        ConfirmPassword = confirmPassword;
        House = house;
        ImageURL = imageURL;
        ImageURL1 = imageURL1;
        ImageURL2 = imageURL2;
        ImageURL3 = imageURL3;
        Latitude = latitude;
        Longitude = longitude;
        Timestamp = timestamp;
        Online = online;
    }

}
