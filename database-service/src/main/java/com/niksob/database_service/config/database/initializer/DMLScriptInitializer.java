package com.niksob.database_service.config.database.initializer;

import com.niksob.logger.object_state.ObjectStateLogger;
import com.niksob.logger.object_state.factory.ObjectStateLoggerFactory;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

@Profile("dev")
@Component
public class DMLScriptInitializer {
    private final JdbcTemplate jdbcTemplate;
    private final TransactionTemplate transactionTemplate;

    private final ResourceLoader resourceLoader;

    @Value("${spring.datasource.database-init-resource-file}")
    private String sqlFileName;
    @Value("${spring.datasource.database-init-enable}")
    private boolean initEnable;

    private final ObjectStateLogger log = ObjectStateLoggerFactory.getLogger(DMLScriptInitializer.class);

    public DMLScriptInitializer(
            JdbcTemplate jdbcTemplate, ResourceLoader resourceLoader, PlatformTransactionManager transactionManager
    ) {
        this.jdbcTemplate = jdbcTemplate;
        this.resourceLoader = resourceLoader;
        this.transactionTemplate = new TransactionTemplate(transactionManager);
    }

    @PostConstruct
    public void init() {
        if (!initEnable) {
            return;
        }
        final String resourcePath = getResourcePath();
        if (resourcePath == null) {
            return;
        }
        final String sqlFilePath = resourcePath + File.separator + sqlFileName;
        final String[] sqlStrings = getSQLStrings(Paths.get(sqlFilePath));
        if (sqlStrings == null) {
            return;
        }
        transactionTemplate.execute(status -> {
            try {
                jdbcTemplate.batchUpdate(sqlStrings);
            } catch (Exception e) {
                status.setRollbackOnly();
                log.error("Additional database initialization commands were not executed before application started", e);
            }
            return null;
        });
    }

    private String[] getSQLStrings(Path filePath) {
        try (Stream<String> stream = Files.lines(filePath)) {
            return stream
                    .filter(s -> !s.isBlank())
                    .toArray(String[]::new);
        } catch (Exception e) {
            log.error("Additional database initialization commands were not executed before application started", e);
        }
        return null;
    }

    private String getResourcePath() {
        try {
            return resourceLoader.getResource("classpath:").getFile().getAbsolutePath();
        } catch (Exception e) {
            log.error("The path to the resource directory could not be obtained for loading SQL" +
                    " for additional data initialization", e);
        }
        return null;
    }
}
