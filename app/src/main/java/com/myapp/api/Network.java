package com.myapp.api;

import androidx.annotation.IntDef;
import androidx.annotation.StringDef;

public class Network {


    @IntDef({
            Errors.HTTP_RESPONSE_OK,
            Errors.HTTP_RESPONSE_AUTHORIZATION_ERROR,
            Errors.HTTP_RESPONSE_AUTHENTICATION_ERROR,
            Errors.HTTP_RESPONSE_NOT_FOUND,
            Errors.HTTP_UNKNOWN_ERROR
    })
    public @interface Errors {
        int HTTP_RESPONSE_AUTHORIZATION_ERROR = 403;
        int HTTP_RESPONSE_AUTHENTICATION_ERROR = 401;
        int HTTP_RESPONSE_NOT_FOUND = 404;
        int HTTP_UNKNOWN_ERROR = 500;
        int HTTP_RESPONSE_OK = 200 ;
    }


    @StringDef({
            CustomFieldType.URGENT,
            CustomFieldType.BOOLEAN,
            CustomFieldType.LIST,
            CustomFieldType.TEXT,
            CustomFieldType.ASSET
    })
    public @interface CustomFieldType {
        String URGENT = "URGENT";
        String LIST = "LIST";
        String TEXT = "TEXT";
        String BOOLEAN = "BOOLEAN";
        String ASSET = "ASSET" ;
    }

    public static String BASE_URL_V2 = "https://api.fieldbit.net/fieldbit/rest/";
    public static String GUEST_MODE_BASE_URL = "https://hero.fieldbit.net/#app/ticket/";

}
