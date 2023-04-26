package com.example.fooddistribution.Models;

public class NGOz {
    private String ngoName;
    private String address;
    private String sector;

    public NGOz(String ngoName, String address, String sector) {
        this.ngoName = ngoName;
        this.address = address;
        this.sector = sector;
    }

    public String getNgoName() {
        return ngoName;
    }

    public void setNgoName(String ngoName) {
        this.ngoName = ngoName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }
}
