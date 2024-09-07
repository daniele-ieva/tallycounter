package com.github.danieleieva.data;

import com.github.danieleieva.data.records.Category;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

public class DataAccess implements AutoCloseable {
    private final Connection con;
    private final Map<String, PreparedStatement> statements;
    private Boolean updated = false;

    private static final Logger log = LoggerFactory.getLogger(DataAccess.class);

    public DataAccess(Connection con, Map<String, PreparedStatement> statements) {
        this.con = con;
        this.statements = statements;
    }

    public void createCategory(String category) throws SQLException {
        var ps = statements.get("category_create");
        try {
            ps.setObject(1, UUID.randomUUID());
            ps.setString(2, category);
            ps.executeUpdate();
            updated = true;
        } catch (SQLException e) {
            log.error("Error creating category", e);
            try {
                con.rollback();
            } catch (SQLException e1) {
                log.error("Error rolling back transaction", e1);
                throw e1;
            }
            throw e;
        }
    }

    public ArrayList<Category> listCategories() {
        var ps = statements.get("category_list");
        ArrayList<Category> categories = new ArrayList<>();
        try {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                categories.add(Category.of(rs.getObject(1, UUID.class), rs.getString(2)));
            }
        } catch (SQLException e) {
            log.error("Error getting category list", e);
        }

        return categories;
    }

    @Override
    public void close() throws Exception {
        if (updated) {
            con.commit();
        }
    }
}
