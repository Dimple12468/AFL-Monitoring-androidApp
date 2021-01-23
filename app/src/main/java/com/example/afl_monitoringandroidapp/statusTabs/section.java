package com.example.afl_monitoringandroidapp.statusTabs;

import java.util.ArrayList;

public class section {
    public String sectionTitle;
    public ArrayList<String> Did,Dlocation_name,Dlocation_address,DadoName,DddaName;
    public boolean isPending,isOngoing,isCompleted;

    public section(String sectionTitle, ArrayList<String> Did, ArrayList<String> Dlocation_name, ArrayList<String> Dlocation_address, ArrayList<String> DadoName, ArrayList<String> DddaName, boolean isPending, boolean isOngoing, boolean isCompleted) {
        this.sectionTitle = sectionTitle;
        this.Did=Did;
        this.Dlocation_name = Dlocation_name;
        this.Dlocation_address = Dlocation_address;
        this.DadoName = DadoName;
        this.DddaName = DddaName;
        this.isPending = isPending;
        this.isOngoing = isOngoing;
        this.isCompleted = isCompleted;
    }

    public String getSectionTitle() {
        return sectionTitle;
    }
    public void setSectionTitle(String sectionTitle) {
        this.sectionTitle = sectionTitle;
    }

    public ArrayList<String> getDid() {
        return Did;
    }
    public void setDid(ArrayList<String> Did) {
        this.Did = Did;
    }

    public ArrayList<String> getDlocation_name() {
        return Dlocation_name;
    }
    public void setDlocation_name(ArrayList<String> Dlocation_name) {
        this.Dlocation_name = Dlocation_name;
    }

    public ArrayList<String> getDlocation_address() {
        return Dlocation_address;
    }
    public void setDlocation_address(ArrayList<String> Dlocation_address) {
        this.Dlocation_address = Dlocation_address;
    }

    public ArrayList<String> getDadoName() {
        return DadoName;
    }
    public void setDadoName(ArrayList<String> DadoName) {
        this.DadoName = DadoName;
    }

    public ArrayList<String> getDddaName() {
        return DddaName;
    }
    public void setDddaName(ArrayList<String> DddaName) {
        this.DddaName = DddaName;
    }

    public Boolean getIsPending() {
        return isPending;
    }
    public void setIsPending(boolean isPending) {
        this.isPending = isPending;
    }

    public Boolean getIsOngoing() {
        return isOngoing;
    }
    public void setIsOngoing(boolean isOngoing) {
        this.isOngoing = isOngoing;
    }

    public Boolean getIsCompleted() {
        return isCompleted;
    }
    public void setIsCompleted(boolean isCompleted) {
        this.isCompleted = isCompleted;
    }
}
