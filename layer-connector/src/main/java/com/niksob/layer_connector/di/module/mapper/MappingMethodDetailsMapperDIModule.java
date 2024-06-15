package com.niksob.layer_connector.di.module.mapper;

import com.niksob.layer_connector.mapper.MappingMethodDetailsMapper;

public class MappingMethodDetailsMapperDIModule {
    public MappingMethodDetailsMapper provide() {
        return new MappingMethodDetailsMapper();
    }
}
