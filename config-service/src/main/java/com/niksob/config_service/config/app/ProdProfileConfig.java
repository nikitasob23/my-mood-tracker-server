package com.niksob.config_service.config.app;

import com.niksob.config_service.service.git.GitConfigService;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("prod")
@Configuration
@AllArgsConstructor
public class ProdProfileConfig {
    private final GitConfigService gitConfigService;

    @PostConstruct
    public void init() {
        gitConfigService.formYamlMicroserviceConfigs();
    }
}
