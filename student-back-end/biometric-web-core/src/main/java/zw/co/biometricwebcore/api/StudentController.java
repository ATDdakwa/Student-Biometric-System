package zw.co.biometricwebcore.api;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import zw.co.biometricwebcore.domain.model.Student;
import zw.co.biometricwebcore.domain.service.StudentService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/students")
public class StudentController {

    private final StudentService studentService;

    @GetMapping(path = "/get-all", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get all students with pagination")
    public Page<Student> getAll(@RequestParam(defaultValue = "0") int page,
                                @RequestParam(defaultValue = "50") int size) {
        return studentService.findAllPaginated(page, size);
    }

    @GetMapping(path = "/get-all-students", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Student> getAllReports() {
        return studentService.findAll();
    }

    @GetMapping(path = "/enrolled", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Student> getEnrolled() {
        return studentService.findEnrolled();
    }

    @GetMapping("/{id}")
    public Student getById(@PathVariable Long id) {
        return studentService.getById(id);
    }

    @PostMapping("/create")
    public Student create(@RequestBody Student student) {
        return studentService.create(student);
    }

    @PutMapping("/{id}")
    public Student update(@PathVariable Long id, @RequestBody Student student) {
        return studentService.update(id, student);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        studentService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
