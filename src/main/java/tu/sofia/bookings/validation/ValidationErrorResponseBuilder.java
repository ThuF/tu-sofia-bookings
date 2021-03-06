package tu.sofia.bookings.validation;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import tu.sofia.bookings.validation.interfaces.IValidator;

/**
 * Builds validation error response
 */
public class ValidationErrorResponseBuilder {

	/**
	 * Returns response
	 *
	 * @param status
	 * @param errorMessage
	 * @return response
	 */
	public static Response toResponse(Status status, String errorMessage) {
		return Response.status(status).entity(new ApplicationExceptionMessage(status, errorMessage)).build();
	}

	/**
	 * Returns HTTP 400 Bad Request response
	 * 
	 * @param validator
	 * @return HTTP 400 Bad Request response
	 */
	public static Response toResponse(IValidator validator) {
		return toResponse(Status.BAD_REQUEST, validator.getValidationMessage());
	}
}
