package zw.co.biometricwebcore.api;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import zw.co.biometricwebcore.domain.model.StudentAccessEvent;
import zw.co.biometricwebcore.domain.service.TrackingService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/tracking")
public class TrackingController {

    private final TrackingService trackingService;

    @PostMapping(path = "/verify", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public StudentAccessEvent verify(@RequestBody VerifyRequest request) {
        return trackingService.verifyAndRecord(request.getStudentNumber(), request.getAccessPointCode(), request.getDirection(), request.getBiometricTag(), request.getVerifierUsername());
    }

    @GetMapping("/student/{studentId}")
    public List<StudentAccessEvent> history(@PathVariable Long studentId) {
        return trackingService.getStudentHistory(studentId);
    }

    @GetMapping("/student/{studentId}/between")
    public List<StudentAccessEvent> historyBetween(@PathVariable Long studentId,
                                                   @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
                                                   @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to) {
        return trackingService.getStudentHistoryBetween(studentId, from, to);
    }

    @GetMapping("/events")
    public List<StudentAccessEvent> eventsBetween(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
                                                  @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to) {
        return trackingService.getEventsBetween(from, to);
    }

    @Data
    public static class VerifyRequest {
        private String studentNumber;
        private String accessPointCode;
        private String direction; // ENTRY or EXIT
        private String biometricTag; // incoming tag to match
        private String verifierUsername; // optional: if present (role USER), server derives access point from assignment
    }
}
