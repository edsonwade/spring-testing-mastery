package code.with.vanilson.patients;

import code.with.vanilson.patients.exception.InvalidEmailException;
import lombok.AllArgsConstructor;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/patients")
@AllArgsConstructor
public class PatientsController {

    private PatientsService patientsService;

    @GetMapping
    public List<PatientResponse> getAllPatients() {
        return patientsService.getAllPatients();
    }

    @GetMapping("/{id}")
    public List<PatientResponse> getOnePatient(@RequestParam("id") Long id) {
        return patientsService.getAllPatients();
    }

    @PostMapping
    public PatientResponse createPatient(
            @RequestBody PatientRequest patientRequest) {
        validatePatientFirstName(patientRequest);
        return patientsService.createPatient(patientRequest);
    }

    private void validatePatientFirstName(PatientRequest patientRequest) {
        if (patientRequest.getFirstName().isBlank()) {
            throw new IllegalArgumentException("First name not provided");
        }
        if (patientRequest.getAge() < 0 || patientRequest.getAge() > 150) {
            throw new IllegalArgumentException("Age is incorrect");
        }
        if (!EmailValidator.getInstance()
                .isValid(patientRequest.getEmail())) {
            throw new InvalidEmailException(patientRequest.getEmail());
        }
    }
}
