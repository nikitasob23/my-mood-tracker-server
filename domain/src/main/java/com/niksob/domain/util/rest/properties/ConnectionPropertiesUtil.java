package com.niksob.domain.util.rest.properties;

import com.niksob.domain.exception.properties.LoadingPropertiesException;
import com.niksob.domain.model.connection.properties.RestConnectionProperties;
import com.niksob.logger.object_state.ObjectStateLogger;
import com.niksob.logger.object_state.factory.ObjectStateLoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Properties;

@Component
public class ConnectionPropertiesUtil {
    private final ObjectStateLogger log = ObjectStateLoggerFactory.getLogger(ConnectionPropertiesUtil.class);

    public RestConnectionProperties getProperties(String configFileName) {
        try {
            final Resource resource = new ClassPathResource("database-service.yml");
            final Properties props = PropertiesLoaderUtils.loadProperties(resource);

            return new RestConnectionProperties(
                    props.getProperty("server.protocol"),
                    props.getProperty("server.hostname"),
                    props.getProperty("server.port"),
                    props.getProperty("server.servlet.context-path")
            );
        } catch (Exception e) {
            log.error("Loading connection properties failed", e, configFileName);
            throw new LoadingPropertiesException("Rest properties didn't load from config file", e);
        }
    }
}
