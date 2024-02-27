package com.niksob.logger.config.properties;

import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;

import java.util.Objects;

public class YamlPropertySourcesPlaceholderConfigurerFactory {

    public static PropertySourcesPlaceholderConfigurer create(String configureFileName) {
        PropertySourcesPlaceholderConfigurer configurer = new PropertySourcesPlaceholderConfigurer();
        configurer.setIgnoreResourceNotFound(true);
        configurer.setIgnoreUnresolvablePlaceholders(true);
        configurer.setFileEncoding("UTF-8");
        configurer.setOrder(0);

        YamlPropertiesFactoryBean yaml = new YamlPropertiesFactoryBean();
        yaml.setResources(new ClassPathResource(configureFileName));

        configurer.setProperties(Objects.requireNonNull(yaml.getObject()));

        return configurer;
    }
}
