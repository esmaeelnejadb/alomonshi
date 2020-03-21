package com.alomonshi.object.enums;

import java.util.HashMap;
import java.util.Map;

public enum FilterItem {
    NEAREST("نزدیکترین"),
    CHEAPEST("ارزان ترین"),
    BEST("بهترین"),
    DISCOUNT("دارای تخفیف"),
    EXPENSIVE("گرانترین");
    private String value;
    private static Map<FilterItem, String> keyValueMap = new HashMap<>();

    FilterItem(String value){
        this.value = value;
    }

    public String getValue(){
        return this.value;
    }

    public static FilterItem getByValue(String value){
        for(FilterItem filterItem : values()){
            if(filterItem.value.equals(value))
                return filterItem;
        }
        return null;
    }

    public static Map<FilterItem, String> getFilterItemMap() {
        for(FilterItem filterItem : values()){
            keyValueMap.put(filterItem, filterItem.getValue());
        }
        return keyValueMap;
    }
}
