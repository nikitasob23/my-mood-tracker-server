package com.niksob.config_service.service;

import com.niksob.config_service.MainContextTest;
import com.niksob.config_service.config.GitConfigLoaderTestConfig;
import com.niksob.config_service.service.git.laoder.GitConfigLoader;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = GitConfigLoaderTestConfig.class)
public class GitConfigLoaderTest extends MainContextTest {
    @Autowired
    private GitConfigLoader gitConfigLoader;

    @Test
    public void testLoading() {
        gitConfigLoader.load();
    }
}
