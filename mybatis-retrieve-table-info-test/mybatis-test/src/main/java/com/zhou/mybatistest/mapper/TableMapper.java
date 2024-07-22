package com.zhou.mybatistest.mapper;

import com.zhou.mybatistest.model.Column;
import com.zhou.mybatistest.model.Table;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface TableMapper {
    @Select("SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME=#{table}")
//    @MapKey("COLUMN_NAME")
    List<String> selectColumnNames(String table);
    @Select("SELECT \n" +
            "COLUMN_NAME,\n" +
            "COLUMN_KEY = 'PRI' as IS_KEY,\n" +
            "IS_NULLABLE = 'YES' as IS_NULLABLE, \n" +
            "CASE \n" +
            "\tWHEN CHARACTER_MAXIMUM_LENGTH IS NOT NULL THEN 'TEXT'\n" +
            "\tWHEN COLUMN_COMMENT = 'BOOLEAN' THEN 'BOOLEAN'\n" +
            "\tWHEN NUMERIC_PRECISION IS NOT NULL AND NUMERIC_SCALE = 0 THEN 'INT'\n" +
            "\tWHEN NUMERIC_PRECISION IS NOT NULL THEN 'DOUBLE'\n" +
            "\tWHEN DATETIME_PRECISION IS NOT NULL THEN 'TIME'\n" +
            "END as DATA_TYPE,\n" +
            "CASE \n" +
            "\tWHEN CHARACTER_MAXIMUM_LENGTH IS NOT NULL THEN CHARACTER_MAXIMUM_LENGTH\n" +
            "\tWHEN NUMERIC_PRECISION IS NOT NULL THEN NUMERIC_PRECISION\n" +
            "\tWHEN DATETIME_PRECISION IS NOT NULL THEN DATETIME_PRECISION\n" +
            "END as MAX_SIZE,\n" +
            "NUMERIC_SCALE\n" +
            "FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME=#{table}")
    @ResultMap("TableResultMap")
    List<Column> selectColumns(String table);

    @Select("SELECT * FROM ${table}")
    List<Map<String, Object>> selectTableContent(String table);
}