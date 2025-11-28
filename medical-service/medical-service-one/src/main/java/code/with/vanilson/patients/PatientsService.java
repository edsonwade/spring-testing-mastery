package code.with.vanilson.patients;

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
        return patientsRepository.findAll().stream()
                .map(PatientResponse::fromEntity)
                .collect(toList());
    }

    public PatientResponse getPatientById(Long id) {
        return patientsRepository.findById(id)
                .map(PatientResponse::fromEntity)
                .orElseThrow();
    }

    public PatientResponse createPatient(PatientRequest patientRequest) {
        Patient patient = patientsRepository.save(patientRequest.toEntity());
        return fromEntity(patient);
    }
}
