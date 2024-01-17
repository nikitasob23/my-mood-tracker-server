package com.niksob.layer_connector.di.module.util;

import com.niksob.layer_connector.util.date.DateUtil;
import com.niksob.layer_connector.util.date.DateUtilImpl;

public class DateUtilDIModule {
    public DateUtil provide() {
        return new DateUtilImpl();
    }
}
