package com.smartown.framework.mission;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

public class Request {

    public final static String REQUEST_TYPE_POST = "POST";
    public final static String REQUEST_TYPE_GET = "GET";

    private String url = "";
    private List<RequestParam> requestParams = new ArrayList<>();
    private boolean saveCookie = false;
    private boolean useCookie = false;
    private String requestType = REQUEST_TYPE_POST;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isUseCookie() {
        return useCookie;
    }

    public void setUseCookie(boolean useCookie) {
        this.useCookie = useCookie;
    }

    public boolean isSaveCookie() {
        return saveCookie;
    }

    public void setSaveCookie(boolean saveCookie) {
        this.saveCookie = saveCookie;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public List<RequestParam> getRequestParams() {
        return requestParams;
    }

    public void addRequestParam(String key, String value) {
        if (TextUtils.isEmpty(key) || TextUtils.isEmpty(value)) {
            return;
        }
        requestParams.add(new RequestParam(key, value));
    }

    public void addRequestParam(List<RequestParam> params) {
        if (params != null) {
            requestParams.addAll(params);
        }
    }

}
