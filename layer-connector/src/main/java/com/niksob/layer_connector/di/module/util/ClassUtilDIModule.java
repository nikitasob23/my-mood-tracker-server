package com.niksob.layer_connector.di.module.util;

import com.niksob.layer_connector.util.clazz.ClassUtil;
import com.niksob.layer_connector.util.clazz.ClassUtilImpl;

public class ClassUtilDIModule {
    public ClassUtil provide() {
        return new ClassUtilImpl();
    }
}
