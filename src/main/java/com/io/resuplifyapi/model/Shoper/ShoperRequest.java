package com.io.resuplifyapi.model.Shoper;

public abstract class ShoperRequest {

    protected String path = "/webapi/rest/";
    protected String method;

    public ShoperRequest(){}

    public ShoperRequest(String path, String method) {
        this.path += path;
        this.method = method;
    }

    public String getPath() {
        return path;
    }

    public String getMethod() {
        return method;
    }
}