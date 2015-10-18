package com.example.nhat.myapplication;

/**
 * Created by Olaf on 16/10/2015.
 */
public class DataObject {
    private String theAction;
    private String thePatern;
    public void setTheAction(String action ){
        theAction=action;
    }
    public void setThePartern(String partern ){
        thePatern=partern;
    }
    public String getTheAction( ){
        return theAction;
    }
    public String getThePartern( ){
        return thePatern;
    }
}
