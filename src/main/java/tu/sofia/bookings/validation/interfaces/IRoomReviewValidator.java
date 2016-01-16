package tu.sofia.bookings.validation.interfaces;

import tu.sofia.bookings.dao.RoomDao;
import tu.sofia.bookings.entity.RoomReview;

/**
 * Interface for performing validations of rooms
 */
public interface IRoomReviewValidator extends IValidator {

	/**
	 * Returns true if the room review is valid
	 *
	 * @param roomReview
	 * @param roomDao
	 * @return true if the room review is valid
	 */
	boolean isValid(RoomReview roomReview, RoomDao roomDao);
}
