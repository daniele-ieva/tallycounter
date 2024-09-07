package com.github.danieleieva.data.connection;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public abstract class PostgresStatements {
    private static final String category_create = "INSERT INTO categories (id, category) VALUES (?, ?);";
    private static final String category_delete = "DELETE FROM categories WHERE category = ?;";
    private static final String category_rename = "UPDATE categories SET category = ? WHERE category = ?;";
    private static final String category_list = "SELECT * FROM categories;";
    private static final String category_getId = "SELECT id FROM categories WHERE category = ? LIMIT 1;";

    private static final String tally_create = "INSERT INTO tally (name, category, tally) VALUES (?, ?, ?);";
    private static final String tally_delete = "DELETE FROM tally WHERE name = ?;";
    private static final String tally_rename = "UPDATE tally SET name = ? WHERE name = ?;";
    private static final String tally_list = "SELECT * FROM tally;";
    private static final String tally_filter = "SELECT * FROM tally WHERE category = ?;";

    private static final Logger log = LoggerFactory.getLogger(PostgresStatements.class);

    private static final Map<String, String> statementMap = new HashMap<>() {{
        put("category_create", category_create);
        put("category_delete", category_delete);
        put("category_rename", category_rename);
        put("category_list", category_list);
        put("category_getId", category_getId);

        put("tally_create", tally_create);
        put("tally_delete", tally_delete);
        put("tally_rename", tally_rename);
        put("tally_list", tally_list);
        put("tally_filter", tally_filter);
    }};

    public static Map<String, PreparedStatement> getStatements(Connection connection) {
        Map<String, PreparedStatement> statements = new HashMap<>();
        try {
            log.info("Preparing statements");
            for (String key : statementMap.keySet()) {
                log.debug("Preparing statement {}", key);
                statements.put(key, connection.prepareStatement(statementMap.get(key)));
            }
            log.info("Prepared statements");
        } catch (SQLException e) {
            log.error("could not prepare statements", e);
            Quarkus.asyncExit();
        }
        return statements;
    }
}
