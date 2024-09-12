package hexlet.code.repository;

import com.zaxxer.hikari.HikariConfig;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;

@Slf4j
public class DataSourceConfig {

    public static DataSource getDataSource() {
        String jdbcUrl = System.getenv().getOrDefault("JDBC_DATABASE_URL", "jdbc:h2:mem:project;DB_CLOSE_DELAY=-1");
        log.info("test2");
        log.info(jdbcUrl);
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(jdbcUrl);

        return new com.zaxxer.hikari.HikariDataSource(config);
    }
}
