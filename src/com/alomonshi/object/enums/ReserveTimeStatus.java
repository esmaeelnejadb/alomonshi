package com.alomonshi.object.enums;

public enum ReserveTimeStatus {
    RESERVABLE(1), RESERVED(2), CANCELED(3), HOLD(4), DELETED(5);
    private int value;

    ReserveTimeStatus(int value){
        this.value = value;
    }

    public int getValue(){
        return this.value;
    }

    public static ReserveTimeStatus getByValue(int value){
        for(ReserveTimeStatus reserveTimeStatus : values()){
            if(reserveTimeStatus.value == value)
                return reserveTimeStatus;
        }
        return null;
    }
}