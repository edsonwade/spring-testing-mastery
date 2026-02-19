package code.with.vanilson.patients;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PatientsRepository extends CrudRepository<Patient, Long> {

    List<Patient> findAllPatients();
}
