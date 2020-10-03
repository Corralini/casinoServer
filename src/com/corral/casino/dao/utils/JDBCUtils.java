package com.corral.casino.dao.utils;

import com.corral.casino.exceptions.CasinoException;
import com.corral.casino.exceptions.Codes;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class JDBCUtils {

    public static void closeResultSet(ResultSet resultSet)
            throws CasinoException {

        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                throw new CasinoException(Codes.ERR_DB, e);
            }
        }

    }

    public static void closeStatement(Statement statement)
            throws CasinoException {

        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                throw new CasinoException(Codes.ERR_DB, e);
            }
        }

    }

    public static void closeConnection(Connection connection)
            throws CasinoException {

        if (connection != null) {
            try {
                // Previene un mal uso
                if (!connection.getAutoCommit()) {
                    throw new CasinoException(Codes.COMMIT_DISABLED);
                }

                connection.close();
            } catch (SQLException e) {
                throw new CasinoException(Codes.ERR_DB, e);
            }
        }
    }

    public static void closeConnection(Connection connection, boolean commitOrRollback)
            throws CasinoException {
        try {
            if (connection != null) {
                if (commitOrRollback) {
                    connection.commit();
                } else {
                    connection.rollback();
                }
                connection.close();
            }
        } catch (SQLException e) {
            throw new CasinoException(Codes.ERR_DB, e);
        }
    }
}
