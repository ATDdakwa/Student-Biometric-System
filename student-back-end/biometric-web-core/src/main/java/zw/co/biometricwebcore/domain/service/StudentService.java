package zw.co.biometricwebcore.domain.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import zw.co.biometricwebcore.domain.model.Student;
import zw.co.biometricwebcore.domain.repository.StudentRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    private final ModelMapper modelMapper;

    public List<Student> findAll() {
        return studentRepository.findAll();
    }

    public List<Student> findEnrolled() {
        return studentRepository.findByEnrolmentStatusIgnoreCase("ENROLLED");
    }

    public Page<Student> findAllPaginated(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return studentRepository.findAll(pageable);
    }

    public Student getById(Long id) {
        return studentRepository.findById(id).orElseThrow();
    }

    public Student create(Student student) {
        return studentRepository.save(student);
    }

    public Student update(Long id, Student updated) {
        Student existing = getById(id);
        existing.setFirstName(updated.getFirstName());
        existing.setSurname(updated.getSurname());
        existing.setDob(updated.getDob());
        existing.setNationality(updated.getNationality());
        existing.setInitials(updated.getInitials());
        existing.setIdNumber(updated.getIdNumber());
        existing.setGender(updated.getGender());
        existing.setStatus(updated.getStatus());
        existing.setEmail(updated.getEmail());
        existing.setFaculty(updated.getFaculty());
        existing.setProgramme(updated.getProgramme());
        existing.setIsBiometric(updated.getIsBiometric());
        existing.setEnrolmentStatus(updated.getEnrolmentStatus());
        existing.setBiometricTag(updated.getBiometricTag());
        return studentRepository.save(existing);
    }

    public void delete(Long id) {
        studentRepository.deleteById(id);
    }

    public Optional<Student> findByStudentNumber(String studentNumber) {
        return studentRepository.findByStudentNumber(studentNumber);
    }

    public Optional<Student> findByIdNumber(String idNumber) {
        return studentRepository.findByIdNumber(idNumber);
    }
}
