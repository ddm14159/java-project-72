package hexlet.code.repository;

import com.zaxxer.hikari.HikariConfig;

import javax.sql.DataSource;

public class DataSourceConfig {

    public static DataSource getDataSource() {
        String jdbcUrl = System.getenv().getOrDefault("JDBC_DATABASE_URL", "jdbc:h2:mem:project");
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(jdbcUrl);

        return new com.zaxxer.hikari.HikariDataSource(config);
    }
}
