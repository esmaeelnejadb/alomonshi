package com.alomonshi.object.enums;

public enum UserLevels {
    CLIENT(1), COMPANY_SUB_ADMIN(2), COMPANY_ADMIN(3), SITE_MANAGER(4);
    int value;
    UserLevels(int value){
        this.value = value;
    }
    public int getValue(){
        return this.value;
    }
    public static UserLevels getByValue(int value){
        for(UserLevels userLevel : values()){
            if(userLevel.value == value)
                return userLevel;
        }
        return null;
    }
}
