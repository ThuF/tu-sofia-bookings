package tu.sofia.bookings.validation.interfaces;

import tu.sofia.bookings.entity.Booking;

/**
 * Interface for performing validations of bookings
 */
public interface IBookingValidator extends IValidator {

	/**
	 * Returns true if the booking is valid
	 *
	 * @param booking
	 * @return true if the booking is valid
	 */
	boolean isValid(Booking booking);
}
