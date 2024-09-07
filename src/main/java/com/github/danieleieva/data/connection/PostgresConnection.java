package com.github.danieleieva.data.connection;


import com.github.danieleieva.data.DataAccess;
import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.Shutdown;
import io.quarkus.runtime.Startup;
import io.quarkus.runtime.annotations.StaticInitSafe;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@Startup
@Singleton
@StaticInitSafe
public class PostgresConnection {
    @Inject
    PostgresConfig pgConfig;

    private Connection con;
    private Map<String, PreparedStatement> statements;

    private static final Logger log = LoggerFactory.getLogger(PostgresConnection.class);

    @PostConstruct
    public void init() {
        try {
            log.info("Connecting to database...");
            con = DriverManager.getConnection(pgConfig.url(), pgConfig.user(), pgConfig.password());
            con.setAutoCommit(false);
            log.info("Successfully connected to database");
            statements = PostgresStatements.getStatements(con);
        }
        catch (SQLException e) {
            log.error("Failed to connect to database", e);
            Quarkus.asyncExit();
        }

    }

    public DataAccess getDAO() {
        return new DataAccess(con, statements);
    }

    @Shutdown
    void onStop() {
        try {
            con.close();
            log.info("Connection closed");
        } catch (SQLException e) {
            log.error("Failed to close database connection", e);
        }
    }
}
