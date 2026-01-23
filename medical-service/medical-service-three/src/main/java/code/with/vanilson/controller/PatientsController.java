package code.with.vanilson.controller;

import code.with.vanilson.dto.PatientRequest;
import code.with.vanilson.dto.PatientResponse;
import code.with.vanilson.service.PatientsService;
import lombok.AllArgsConstructor;

import org.springframework.validation.annotation.Validated;
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
    public PatientResponse createPatient(@Validated
                                         @RequestBody PatientRequest patientRequest) {
        return patientsService.createPatient(patientRequest);
    }

    @PutMapping("/{id}")
    public PatientResponse updateResponse(@PathVariable Long id,
                                          @Validated @RequestBody PatientRequest patientRequest) {
        return patientsService.updatePatient(id, patientRequest);
    }
}
