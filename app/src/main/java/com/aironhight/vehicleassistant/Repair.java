package com.aironhight.vehicleassistant;
import java.time.LocalDate;
import java.util.Date;

public class Repair {
    private Date date;
    private String repair;
    private double cost;
    private long currentMileage;

    public Repair() { }

    public Repair(String repair, double cost, long currentMileage) {
        date = new Date();
        this.repair = repair;
        this.cost = cost;
        this.currentMileage = currentMileage;
    }

    public Date getDate() { return date; }
    public String getRepair() { return repair; }
    public double getCost() { return cost; }
    public long getCurrentMileage() { return currentMileage; }

    public String toString(){
        return repair + "($" + cost +"), "+ currentMileage + "\n" +
                "(" +date.getDate() +"."+ date.getMonth() + "." + date.getYear() + ")";



    }

}
