package com.aironhight.vehicleassistant;

import java.io.Serializable;
import java.util.ArrayList;

public class Vehicle implements Serializable{

    private String owner;
    private String make;
    private String model;
    private String specification;
    private int year;
    private String VIN;
    private long mileage;
    private ArrayList<Repair> repairs;
    private String pushID;

    public Vehicle() {}


    public Vehicle(String make, String model, String specification, int year,long mileage, String VIN, String owner) {
        this.make = make;
        this.model = model;
        this.specification = specification;
        this.year = year;
        this.VIN = VIN;
        this.mileage = mileage;
        repairs = new ArrayList<Repair>();
        this.owner = owner;
    }

    public String getMake() { return make; }

    public String getModel() { return model; }

    public String getSpecification() { return specification; }

    public int getYear() { return year; }

    public String getVIN() { return VIN; }

    public String getOwner(){ return owner; }

    public long getMileage() { return mileage; }

    public ArrayList<Repair> getRepairs() {
        return repairs;
    }

    public ArrayList<Repair> getRepairsWithoutDummy() {
        ArrayList<Repair> ret = repairs;
        ret.remove(0);
        return ret;
    }

    public String getPushID() { return pushID; }

    public void addRepair(Repair repair) { repairs.add(repair); }

    public void setSpecification(String specification) { this.specification = specification ;}

    public void setMileage(long mileage) { this.mileage = mileage; }

    public void setPushID (String id) { this.pushID = id; }

    public String toString() {
        String str = year + " " + make + " " + model;
        if (specification != null) {
            str += " " + specification;
        }
        return str;
    }
}
