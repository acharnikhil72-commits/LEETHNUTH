package com.Hams.Leethnut.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "orders")
public class Order1 {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String area;
    private double slotScore;
    private int pastFail;
    private double zoneRate;
    private boolean cod;
    private double weatherScore;
    private int dayOfWeek;

    private String predictionResult;
    private int predictionPercent;

    // ← NEW: null = pending, "SUCCESS" = delivered, "FAILED" = not delivered
    private String deliveryStatus;

    // Getters & Setters
    public Long getId() {
        return id;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public double getSlotScore() {
        return slotScore;
    }

    public void setSlotScore(double slotScore) {
        this.slotScore = slotScore;
    }

    public int getPastFail() {
        return pastFail;
    }

    public void setPastFail(int pastFail) {
        this.pastFail = pastFail;
    }

    public double getZoneRate() {
        return zoneRate;
    }

    public void setZoneRate(double zoneRate) {
        this.zoneRate = zoneRate;
    }

    public boolean isCod() {
        return cod;
    }

    public void setCod(boolean cod) {
        this.cod = cod;
    }

    public double getWeatherScore() {
        return weatherScore;
    }

    public void setWeatherScore(double weatherScore) {
        this.weatherScore = weatherScore;
    }

    public int getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public String getPredictionResult() {
        return predictionResult;
    }

    public void setPredictionResult(String r) {
        this.predictionResult = r;
    }

    public int getPredictionPercent() {
        return predictionPercent;
    }

    public void setPredictionPercent(int p) {
        this.predictionPercent = p;
    }

    public String getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(String deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }
}
