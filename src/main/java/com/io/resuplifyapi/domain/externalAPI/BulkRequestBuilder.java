package com.io.resuplifyapi.domain.externalAPI;

import java.util.ArrayList;
import java.util.List;

public class BulkRequestBuilder {

    private List<List<ProductsRequest>> bulkRequestsList;
    private final int productsPerRequestLimit = 50;
    private final int requestsPerBulkRequestLimit = 25;
    private int requestsNumber;
    private int bulkRequestsNumber;

    public List<List<ProductsRequest>> prepareProductsRequests(int totalProductsNumber) {
        calculateRequestsNumber(totalProductsNumber);
        calculateBulkRequestsNumber();
        prepareBulkRequests();
        return bulkRequestsList;
    }

    private void calculateRequestsNumber(int productsNumber) {
        requestsNumber = (int) Math.ceil((double) productsNumber / productsPerRequestLimit);
    }

    private void calculateBulkRequestsNumber() {
        bulkRequestsNumber = (int) Math.ceil((double) requestsNumber / requestsPerBulkRequestLimit);
    }

    private void prepareBulkRequests() {
        int pageStart = 1;
        int pageStop = 0;

        bulkRequestsList = new ArrayList<>();
        for(int i = 1; i <= bulkRequestsNumber; i++){

            if(isLastBulkRequest(i)){
                pageStop += requestsNumber % requestsPerBulkRequestLimit;
            }
            else{
                pageStop += requestsPerBulkRequestLimit;
            }

            bulkRequestsList.add(getProductsRequestsListForPages(pageStart, pageStop));
            pageStart += requestsPerBulkRequestLimit;
        }
    }

    private boolean isLastBulkRequest(int i){
        return i == bulkRequestsNumber;
    }

    private List<ProductsRequest> getProductsRequestsListForPages(int pageStart, int pageStop){

        List<ProductsRequest> requestsList = new ArrayList<>();

        for(int i = pageStart; i <= pageStop; i++){
            requestsList.add(new ProductsRequest(i));
        }
        return requestsList;
    }
}