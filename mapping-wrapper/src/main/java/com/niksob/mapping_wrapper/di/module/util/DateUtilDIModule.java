package com.niksob.mapping_wrapper.di.module.util;

import com.niksob.mapping_wrapper.util.date.DateUtil;
import com.niksob.mapping_wrapper.util.date.DateUtilImpl;

public class DateUtilDIModule {
    public DateUtil provide() {
        return new DateUtilImpl();
    }
}
