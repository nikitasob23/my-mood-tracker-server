package com.niksob.layer_connector.util.clazz;

import com.niksob.layer_connector.model.method_details.MethodSignature;

public interface ClassUtil {
    boolean compareParams(MethodSignature method1, MethodSignature method2);

    boolean compareReturnTypes(MethodSignature method1, MethodSignature method2);

    boolean haveParam(MethodSignature method);

    boolean returnVoid(MethodSignature method);

    String getShortClassName(String paramType);

    String stickNames(String name1, String name2);

    String getPackageName(String name);

    String getVariableName(String fullClassName);
}
