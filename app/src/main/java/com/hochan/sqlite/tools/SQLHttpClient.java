package com.hochan.sqlite.tools;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.net.URL;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpEntity;

/**
 * Created by Administrator on 2016/5/5.
 */
public class SQLHttpClient{

    public final static String BASE_URL = "http://192.168.1.40/";
    public final static String GET_ALL = "http://192.168.1.140:8080/a";
    public final static String UPLOAD_ALL = "http://192.168.1.140:8080/b";
    public final static String SYNC_UPLOAD = "http://192.168.1.140:8080/d";
    public final static String SYNC_DOWNLOAD = "http://192.168.1.140:8080/c";

    public final static String GET_FILE = "http://192.168.1.157:8080/fechDirectory";

    private static AsyncHttpClient mClient = new AsyncHttpClient();

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        mClient.get(url, params, responseHandler);
    }

    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        mClient.post(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void post(Context context, String url, HttpEntity httpEntity, String contentType,
                            AsyncHttpResponseHandler responseHandler){
        mClient.post(context, url, httpEntity, contentType, responseHandler);
    }


    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }

    public static String handleFailure(int statusCode, Header[] headers, String responseString, Throwable throwable){
        StringBuilder sb = new StringBuilder();
        sb.append("statusCode:"+String.valueOf(statusCode));
        sb.append("\n------\n");
        for(Header header : headers){
            sb.append(header.getName()+":"+header.getValue());
        }
        sb.append("\n------\n");
        sb.append("responseString:"+responseString);
        return sb.toString();
    }

}
