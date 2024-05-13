package com.niksob.config_service.service;

import com.niksob.config_service.MainContextTest;
import com.niksob.config_service.config.YamlPropertiesMergerTestConfig;
import com.niksob.config_service.service.yaml.merger.YamlPropertiesMerger;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = YamlPropertiesMergerTestConfig.class)
public class YamlPropertiesMergerTest extends MainContextTest {
    @Autowired
    private YamlPropertiesMerger yamlPropertiesMerger;

    @Test
    public void testMerging() {
        yamlPropertiesMerger.merge();
    }
}
