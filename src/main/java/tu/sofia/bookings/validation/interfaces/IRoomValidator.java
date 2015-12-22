package tu.sofia.bookings.validation.interfaces;

import tu.sofia.bookings.entity.Room;

/**
 * Interface for performing validations of rooms
 */
public interface IRoomValidator extends IValidator {

	/**
	 * Returns true if the room is valid
	 *
	 * @param room
	 * @return true if the room is valid
	 */
	boolean isValid(Room room);
}
