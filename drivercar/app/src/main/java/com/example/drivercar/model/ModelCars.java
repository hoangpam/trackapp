package com.example.drivercar.model;

public class ModelCars {
    String City,CompleteAddress,Latitude,License_Plates,Longitude,Plate,SizeCar,Status,Timestamp,VehicleTonnage,VehicleTypeName,carId,uid;

    public ModelCars() {
    }

    public ModelCars(String city, String completeAddress, String latitude, String license_Plates, String longitude, String plate, String sizeCar, String status, String timestamp, String vehicleTonnage, String vehicleTypeName, String carId, String uid) {
        City = city;
        CompleteAddress = completeAddress;
        Latitude = latitude;
        License_Plates = license_Plates;
        Longitude = longitude;
        Plate = plate;
        SizeCar = sizeCar;
        Status = status;
        Timestamp = timestamp;
        VehicleTonnage = vehicleTonnage;
        VehicleTypeName = vehicleTypeName;
        this.carId = carId;
        this.uid = uid;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getCompleteAddress() {
        return CompleteAddress;
    }

    public void setCompleteAddress(String completeAddress) {
        CompleteAddress = completeAddress;
    }

    public String getLatitude() {
        return Latitude;
    }

    public void setLatitude(String latitude) {
        Latitude = latitude;
    }

    public String getLicense_Plates() {
        return License_Plates;
    }

    public void setLicense_Plates(String license_Plates) {
        License_Plates = license_Plates;
    }

    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String longitude) {
        Longitude = longitude;
    }

    public String getPlate() {
        return Plate;
    }

    public void setPlate(String plate) {
        Plate = plate;
    }

    public String getSizeCar() {
        return SizeCar;
    }

    public void setSizeCar(String sizeCar) {
        SizeCar = sizeCar;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getTimestamp() {
        return Timestamp;
    }

    public void setTimestamp(String timestamp) {
        Timestamp = timestamp;
    }

    public String getVehicleTonnage() {
        return VehicleTonnage;
    }

    public void setVehicleTonnage(String vehicleTonnage) {
        VehicleTonnage = vehicleTonnage;
    }

    public String getVehicleTypeName() {
        return VehicleTypeName;
    }

    public void setVehicleTypeName(String vehicleTypeName) {
        VehicleTypeName = vehicleTypeName;
    }

    public String getCarId() {
        return carId;
    }

    public void setCarId(String carId) {
        this.carId = carId;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
