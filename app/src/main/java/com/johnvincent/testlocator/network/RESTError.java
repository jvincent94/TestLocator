package com.johnvincent.testlocator.network;

import com.google.gson.annotations.Expose;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Response;


public class RESTError {

    @Expose
    private HashMap<String, Object> errors;

    public HashMap<String, Object> getErrors() {
        return errors;
    }

    private RESTError() {
    }

    public static RESTError getInstance(Response response) {
        RESTError error;
        try {
            Converter<ResponseBody, RESTError> errorConverter = RESTClient.getInstance().getRetrofit().responseBodyConverter(RESTError.class, new Annotation[0]);
            error = errorConverter.convert(response.errorBody());
        } catch (Exception e) {
            e.printStackTrace();
            return new RESTError();
        }
        return error;
    }

    public static RESTError getInstance() {
        return new RESTError();
    }

    public String getErrorsString() {
        StringBuilder db = new StringBuilder();
        if (errors != null) {
            for (Map.Entry<String, Object> entry : errors.entrySet()) {
                db.append(entry.getKey());
                db.append(":");
                db.append(entry.getValue());
                db.append(" ");
            }
            return db.toString();
        }
        return "Unknown server error";
    }

    public String getTextViewString() {
        StringBuilder db = new StringBuilder();
        String error = "Unknown server error";
        if (errors != null) {
            for (Map.Entry<String, Object> entry : errors.entrySet()) {
                String temp = entry.getValue().toString();
                db.append(temp.substring(1, temp.length() - 1)).append("\n");
            }
            if (error.length() > 2)
                error = db.toString().substring(0, db.length() - 2);
        }
        return error;
    }
}
