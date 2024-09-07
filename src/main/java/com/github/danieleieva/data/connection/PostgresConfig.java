package com.github.danieleieva.data.connection;

import io.smallrye.config.ConfigMapping;

@ConfigMapping(prefix = "postgres")
public interface PostgresConfig {
    public String host();
    public String db();
    public String user();
    public String password();

    default String url() {return "jdbc:postgresql://" + host() + ":5432/" + db();}
}
