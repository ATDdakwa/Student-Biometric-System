package zw.co.biometricwebcore.api;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import zw.co.biometricwebcore.domain.model.AccessPoint;
import zw.co.biometricwebcore.domain.model.AccessPointAssignment;
import zw.co.biometricwebcore.domain.service.AccessPointAssignmentService;
import zw.co.biometricwebcore.domain.service.AccessPointService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/access-points")
public class AccessPointController {

    private final AccessPointService accessPointService;
    private final AccessPointAssignmentService assignmentService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<AccessPoint> getAll() {
        return accessPointService.findAll();
    }

    @GetMapping("/{id}")
    public AccessPoint getById(@PathVariable Long id) {
        return accessPointService.getById(id);
    }

    @PostMapping
    public AccessPoint create(@RequestBody AccessPoint ap) throws Exception {
        return accessPointService.create(ap);
    }

    @PutMapping("/{id}")
    public AccessPoint update(@PathVariable Long id, @RequestBody AccessPoint ap) {
        return accessPointService.update(id, ap);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        accessPointService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // Assignments management
    @PostMapping("/assign")
    public AccessPointAssignment assign(@RequestBody AssignRequest request) {
        return assignmentService.assign(request.getUsername(), request.getAccessPointCode());
    }

    @GetMapping("/assign/{username}")
    public ResponseEntity<AccessPointAssignment> getAssignment(@PathVariable String username) {
        return assignmentService.getByUsername(username)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/assign/{username}")
    public ResponseEntity<Void> unassign(@PathVariable String username) {
        assignmentService.unassign(username);
        return ResponseEntity.noContent().build();
    }

    @Data
    public static class AssignRequest {
        private String username;
        private String accessPointCode;
    }
}
