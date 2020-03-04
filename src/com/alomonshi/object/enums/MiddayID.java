package com.alomonshi.object.enums;

public enum MiddayID {
    MORNING(1), AFTERNOON(2);
    private int value;
    MiddayID(int value){
        this.value = value;
    }
    public int getValue(){
        return this.value;
    }

    public static MiddayID getByValue(int value) {
        for(MiddayID middayID : values()) {
            if (middayID.value == value)
                return middayID;
        }
        return null;
    }
}
