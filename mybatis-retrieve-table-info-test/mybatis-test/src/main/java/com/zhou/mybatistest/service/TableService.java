package com.zhou.mybatistest.service;

import com.zhou.mybatistest.model.Column;
import com.zhou.mybatistest.model.Table;

import java.util.List;

public interface TableService {
    List<String> selectColumnNames(String table);
    List<Column> selectColumns(String table);
    Table selectTable(String table);
}
