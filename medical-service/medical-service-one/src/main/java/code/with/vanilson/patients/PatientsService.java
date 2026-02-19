package code.with.vanilson.patients;

import code.with.vanilson.patients.exception.PatientNotFound;
import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;

import static code.with.vanilson.patients.PatientResponse.fromEntity;
import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
public class PatientsService {

    private PatientsRepository patientsRepository;

    public List<PatientResponse> getAllPatients() {
        return patientsRepository
                .findAllPatients()
                .stream()
                .map(PatientResponse::fromEntity)
                .collect(toList());
    }
    public PatientResponse getPatientById(Long id) {
        return patientsRepository.findById(id)
                .map(PatientResponse::fromEntity)
                .orElseThrow(() -> new PatientNotFound("Patient with id " + id + " not found"));
    }
    public PatientResponse createPatient(PatientRequest patientRequest) {
        Patient patient = patientsRepository.save(patientRequest.toEntity());
        return fromEntity(patient);
    }
}
