package com.niksob.domain.http.rest.path;

import java.util.Map;

public interface RestPath {
    String get(String resourceUri, Map<String, String> params);

    String post(String resourceUri);
}
