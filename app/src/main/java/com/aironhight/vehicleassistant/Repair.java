package com.aironhight.vehicleassistant;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

public class Repair implements Serializable{
    private Date date;
    private String repair;
    private double cost;
    private long currentMileage;
    private String vehicleUID;

    public Repair() { }

    public Repair(String repair, double cost, long currentMileage, String vehicleUID) {
        date = new Date();
        this.repair = repair;
        this.cost = cost;
        this.currentMileage = currentMileage;
        this.vehicleUID = vehicleUID;
    }

    public Date getDate() { return date; }
    public String getRepair() { return repair; }
    public double getCost() { return cost; }
    public long getCurrentMileage() { return currentMileage; }
    public String getVehicleUID() { return vehicleUID; }

    public void setDate (Date date) { this.date = date; }


    public String toString(){
        return repair + "($" + cost +"), mileage: "+  currentMileage + "\n" +
                "(" +date.getDate() +"."+ date.getMonth() + "." + (date.getYear()+1900) + ")";



    }

}
