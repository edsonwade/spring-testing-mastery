package code.with.vanilson.service;

import code.with.vanilson.dto.PatientRequest;
import code.with.vanilson.dto.PatientResponse;
import code.with.vanilson.model.Patient;

import code.with.vanilson.repository.PatientsRepository;
import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;

import static code.with.vanilson.dto.PatientResponse.fromEntity;
import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
public class PatientsService {

    private PatientsRepository patientsRepository;

    public List<PatientResponse> getAllPatients() {
        return patientsRepository.findAllPatients().stream()
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

    public PatientResponse updatePatient(Long id, PatientRequest patientRequest) {
        return patientsRepository.findById(id)
                .map(patient -> patient.updatePatient(patientRequest))
                .map(patient -> patientsRepository.save(patient))
                .map(PatientResponse::fromEntity)
                .orElseThrow(() -> new IllegalArgumentException("Patient with id " + id + " not found"));
    }
}
