package com.zhou.mybatistest.service.impl;

import com.zhou.mybatistest.mapper.TableMapper;
import com.zhou.mybatistest.model.Column;
import com.zhou.mybatistest.model.Table;
import com.zhou.mybatistest.service.TableService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TableServiceImpl implements TableService {

    private final TableMapper tableMapper;

    public TableServiceImpl(TableMapper tableMapper) {
        this.tableMapper = tableMapper;
    }

    @Override
    public List<String> selectColumnNames(String table) {
        return tableMapper.selectColumnNames(table.toUpperCase());
    }

    @Override
    public List<Column> selectColumns(String table) {
        return tableMapper.selectColumns(table.toUpperCase());
    }

    @Override
    @Transactional
    public Table selectTable(String table) {
        var res = new Table();

        var columns = tableMapper.selectColumns(table.toUpperCase());
        var key = columns.stream().filter(Column::getKey).map(Column::getName).collect(Collectors.joining("#"));
        var content = tableMapper.selectTableContent(table.toUpperCase());

        res.setColumns(columns);
        res.setKey(key);
        res.setContent(content);

        return res;
    }
}
