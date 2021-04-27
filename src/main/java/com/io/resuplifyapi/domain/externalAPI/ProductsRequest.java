package com.io.resuplifyapi.domain.externalAPI;

public class ProductsRequest {

    private String path = "/webapi/rest/products";
    private String method = "GET";
    private Params params;

    public ProductsRequest(int page){
        params = new Params(page);
    }

    public String getPath() {
        return path;
    }

    public String getMethod() {
        return method;
    }

    public Params getParams() {
        return params;
    }

    public class Params {
        private final String limit = "50";
        private final int page;

        private Params(int page){
            this.page = page;
        }

        public String getLimit() {
            return limit;
        }

        public int getPage() {
            return page;
        }
    }
}