package com.io.resuplifyapi.domain;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name="prediction")
public class Prediction {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @Column(name="valid")
    private boolean valid;

    @Column(name="sale_per_day")
    private double salePerDay;

    @Column(name="out_of_stock_date")
    private LocalDate outOfStockDate;

    public Prediction(){}

    public Prediction(boolean valid){
        this.valid = valid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public double getSalePerDay() {
        return salePerDay;
    }

    public void setSalePerDay(double salePerDay) {
        this.salePerDay = salePerDay;
    }

    public LocalDate getOutOfStockDate() {
        return outOfStockDate;
    }

    public void setOutOfStockDate(LocalDate outOfStockDate) {
        this.outOfStockDate = outOfStockDate;
    }
}