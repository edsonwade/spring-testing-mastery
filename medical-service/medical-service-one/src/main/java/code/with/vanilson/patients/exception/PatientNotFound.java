/**
 * Author: vanilson muhongo
 * Date:2026-02-05
 * Time:17:05
 * Version:
 */

package code.with.vanilson.patients.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class PatientNotFound extends RuntimeException {
    public PatientNotFound(String message) {
        super(message);
    }
}
