package tu.sofia.bookings.validation.interfaces;

import tu.sofia.bookings.dao.RoomDao;
import tu.sofia.bookings.dao.RoomPriceDao;
import tu.sofia.bookings.entity.RoomPrice;

/**
 * Interface for performing validations of rooms
 */
public interface IRoomPriceValidator extends IValidator {

	/**
	 * Returns true if the room price is valid
	 *
	 * @param roomPrice
	 * @param roomDao
	 * @param roomPriceDao
	 * @return true if the room price is valid
	 */
	boolean isValid(RoomPrice roomPrice, RoomDao roomDao, RoomPriceDao roomPriceDao);
}
