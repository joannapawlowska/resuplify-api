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

    @Column(name="warn_level_date")
    private LocalDate warnLevelDate;

    public Prediction(){}

    public Prediction(boolean valid){
        this.valid = valid;
    }

    public Prediction(boolean valid, LocalDate warnLevelDate){
        this(valid);
        this.warnLevelDate = warnLevelDate;
    }

    public Prediction(boolean valid, LocalDate warnLevelDate, double salePerDay){
        this(valid, warnLevelDate);
        this.salePerDay = salePerDay;
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

    public LocalDate getWarnLevelDate() {
        return warnLevelDate;
    }

    public void setWarnLevelDate(LocalDate outOfStockDate) {
        this.warnLevelDate = outOfStockDate;
    }
}