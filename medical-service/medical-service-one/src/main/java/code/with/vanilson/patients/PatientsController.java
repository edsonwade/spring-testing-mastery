package code.with.vanilson.patients;

import lombok.AllArgsConstructor;
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
        return patientsService.createPatient(patientRequest);
    }
}
