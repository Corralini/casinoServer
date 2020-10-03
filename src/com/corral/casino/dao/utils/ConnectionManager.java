package com.corral.casino.dao.utils;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ConnectionManager {
    private static Logger logger = LogManager.getLogger(ConnectionManager.class.getName());
    private static ResourceBundle dbConfig = ResourceBundle.getBundle("DBConfiguration");

    private static ComboPooledDataSource pooledDataSource = null;

    static {
        try {
            pooledDataSource = new ComboPooledDataSource();
            pooledDataSource.setDriverClass(dbConfig.getString("jdbc.driver.classname"));
            pooledDataSource.setJdbcUrl(dbConfig.getString("jdbc.url"));
            pooledDataSource.setUser(dbConfig.getString("jdbc.user"));
            pooledDataSource.setPassword(dbConfig.getString("jdbc.password"));
        } catch (PropertyVetoException e) {
            logger.fatal(e.getMessage(), e);
        }
    }

    private ConnectionManager() {
    }

    public final static Connection getConnection() throws SQLException {
        return pooledDataSource.getConnection();
    }
}
