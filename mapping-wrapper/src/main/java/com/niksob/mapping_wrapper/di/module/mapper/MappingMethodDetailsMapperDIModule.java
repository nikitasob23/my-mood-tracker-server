package com.niksob.mapping_wrapper.di.module.mapper;

import com.niksob.mapping_wrapper.mapper.MappingMethodDetailsMapper;

public class MappingMethodDetailsMapperDIModule {
    public MappingMethodDetailsMapper provide() {
        return new MappingMethodDetailsMapper();
    }
}
