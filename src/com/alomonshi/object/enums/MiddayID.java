package com.alomonshi.object.enums;

public enum MiddayID {
    MORNING(1), AFTERNOON(2);
    private int value;
    private MiddayID(int value){
        this.value = value;
    }
    public int getValue(){
        return this.value;
    }
}
