package com.example.fooddistribution.Models;

import com.google.firebase.firestore.DocumentId;

import java.io.Serializable;

public class NgoModel implements Serializable {

    @DocumentId
    private String documentId;

    private String imageUrl;

    private String ngoN;
    private String ncontact;

    private String ncity;
    private String npincode ;
    private String nemail ;
    private String ncountry ;
    private String naddress;
    private String nstate;
    private String typeofuser;

    public NgoModel(String imageUrl, String ngoN, String ncontact, String ncity, String npincode, String nemail, String ncountry, String naddress, String nstate, String typeofuser) {
        this.imageUrl = imageUrl;
        this.ngoN = ngoN;
        this.ncontact = ncontact;
        this.ncity = ncity;
        this.npincode = npincode;
        this.nemail = nemail;
        this.ncountry = ncountry;
        this.naddress = naddress;
        this.nstate = nstate;
        this.typeofuser = typeofuser;
    }

    public NgoModel() {
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getNgoN() {
        return ngoN;
    }

    public void setNgoN(String ngoN) {
        this.ngoN = ngoN;
    }

    public String getNcontact() {
        return ncontact;
    }

    public void setNcontact(String ncontact) {
        this.ncontact = ncontact;
    }

    public String getNcity() {
        return ncity;
    }

    public void setNcity(String ncity) {
        this.ncity = ncity;
    }

    public String getNpincode() {
        return npincode;
    }

    public void setNpincode(String npincode) {
        this.npincode = npincode;
    }

    public String getNemail() {
        return nemail;
    }

    public void setNemail(String nemail) {
        this.nemail = nemail;
    }

    public String getNcountry() {
        return ncountry;
    }

    public void setNcountry(String ncountry) {
        this.ncountry = ncountry;
    }

    public String getNaddress() {
        return naddress;
    }

    public void setNaddress(String naddress) {
        this.naddress = naddress;
    }

    public String getNstate() {
        return nstate;
    }

    public void setNstate(String nstate) {
        this.nstate = nstate;
    }

    public String getTypeofuser() {
        return typeofuser;
    }

    public void setTypeofuser(String typeofuser) {
        this.typeofuser = typeofuser;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }
}