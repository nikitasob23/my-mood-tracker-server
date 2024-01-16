package com.niksob.mapping_wrapper.di.module.util;

import com.niksob.mapping_wrapper.util.clazz.ClassUtil;
import com.niksob.mapping_wrapper.util.clazz.ClassUtilImpl;

public class ClassUtilDIModule {
    public ClassUtil provide() {
        return new ClassUtilImpl();
    }
}
