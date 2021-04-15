package com.io.resuplifyapi.repository;

import com.io.resuplifyapi.model.Shoper.ShoperProductsRequest;

import java.util.ArrayList;
import java.util.List;

public class BulkProductsRequestBuilder {

    private List<List<ShoperProductsRequest>> bulkRequestsList;
    private final int productsPerRequestLimit = 50;
    private final int requestsPerBulkRequestLimit = 25;
    private int requestsNumber;
    private int bulkRequestsNumber;

    public List<List<ShoperProductsRequest>> prepare(int totalProductsNumber) {
        calculateRequestsNumber(totalProductsNumber);
        calculateBulkRequestsNumber();
        prepareBulkRequestsList();
        return bulkRequestsList;
    }

    private void calculateRequestsNumber(int productsNumber) {
        requestsNumber = (int) Math.ceil((double) productsNumber / productsPerRequestLimit);
    }

    private void calculateBulkRequestsNumber() {
        bulkRequestsNumber = (int) Math.ceil((double) requestsNumber / requestsPerBulkRequestLimit);
    }

    private void prepareBulkRequestsList() {
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

    private List<ShoperProductsRequest> getProductsRequestsListForPages(int pageStart, int pageStop){

        List<ShoperProductsRequest> requestsList = new ArrayList<>();

        for(int i = pageStart; i <= pageStop; i++){
            requestsList.add(new ShoperProductsRequest("GET", i));
        }
        return requestsList;
    }
}