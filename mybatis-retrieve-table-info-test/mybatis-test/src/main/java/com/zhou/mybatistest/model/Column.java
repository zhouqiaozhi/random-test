package com.zhou.mybatistest.model;

public class Column {
    String name;
    Boolean isKey;
    Boolean isNullable;
    String type;
    Integer maxSize;
    Integer digits;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getKey() {
        return isKey;
    }

    public void setKey(Boolean key) {
        isKey = key;
    }

    public Boolean getNullable() {
        return isNullable;
    }

    public void setNullable(Boolean nullable) {
        isNullable = nullable;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(Integer maxSize) {
        this.maxSize = maxSize;
    }

    public Integer getDigits() {
        return digits;
    }

    public void setDigit(Integer digit) {
        this.digits = digit;
    }
}
