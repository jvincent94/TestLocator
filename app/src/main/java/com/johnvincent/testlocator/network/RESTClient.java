package com.johnvincent.testlocator.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.johnvincent.testlocator.util.LogUtils;

import java.io.IOException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;


import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import okio.Buffer;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by John on 1/15/2019.
 */

public class RESTClient {


    private static final String BASE_URL = "https://staging.dochq.co.uk/api/v2/";
    private Retrofit retrofit;
    private final OkHttpClient client;

    private static RESTClient ourInstance = new RESTClient();

    public static RESTClient getInstance() {
        return ourInstance;
    }

    public RESTClient() {
        Gson gson = new GsonBuilder().create();
        LoggingInterceptor interceptor = new LoggingInterceptor();
        client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .hostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String arg0, SSLSession arg1) {
                        return true;
                    }
                })
                .connectTimeout(1, TimeUnit.MINUTES)
                .sslSocketFactory(getSSLSocketFactory())
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build();
    }

    public ApiService getApiService() { return retrofit.create(ApiService.class); }

    public OkHttpClient getClient() {
        return client;
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }

    public SSLSocketFactory getSSLSocketFactory() {
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        public X509Certificate[] getAcceptedIssuers() {
                            return new X509Certificate[0];
                        }

                        @Override
                        public void checkClientTrusted(X509Certificate[] certs, String authType) {
                        }

                        @Override
                        public void checkServerTrusted(X509Certificate[] certs, String authType) {
                        }
                    }
            };

            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new SecureRandom());
            return sc.getSocketFactory();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static class LoggingInterceptor implements Interceptor {
        @Override
        public okhttp3.Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            LogUtils.e("RESTClient", "url: " + request.url() + " body: " + bodyToString(request));
            okhttp3.Response response = chain.proceed(request);
            String bodyString = response.body().string();
            LogUtils.e("RESTClient", "url: " + request.url() + " code: " + String.valueOf(response.code()) + "; response: " + bodyString);
            return response.newBuilder()
                    .body(ResponseBody.create(response.body().contentType(), bodyString))
                    .build();

        }


        public static String bodyToString(final Request request) {
            try {
                final Request copy = request.newBuilder().build();
                final Buffer buffer = new Buffer();
                if (copy != null && copy.body() != null) {
                    copy.body().writeTo(buffer);
                }
                return buffer.readUtf8();
            } catch (final IOException e) {
                return "did not work";
            }
        }

    }

}
