package zw.co.firstmutual.userservice.controller.audit;

import io.swagger.annotations.ApiOperation;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import zw.co.firstmutual.userservice.domain.dto.audit.AuditTrailDto;
import zw.co.firstmutual.userservice.domain.model.AuditTrail;
import zw.co.firstmutual.userservice.service.AuditTrailService;
import zw.co.hcpwebcommons.api.ApiResponse;

@RestController
@CrossOrigin
@RequestMapping(value = "/api/v1/auditTrail", produces = MediaType.APPLICATION_JSON_VALUE)
//@Api(value = "/api/v1/auditTrail", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuditTrailController {

    private final AuditTrailService auditTrailService;
    private final ModelMapper modelMapper;

    @Autowired
    public AuditTrailController(AuditTrailService auditTrailService, ModelMapper modelMapper) {
        this.auditTrailService = auditTrailService;
        this.modelMapper = modelMapper;
    }

    @PostMapping(value = "/add", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Adding a audit trail entity", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse addAuditTrail(@RequestBody AuditTrailDto auditTrailDto) {
        var auditTrail = modelMapper.map(auditTrailDto, AuditTrail.class);

        return new ApiResponse(200, "SUCCESS", auditTrailService.add(auditTrail));
    }

    @GetMapping("/")
    @ApiOperation(value = "Get all audit trails", response = ApiResponse.class)
    public ApiResponse getAllTrails() {
        return new ApiResponse(200, "SUCCESS", auditTrailService.getAll());
    }

    @GetMapping("/{userId}")
    @ApiOperation(value = "Get audit trail by user ID ", response = ApiResponse.class)
    public ApiResponse getByUsername(@PathVariable("userId") Long userId) {
        return new ApiResponse(200, "SUCCESS", auditTrailService.getByUserId(userId));
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Get one audit trail by its ID", response = ApiResponse.class)
    public ApiResponse getOneTrail(@RequestParam("id") Long id) {
        return new ApiResponse(200, "SUCCESS", auditTrailService.getById(id));
    }
}