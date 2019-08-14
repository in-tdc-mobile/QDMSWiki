package com.mariapps.qdmswiki.serviceclasses;

import com.mariapps.qdmswiki.AppConfig;

import java.io.IOException;
import java.security.cert.CertificateException;
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
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by aruna.ramakrishnan on 14-08-2019
 */

public class QDMSWikiNetworkService {

    private static String baseUrl = AppConfig.BASE_URL;
    private QDMSWikiApi networkAPI;

    public QDMSWikiNetworkService()   {
        this(baseUrl);
    }

    public QDMSWikiNetworkService(String baseUrl) {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = getUnsafeOkHttpClient();//new OkHttpClient.Builder().readTimeout(60, TimeUnit.SECONDS).connectTimeout(60,TimeUnit.SECONDS).writeTimeout(60,TimeUnit.SECONDS);
        httpClient.addInterceptor(logging);

        httpClient.connectTimeout(90, TimeUnit.SECONDS);
        httpClient.writeTimeout(90, TimeUnit.SECONDS);
        httpClient.readTimeout(90, TimeUnit.SECONDS);
        httpClient.addNetworkInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request originalRequest = chain.request();
                Request authorisedRequest = originalRequest.newBuilder()
                        .header("Content-Type","application/json")
                        .build();
                return chain.proceed(authorisedRequest);
            }
        });

        Retrofit retrofit = new Retrofit.Builder().baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(httpClient.build())
                .build();

        networkAPI = retrofit.create(QDMSWikiApi.class);

    }


    public static OkHttpClient.Builder getUnsafeOkHttpClient() {
        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();


            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0]);
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
            return builder;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Method to return the API interface.
     */

    public QDMSWikiApi getAPI() {
        return networkAPI;
    }
}
