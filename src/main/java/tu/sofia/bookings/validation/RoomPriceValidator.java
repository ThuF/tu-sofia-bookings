package tu.sofia.bookings.validation;

import java.text.MessageFormat;

import com.google.inject.Singleton;

import tu.sofia.bookings.dao.RoomPriceDao;
import tu.sofia.bookings.entity.RoomPrice;
import tu.sofia.bookings.service.RoomService;
import tu.sofia.bookings.validation.interfaces.IRoomPriceValidator;

/**
 * Class for validating {@link RoomPrice}
 */
@Singleton
public class RoomPriceValidator implements IRoomPriceValidator {

	private static final String VALIDATION_MESSAGE_THE_ROOM_PRICE_CAN_T_BE_NULL = "The room price can't be null";
	private static final String VALIDATION_MESSAGE_THE_ROOM_ID_PROPERTY_CAN_T_BE_NULL = "The [roomId] property can't be null";
	private static final String VALIDATION_MESSAGE_THE_PRICE_PROPERTY_CAN_T_BE_NULL = "The [price] property can't be null";
	private static final String VALIDATION_MESSAGE_THERE_IS_NO_ROOM_WITH_ROOM_ID = "There is no room with [roomId={0}]";
	private static final String VALIDATION_MESSAGE_THERE_IS_ALREADY_A_PRICE_FOR_THIS_ROOM = "There is already a price for this room";

	private String validationMessage;

	@Override
	public String getValidationMessage() {
		return validationMessage;
	}

	@Override
	public boolean isValid(RoomPrice roomPrice, RoomPriceDao roomPriceDao, RoomService roomService) {
		boolean isValid = false;
		if (roomPrice == null) {
			validationMessage = VALIDATION_MESSAGE_THE_ROOM_PRICE_CAN_T_BE_NULL;
		} else if (roomPrice.getRoomId() == null) {
			validationMessage = VALIDATION_MESSAGE_THE_ROOM_ID_PROPERTY_CAN_T_BE_NULL;
		} else if (roomPrice.getPrice() == null) {
			validationMessage = VALIDATION_MESSAGE_THE_PRICE_PROPERTY_CAN_T_BE_NULL;
		} else if (roomService.getRoomEntity(roomPrice.getRoomId()) == null) {
			validationMessage = MessageFormat.format(VALIDATION_MESSAGE_THERE_IS_NO_ROOM_WITH_ROOM_ID, roomPrice.getRoomId());
		} else if (roomPriceDao.findById(roomPrice.getRoomId()) != null) {
			validationMessage = VALIDATION_MESSAGE_THERE_IS_ALREADY_A_PRICE_FOR_THIS_ROOM;
		} else {
			isValid = true;
		}
		return isValid;
	}
}
