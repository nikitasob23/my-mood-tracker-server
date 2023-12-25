package com.niksob.mapping_wrapper.util;

import com.niksob.mapping_wrapper.model.method_details.MethodSignature;

public interface ClassUtil {
    boolean compareParams(MethodSignature method1, MethodSignature method2);

    boolean compareReturnTypes(MethodSignature method1, MethodSignature method2);

    boolean haveParam(MethodSignature method);

    boolean returnVoid(MethodSignature method);

    String getShortClassName(String paramType);

    String stickNames(String name1, String name2);

    String getPackageName(String name);
}
