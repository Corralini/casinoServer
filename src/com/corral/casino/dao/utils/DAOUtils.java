package com.corral.casino.dao.utils;

public class DAOUtils {

    public DAOUtils() {

    }

    public static void addClause(StringBuilder queryString, boolean first, String clause) {
        queryString.append(first ? " WHERE " : " AND ").append(clause);
    }

    public static void addUpdate(StringBuilder queryString, boolean first, String clause) {
        queryString.append(first ? " SET " : " , ").append(clause);
    }
}
