package tu.sofia.bookings.validation;

import com.google.inject.Singleton;

import tu.sofia.bookings.entity.Booking;
import tu.sofia.bookings.entity.additional.BookingStatus;
import tu.sofia.bookings.entity.additional.PaymentStatus;
import tu.sofia.bookings.validation.interfaces.IBookingValidator;

/**
 * Class for validating {@link Booking}
 */
@Singleton
public class BookValidator implements IBookingValidator {

	private static final String VALIDATION_MESSAGE_THE_BOOKING_CAN_T_BE_NULL = "The booking can't be null";
	private static final String VALIDATION_MESSAGE_THE_START_DATE_PROPERTY_CAN_T_BE_NULL = "The [startDate] property can't be null";
	private static final String VALIDATION_MESSAGE_THE_END_DATE_PROPERTY_CAN_T_BE_NULL = "The [endDate] property can't be null";
	private static final String VALIDATION_MESSAGE_THE_INITIAL_VALUE_OF_THE_BOOKING_STATUS_PROPERTY_CAN_T_BE_DIFFERENT_FROM_PROCESSING = "The initial value of the [bookingStatus] property can't be different from [PROCESSING]";
	private static final String VALIDATION_MESSAGE_THE_INITIAL_VALUE_OF_THE_PAYMENT_STATUS_PROPERTY_CAN_T_BE_DIFFERENT_FROM_UNPAID = "The initial value of the [paymentStatus] property can't be different from [UNPAID]";
	private static final String VALIDATION_MESSAGE_NO_ROOMS_ARE_PROVIDED_FOR_THE_BOOKING = "No rooms are provided for the booking";
	private static final String VALIDATION_MESSAGE_THE_USER_PROPERTY_SHOULD_NOT_BE_PROVIDED_BY_THE_USER = "The [user] property should not be provided by the user";

	private String validationMessage;

	@Override
	public String getValidationMessage() {
		return validationMessage;
	}

	@Override
	public boolean isValid(Booking booking) {
		boolean isValid = false;
		if (booking == null) {
			validationMessage = VALIDATION_MESSAGE_THE_BOOKING_CAN_T_BE_NULL;
		} else if (booking.getStartDate() == null) {
			validationMessage = VALIDATION_MESSAGE_THE_START_DATE_PROPERTY_CAN_T_BE_NULL;
		} else if (booking.getEndDate() == null) {
			validationMessage = VALIDATION_MESSAGE_THE_END_DATE_PROPERTY_CAN_T_BE_NULL;
		} else if (booking.getBookingStatus() != BookingStatus.PROCESSING) {
			validationMessage = VALIDATION_MESSAGE_THE_INITIAL_VALUE_OF_THE_BOOKING_STATUS_PROPERTY_CAN_T_BE_DIFFERENT_FROM_PROCESSING;
		} else if (booking.getPaymentStatus() != PaymentStatus.UNPAID) {
			validationMessage = VALIDATION_MESSAGE_THE_INITIAL_VALUE_OF_THE_PAYMENT_STATUS_PROPERTY_CAN_T_BE_DIFFERENT_FROM_UNPAID;
		} else if (booking.getRoomsId().isEmpty()) {
			validationMessage = VALIDATION_MESSAGE_NO_ROOMS_ARE_PROVIDED_FOR_THE_BOOKING;
		} else if (booking.getUser() != null) {
			validationMessage = VALIDATION_MESSAGE_THE_USER_PROPERTY_SHOULD_NOT_BE_PROVIDED_BY_THE_USER;
		} else {
			isValid = true;
		}
		return isValid;
	}

}
