package com.zhou.mybatistest.model;

import java.util.List;
import java.util.Map;

public class Table {
    private List<Column> columns;
    private String key;
    private List<Map<String, Object>> content;

    public List<Column> getColumns() {
        return columns;
    }

    public void setColumns(List<Column> columns) {
        this.columns = columns;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public List<Map<String, Object>> getContent() {
        return content;
    }

    public void setContent(List<Map<String, Object>> content) {
        this.content = content;
    }
}
