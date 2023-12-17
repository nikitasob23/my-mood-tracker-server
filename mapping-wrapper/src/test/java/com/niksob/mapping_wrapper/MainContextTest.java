package com.niksob.mapping_wrapper;

import com.niksob.mapping_wrapper.config.MainMappingWrapperConfig;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {MainMappingWrapperConfig.class})
public class MainContextTest {
}
