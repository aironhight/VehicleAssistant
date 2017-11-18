package com.aironhight.vehicleassistant;
import java.time.LocalDate;

public class Repair {
    private LocalDate date;
    private String repair;
    private double cost;
    private long currentMileage;

    public Repair() { }

    public Repair(String repair, double cost, long currentMileage) {
        date = null;
        this.repair = repair;
        this.cost = cost;
        this.currentMileage = currentMileage;
    }

    public LocalDate getDate() { return date; }
    public String getRepair() { return repair; }
    public double getCost() { return cost; }
    public long getCurrentMileage() { return currentMileage; }

}
