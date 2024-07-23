package com.zhou.postgres.datasource;

import java.sql.Connection;
import java.sql.SQLException;

import org.postgresql.PGConnection;
import org.postgresql.jdbc.PgConnection;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import com.zhou.postgres.model.CountryBasedCustomers;

public class CustomDatasource extends HikariDataSource {
	public CustomDatasource() {
        super();
    }

    public CustomDatasource(HikariConfig configuration) {
        super(configuration);
    }
    
    @Override
    public Connection getConnection() throws SQLException {
        Connection conn = super.getConnection();
        prepareConnection(conn); 
        return conn;
    }
    
    protected void prepareConnection(Connection connection) throws SQLException {
        if (connection != null) {
            PgConnection pgconn = ((PgConnection) connection.unwrap(PGConnection.class));
            pgconn.addDataType("countrybasedcustomers", CountryBasedCustomers.class);
        }
    }
}
