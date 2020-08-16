package com.alomonshi.object.enums;

public enum MediaType {
    PICTURE(1), VIDEO(2);
    private int value;

    MediaType(int value){
        this.value = value;
    }

    public int getValue(){
        return this.value;
    }

    public static MediaType getByValue(int value) {
        for(MediaType middayID : values()) {
            if (middayID.value == value)
                return middayID;
        }
        return null;
    }
}
