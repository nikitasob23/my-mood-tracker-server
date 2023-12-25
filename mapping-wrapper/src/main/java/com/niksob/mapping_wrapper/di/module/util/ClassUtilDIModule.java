package com.niksob.mapping_wrapper.di.module.util;

import com.niksob.mapping_wrapper.util.ClassUtil;
import com.niksob.mapping_wrapper.util.ClassUtilImpl;

public class ClassUtilDIModule {
    public ClassUtil provide() {
        return new ClassUtilImpl();
    }
}
