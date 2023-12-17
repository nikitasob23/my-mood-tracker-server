package com.niksob.mapping_wrapper.model.mapping_wrapper;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MappingWrapperNameDetails {
    public static final String MAPPING_WRAPPER_POSTFIX = "MappingWrapper";
    private final String interfaceName;
    private final String packageInterfaceName;

    public String getFullInterfaceName() {
        return packageInterfaceName + "." + interfaceName;
    }

    public String getImplementationName() {
        return interfaceName + MAPPING_WRAPPER_POSTFIX;
    }

    public String getImplementationFullName() {
        return packageInterfaceName + "." + getImplementationName();
    }
}
