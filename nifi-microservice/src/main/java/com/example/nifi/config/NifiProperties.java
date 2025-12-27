package com.example.nifi.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "nifi")
public class NifiProperties {

    /**
     * Base URL for the NiFi REST API, e.g. http://localhost:8080/nifi-api
     */
    private String baseUrl;

    /**
     * Optional root process group id to use when creating new workflows.
     */
    private String rootProcessGroupId;

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getRootProcessGroupId() {
        return rootProcessGroupId;
    }

    public void setRootProcessGroupId(String rootProcessGroupId) {
        this.rootProcessGroupId = rootProcessGroupId;
    }
}
