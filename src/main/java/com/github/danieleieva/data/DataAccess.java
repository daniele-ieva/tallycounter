package com.github.danieleieva.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;
import java.util.UUID;

public class DataAccess implements AutoCloseable {
    private final Connection con;
    private final Map<String, PreparedStatement> statements;

    private static final Logger log = LoggerFactory.getLogger(DataAccess.class);

    public DataAccess(Connection con, Map<String, PreparedStatement> statements) {
        this.con = con;
        this.statements = statements;
    }

    public void createCategory(String category) {
        var ps = statements.get("category_create");
        try {
            ps.setObject(1, UUID.randomUUID());
            ps.setString(2, category);
            ps.executeUpdate();
        } catch (SQLException e) {
            log.error("Error creating category", e);
            try {
                con.rollback();
            } catch (SQLException e1) {
                log.error("Error rolling back transaction", e1);
            }
        }
    }

    @Override
    public void close() throws Exception {
        con.commit();
    }
}
