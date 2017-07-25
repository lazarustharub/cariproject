package id.project.lazarus.cariproject;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by lazar on 7/24/2017.
 */

public class Car
{
    private String id_car;
    private String name_car;
    private String year_car;
    private String fuel_car;
    private String bought_car;
    private String service_car;
    private String license_car;
    private String status;
    private String picture_profil;
    private String picture_sec;
    private String color_car;

    public Car(String id_car, String name_car, String year_car, String fuel_car, String bought_car, String service_car, String license_car, String status,
               String profil_pictute, String picture_sec, String color_car) {
        this.id_car = id_car;
        this.name_car = name_car;
        this.year_car = year_car;
        this.fuel_car = fuel_car;
        this.bought_car = bought_car;
        this.service_car = service_car;
        this.license_car = license_car;
        this.status = status;
        this.picture_profil = profil_pictute;
        this.picture_sec = picture_sec;
        this.color_car = color_car;
    }

    public Car(JSONObject jsonObject){
        try {
            this.id_car = jsonObject.getString("id_car");
            this.name_car = jsonObject.getString("name_car");
            this.year_car = jsonObject.getString("year_car");
            this.fuel_car = jsonObject.getString("fuel_car");
            this.bought_car = jsonObject.getString("bought_car");
            this.service_car = jsonObject.getString("service_car");
            this.license_car = jsonObject.getString("license_car");
            this.status = jsonObject.getString("status");
            this.picture_profil = jsonObject.getString("picture_profil");
            this.picture_sec = jsonObject.getString("picture_sec");
            this.color_car = jsonObject.getString("color_car");

        } catch (JSONException e) {
            Log.e("Error Ini Ternyata", "error"+ e.getMessage());
            e.printStackTrace();
        }
    }

    public String getId_car() {
        return id_car;
    }

    public void setId_car(String id_car) {
        this.id_car = id_car;
    }

    public String getName_car() {
        return name_car;
    }

    public void setName_car(String name_car) {
        this.name_car = name_car;
    }

    public String getYear_car() {
        return year_car;
    }

    public void setYear_car(String year_car) {
        this.year_car = year_car;
    }

    public String getFuel_car() {
        return fuel_car;
    }

    public void setFuel_car(String fuel_car) {
        this.fuel_car = fuel_car;
    }

    public String getBought_car() {
        return bought_car;
    }

    public void setBought_car(String bought_car) {
        this.bought_car = bought_car;
    }

    public String getService_car() {
        return service_car;
    }

    public void setService_car(String service_car) {
        this.service_car = service_car;
    }

    public String getLicense_car() {
        return license_car;
    }

    public void setLicense_car(String license_car) {
        this.license_car = license_car;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getProfil_pictute() {
        return picture_profil;
    }

    public void setProfil_pictute(String profil_pictute) {
        this.picture_profil = profil_pictute;
    }

    public String getPicture_sec() {
        return picture_sec;
    }

    public void setPicture_sec(String picture_sec) {
        this.picture_sec = picture_sec;
    }

    public String getColor_car() {
        return color_car;
    }

    public void setColor_car(String color_car) {
        this.color_car = color_car;
    }
}