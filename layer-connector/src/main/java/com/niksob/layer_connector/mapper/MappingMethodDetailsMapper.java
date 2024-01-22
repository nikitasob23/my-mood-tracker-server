package com.niksob.layer_connector.mapper;

import com.niksob.layer_connector.model.class_details.ClassDetails;
import com.niksob.layer_connector.model.method_details.MappingMethodDetails;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
@NoArgsConstructor
public class MappingMethodDetailsMapper {
    public Set<MappingMethodDetails> map(ClassDetails classDetails, String variableName) {
        return classDetails.getMethods().stream()
                .map(method -> new MappingMethodDetails(
                        classDetails.getName(),
                        variableName,
                        method.getMethodName(),
                        method.getReturnType(),
                        method.getParamType()
                )).collect(Collectors.toSet());
    }
}
