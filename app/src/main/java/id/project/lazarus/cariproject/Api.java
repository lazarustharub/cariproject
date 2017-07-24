package id.project.lazarus.cariproject;

import java.net.URL;

/**
 * Created by lazar on 5/14/2017.
 */

public class Api {
    public static final String URL_PREFIX = "http://";
    public static final String API_PREFIX = "/Api";
    public static final String URLPOINT = URL_PREFIX + "192.168.1.136/cariadmin_db/web" + API_PREFIX;
    public static final String VEHICLEPREFIX = "/vehicle";
    public static final String LOCATIONPREFIX = "/location";
    public static final String USERPREFIX = "/user";

    public static final String GET_CAR = URLPOINT+VEHICLEPREFIX+"/get-car";
    public static final String GET_ALL_CAR = URLPOINT+VEHICLEPREFIX+"/get-all-car";
    public static final String GET_LOCATION = URLPOINT+LOCATIONPREFIX+"/get-location";

    public static final String PARAM_IDCAR = "id_car";
    public static final String RESPONSE_NAME = "name";
    public static final String RESPONSE_MESSAGE = "message";
    public static final String RESPONSE_CODE = "code";
    public static final String RESPONSE_DATA = "data";

    public static final String RESPONSE_IDCAR = "id_car";
    public static final String RESPONSE_NAMECAR = "name_car";
    public static final String RESPONSE_YEARCAR = "year_car";
    public static final String RESPONSE_FUELCAR = "fuel_car";
    public static final String RESPONSE_SERVICECAR = "service_car";
    public static final String RESPONSE_LICENSECAR = "license_car";
    public static final String RESPONSE_BOUGHTCAR = "bought_car";
    public static final String RESPONSE_STATUS = "status";
    public static final String RESPONSE_PICTUREPROFIL = "picture_profil";
    public static final String RESPONSE_PICTURESEC = "picture_sec";
    public static final String RESPONSE_COLORCAR = "color_car";

}
