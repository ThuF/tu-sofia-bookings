package tu.sofia.bookings.validation;

import java.text.MessageFormat;

import com.google.inject.Singleton;

import tu.sofia.bookings.dao.RoomDao;
import tu.sofia.bookings.entity.RoomReview;
import tu.sofia.bookings.validation.interfaces.IRoomReviewValidator;

/**
 * Class for validating {@link RoomReview}
 */
@Singleton
public class RoomReviewValidator implements IRoomReviewValidator {

	private static final String VALIDATION_MESSAGE_THE_ROOM_REVIEW_CAN_T_BE_NULL = "The room review can't be null";
	private static final String VALIDATION_MESSAGE_THE_ROOM_ID_PROPERTY_CAN_T_BE_NULL = "The [roomId] property can't be null";
	private static final String VALIDATION_MESSAGE_THE_COMMENT_PROPERTY_CAN_T_BE_NULL = "The [comment] property can't be null";
	private static final String VALIDATION_MESSAGE_THERE_IS_NO_ROOM_WITH_ROOM_ID = "There is no room with [roomId={0}]";

	private String validationMessage;

	@Override
	public String getValidationMessage() {
		return validationMessage;
	}

	@Override
	public boolean isValid(RoomReview roomReview, RoomDao roomDao) {
		boolean isValid = false;
		if (roomReview == null) {
			validationMessage = VALIDATION_MESSAGE_THE_ROOM_REVIEW_CAN_T_BE_NULL;
		} else if (roomReview.getRoomId() == null) {
			validationMessage = VALIDATION_MESSAGE_THE_ROOM_ID_PROPERTY_CAN_T_BE_NULL;
		} else if (roomReview.getComment() == null) {
			validationMessage = VALIDATION_MESSAGE_THE_COMMENT_PROPERTY_CAN_T_BE_NULL;
		} else if (roomDao.findById(roomReview.getRoomId()) == null) {
			validationMessage = MessageFormat.format(VALIDATION_MESSAGE_THERE_IS_NO_ROOM_WITH_ROOM_ID, roomReview.getRoomId());
		} else {
			isValid = true;
		}
		return isValid;
	}
}
