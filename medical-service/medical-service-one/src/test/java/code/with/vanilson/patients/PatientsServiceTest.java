package code.with.vanilson.patients;

import code.with.vanilson.patients.exception.PatientNotFound;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

class PatientsServiceTest {

    private final PatientsRepository patientsRepo = mock(PatientsRepository.class);
    private final PatientsService patientsService = new PatientsService(patientsRepo);

    public List<Patient> patients;

    public Patient patient;

    @BeforeEach
    void setup() {
        patients = Collections.singletonList(Patient.builder()
                .id(1L)
                .firstName("firstName")
                .middleName("middleName")
                .lastName("lastName")
                .age(12)
                .email("test1@test.com")
                .build());

        patient = Patient.builder()
                .id(1L)
                .firstName("firstName")
                .middleName("middleName")
                .lastName("lastName")
                .age(12)
                .email("test1@test.com")
                .build();

    }

    @Test
    @DisplayName("should return all patients mapped to response DTOs")
    void shouldReturnAllPatients() {
        // given
        var expectedResponse = PatientResponse.fromEntity(patient);
        when(patientsRepo.findAllPatients()).thenReturn(patients);

        // when
        var actualResponse = patientsService.getAllPatients();

        // then
        assertEquals(1, actualResponse.size());
        assertEquals(expectedResponse.getId(), actualResponse.get(0).getId());
        assertEquals(expectedResponse.getFirstName(), actualResponse.get(0).getFirstName());
        assertEquals(expectedResponse.getAge(), actualResponse.get(0).getAge());

        assertThat(actualResponse.get(0).getId()).isPositive();
        assertThat(actualResponse.get(0).getFirstName()).isNotBlank();

        verify(patientsRepo, times(1)).findAllPatients();
    }

    @Test
    @DisplayName("should return empty list when repository has no patients")
    void givenNoPatients_whenGetAllPatients_thenReturnEmptyList() {
        // given
        given(patientsRepo.findAllPatients()).willReturn(List.of());

        // when
        var actual = patientsService.getAllPatients();

        // then
        assertThat(actual).isEmpty();
        verify(patientsRepo, times(1)).findAllPatients();
        verifyNoMoreInteractions(patientsRepo);
    }

    @Test
    @DisplayName("should return the patient when a valid patient id is provided")
    void givenPatientByIdShouldReturnPatient() {
        // given
        Long id = 1L;
        var expectedResponse = PatientResponse.fromEntity(patient);

        when(patientsRepo.findById(id)).thenReturn(Optional.of(patient));

        // when
        var actualResponse = patientsService.getPatientById(id);

        // then
        assertThat(actualResponse).isNotNull();

        assertThat(actualResponse)
                .extracting("id", "firstName", "lastName", "age")
                .containsExactly(
                        expectedResponse.getId(),
                        expectedResponse.getFirstName(),
                        expectedResponse.getLastName(),
                        expectedResponse.getAge()
                );

        verify(patientsRepo, times(1)).findById(id);
        verifyNoMoreInteractions(patientsRepo);
    }

    @Test
    @DisplayName("should throw PatientNotFound when patient id does not exist")
    void givenInvalidIdShouldThrowPatientNotFound() {
        // given
        Long id = 999L;
        when(patientsRepo.findById(id)).thenReturn(Optional.empty());

        // when / then
        assertThatThrownBy(() -> patientsService.getPatientById(id))
                .isInstanceOf(PatientNotFound.class)
                .hasMessageContaining("Patient with id " + id + " not found");

        verify(patientsRepo, times(1)).findById(id);
        verifyNoMoreInteractions(patientsRepo);
    }

    @Test
    @DisplayName("should create a patient and return the response")
    void givenValidRequest_whenCreatePatient_thenReturnCreatedPatientResponse() {
        // given
        PatientRequest request = new PatientRequest();
        request.setFirstName("Ana");
        request.setMiddleName("Maria");
        request.setLastName("Silva");
        request.setAge(30);
        request.setEmail("ana@example.com");

        // Entity produced by the request
        Patient entityFromRequest = request.toEntity();

        // Entity returned by the repo (with ID)
        Patient savedPatient = Patient.builder()
                .id(1L)
                .firstName("Ana")
                .middleName("Maria")
                .lastName("Silva")
                .age(30)
                .email("ana@example.com")
                .build();

        // Mock repository behavior
        when(patientsRepo.save(entityFromRequest)).thenReturn(savedPatient);

        PatientResponse expectedResponse = PatientResponse.fromEntity(savedPatient);

        // when
        PatientResponse actualResponse = patientsService.createPatient(request);

        // then
        assertThat(actualResponse)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(expectedResponse);

        verify(patientsRepo).save(entityFromRequest);
        verifyNoMoreInteractions(patientsRepo);
    }

}