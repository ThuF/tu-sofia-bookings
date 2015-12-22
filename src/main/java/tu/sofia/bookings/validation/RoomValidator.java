package tu.sofia.bookings.validation;

import com.google.inject.Singleton;

import tu.sofia.bookings.entity.Room;

/**
 * Class for validating {@link Room}
 */
@Singleton
public class RoomValidator implements IRoomValidator {

	private static final String VALIDATION_MESSAGE_THE_ROOM_CAN_T_BE_NULL = "The room can't be null";
	private static final String VALIDATION_MESSAGE_THE_ROOM_TYPE_PROPERTY_CAN_T_BE_NULL = "The [roomType] property can't be null";
	private static final String VALIDATION_MESSAGE_THE_ROOM_VIEW_PROPERTY_CAN_T_BE_NULL = "The [roomView] property can't be null";
	private static final String VALIDATION_MESSAGE_THE_BED_TYPE_PROPERTY_CAN_T_BE_NULL = "The [bedType] property can't be null";

	private String validationMessage;

	@Override
	public String getValidationMessage() {
		return validationMessage;
	}

	@Override
	public boolean isValid(Room room) {
		boolean isValid = false;
		if (room == null) {
			validationMessage = VALIDATION_MESSAGE_THE_ROOM_CAN_T_BE_NULL;
		} else if (room.getRoomType() == null) {
			validationMessage = VALIDATION_MESSAGE_THE_ROOM_TYPE_PROPERTY_CAN_T_BE_NULL;
		} else if (room.getRoomView() == null) {
			validationMessage = VALIDATION_MESSAGE_THE_ROOM_VIEW_PROPERTY_CAN_T_BE_NULL;
		} else if (room.getBedType() == null) {
			validationMessage = VALIDATION_MESSAGE_THE_BED_TYPE_PROPERTY_CAN_T_BE_NULL;
		} else {
			isValid = true;
		}
		return isValid;
	}

}
