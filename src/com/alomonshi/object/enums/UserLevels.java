package com.alomonshi.object.enums;

public enum UserLevels {
    CLIENT(0), SUB_ADMIN(1), ADMIN(2), MANAGER(3);
    int value;
    private UserLevels(int value){
        this.value = value;
    }
    public int getValue(){
        return this.value;
    }
}
