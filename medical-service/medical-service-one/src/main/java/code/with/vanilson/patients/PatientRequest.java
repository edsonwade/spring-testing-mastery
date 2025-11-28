package code.with.vanilson.patients;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class PatientRequest {

    private String firstName;
    private String middleName;
    private String lastName;
    private Integer age;
    private String email;

    public Patient toEntity() {
        return Patient.builder()
                .firstName(firstName)
                .middleName(middleName)
                .lastName(lastName)
                .age(age)
                .email(email)
                .build();
    }
}
