package com.example.nifi.service;

import com.example.nifi.config.NifiProperties;
import com.example.nifi.model.CreateProcessGroupRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

@Service
public class NifiClient {

    private final RestTemplate restTemplate;
    private final NifiProperties properties;

    public NifiClient(RestTemplate restTemplate, NifiProperties properties) {
        this.restTemplate = restTemplate;
        this.properties = properties;
    }

    public Map<String, Object> startProcessGroup(String processGroupId) {
        return updateProcessGroupState(processGroupId, "RUNNING");
    }

    public Map<String, Object> stopProcessGroup(String processGroupId) {
        return updateProcessGroupState(processGroupId, "STOPPED");
    }

    public Map<String, Object> getProcessGroupQueueStatus(String processGroupId) {
        String url = String.format("%s/flow/process-groups/%s/status", properties.getBaseUrl(), processGroupId);
        ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
        return Objects.requireNonNull(response.getBody());
    }

    public Map<String, Object> createProcessGroup(CreateProcessGroupRequest request) {
        String parentId = resolveRootProcessGroupId();
        String url = String.format("%s/process-groups/%s/process-groups", properties.getBaseUrl(), parentId);

        Map<String, Object> payload = new HashMap<>();
        payload.put("revision", Map.of("version", 0));
        payload.put("component", Map.of(
            "name", request.getName(),
            "position", Map.of(
                "x", request.getPositionX() == null ? 0.0 : request.getPositionX(),
                "y", request.getPositionY() == null ? 0.0 : request.getPositionY()
            )
        ));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(payload, headers);
        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, entity, Map.class);
        return Objects.requireNonNull(response.getBody());
    }

    private Map<String, Object> updateProcessGroupState(String processGroupId, String state) {
        String url = String.format("%s/flow/process-groups/%s", properties.getBaseUrl(), processGroupId);

        Map<String, Object> payload = new HashMap<>();
        payload.put("id", processGroupId);
        payload.put("state", state);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(payload, headers);
        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.PUT, entity, Map.class);
        return Objects.requireNonNull(response.getBody());
    }

    private String resolveRootProcessGroupId() {
        if (StringUtils.hasText(properties.getRootProcessGroupId())) {
            return properties.getRootProcessGroupId();
        }
        String url = String.format("%s/flow/process-groups/root", properties.getBaseUrl());
        ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
        Map<String, Object> body = Objects.requireNonNull(response.getBody());
        Map<String, Object> processGroupFlow = (Map<String, Object>) body.get("processGroupFlow");
        Map<String, Object> flow = (Map<String, Object>) processGroupFlow.get("flow");
        return (String) flow.get("id");
    }
}
