package com.io.resuplifyapi.domain;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name="stock")
public class Stock {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @Column(name="date")
    private LocalDate date;

    @Column(name="stock")
    private int stock;

    public Stock(){}

    public Stock(LocalDate date, int stock) {
        this.date = date;
        this.stock = stock;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }
}