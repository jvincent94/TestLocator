package com.johnvincent.testlocator.network;

import android.content.Context;
import android.widget.Toast;
import com.google.gson.Gson;
import com.johnvincent.testlocator.MyApp;
import com.johnvincent.testlocator.R;

import java.io.IOException;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public abstract class RESTCallback<T> implements Callback<T> {

    private Context context;

    public RESTCallback(Context context) {
        this.context = context;
    }



    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        if (response.isSuccessful()) {

            onResponseSuccessful(response);
        } else {
            String temp = null;
            Response<T> tempResponse = null;
            try {
                byte[] data = response.errorBody().bytes();
                ResponseBody responseBody = ResponseBody.create(response.errorBody().contentType(), data);
                tempResponse = Response.error(response.code(), responseBody);
                temp = new String(data);
            } catch (IOException e) {
                e.printStackTrace();
            }

            RESTError restError = new Gson().fromJson(temp, RESTError.class);
            onResponseFail(tempResponse != null ? tempResponse : response, restError);
        }
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        Toast.makeText(MyApp.getContext(), MyApp.getContext().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
        onFailure(t);
    }

    public abstract void onResponseSuccessful(Response<T> response);

    public void onResponseFail(Response response, RESTError restError) { }

    public void onFailure(Throwable t) { }

}
