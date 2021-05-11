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

    @Column(name="level")
    private int level;

    public Stock(){}

    public Stock(LocalDate date, int level) {
        this.date = date;
        this.level = level;
    }

    public Stock(LocalDate date) {
        this.date = date;
    }

    public Stock(int level) {
        this.level = level;
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

    public int getLevel() {
        return level;
    }

    public void setLevel(int stock) {
        this.level = stock;
    }
}