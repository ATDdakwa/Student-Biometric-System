package zw.co.firstmutual.userservice.controller.audit;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import zw.co.firstmutual.userservice.domain.dto.audit.ActivityDto;
import zw.co.firstmutual.userservice.domain.model.UserActivity;
import zw.co.firstmutual.userservice.service.ActivityService;
import zw.co.hcpwebcommons.api.ApiResponse;

@RestController
@CrossOrigin
@RequestMapping(value = "/api/v1/activity", produces = MediaType.APPLICATION_JSON_VALUE)
public class ActivityController {
    private final ActivityService activityService;
    private final ModelMapper modelMapper;

    @Autowired
    public ActivityController(ActivityService activityService, ModelMapper modelMapper) {
        this.activityService = activityService;
        this.modelMapper = modelMapper;
    }

    @PostMapping(value = "/log", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse addActivity(@RequestBody ActivityDto activityDto) {
        var activity = modelMapper.map(activityDto, UserActivity.class);
        return new ApiResponse(200, "SUCCESS", activityService.add(activity));
    }

    @GetMapping("/")
    public ApiResponse getAllActivities() {
        return new ApiResponse(200, "SUCCESS", activityService.getAll());
    }

    @GetMapping("/{id}")
    public ApiResponse getById(@PathVariable("id") Long id) {
        return new ApiResponse(200, "SUCCESS", activityService.getById(id));
    }

    @GetMapping("/{entityId}")
    public ApiResponse getAllActivitiesPerformedOnEntity(@PathVariable("entityId") Long entityId) {
        return new ApiResponse(200, "SUCCESS", activityService.getAllByEntityId(entityId));
    }
}