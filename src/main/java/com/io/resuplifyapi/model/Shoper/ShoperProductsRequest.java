package com.io.resuplifyapi.model.Shoper;

public class ShoperProductsRequest extends ShoperRequest {

    private Params params;

    public ShoperProductsRequest(){super();}

    public ShoperProductsRequest(String method, int page){
        super("products", method);
        params = new Params(page);
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