package com.zhou.mybatistest.controller;

import com.zhou.mybatistest.model.Column;
import com.zhou.mybatistest.model.Student;
import com.zhou.mybatistest.model.Table;
import com.zhou.mybatistest.service.TableService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MyBatisController {

    private final TableService tableService;

    MyBatisController(TableService tableService) {
        this.tableService = tableService;
    }

    @GetMapping("/{table}/columns")
    public List<String> getTableColumns(@PathVariable("table") String table) {
        return tableService.selectColumnNames(table);
    }

    @GetMapping("/{table}/info")
    public List<Column> getTableInfo(@PathVariable("table") String table) {
        return tableService.selectColumns(table);
    }

    @GetMapping("/{table}")
    public Table getTable(@PathVariable("table") String table) {
        return tableService.selectTable(table.toUpperCase());
    }
}
