package com.example.nifi.controller;

import com.example.nifi.model.CreateProcessGroupRequest;
import com.example.nifi.service.NifiClient;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/nifi")
public class NifiController {

    private final NifiClient nifiClient;

    public NifiController(NifiClient nifiClient) {
        this.nifiClient = nifiClient;
    }

    @PostMapping("/process-groups/{processGroupId}/start")
    public ResponseEntity<Map<String, Object>> startProcessGroup(@PathVariable String processGroupId) {
        return ResponseEntity.ok(nifiClient.startProcessGroup(processGroupId));
    }

    @PostMapping("/process-groups/{processGroupId}/stop")
    public ResponseEntity<Map<String, Object>> stopProcessGroup(@PathVariable String processGroupId) {
        return ResponseEntity.ok(nifiClient.stopProcessGroup(processGroupId));
    }

    @GetMapping("/process-groups/{processGroupId}/queues")
    public ResponseEntity<Map<String, Object>> getQueueStatus(@PathVariable String processGroupId) {
        return ResponseEntity.ok(nifiClient.getProcessGroupQueueStatus(processGroupId));
    }

    @PostMapping("/process-groups")
    public ResponseEntity<Map<String, Object>> createProcessGroup(
        @RequestBody CreateProcessGroupRequest request
    ) {
        return ResponseEntity.ok(nifiClient.createProcessGroup(request));
    }
}
