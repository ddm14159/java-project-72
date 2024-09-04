package hexlet.code.Repository;

import com.zaxxer.hikari.HikariConfig;

import javax.sql.DataSource;

public class DataSourceConfig {

    public static DataSource getDataSource() {
        String jdbcUrl = System.getenv("JDBC_DATABASE_URL");

        if (jdbcUrl == null || jdbcUrl.isEmpty()) {
            throw new IllegalArgumentException("Environment variable JDBC_DATABASE_URL is not set");
        }

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(jdbcUrl);

        return new com.zaxxer.hikari.HikariDataSource(config);
    }
}
