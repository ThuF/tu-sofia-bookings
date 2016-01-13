package tu.sofia.bookings.validation.interfaces;

import tu.sofia.bookings.dao.RoomPriceDao;
import tu.sofia.bookings.entity.RoomPrice;
import tu.sofia.bookings.service.RoomService;

/**
 * Interface for performing validations of rooms
 */
public interface IRoomPriceValidator extends IValidator {

	/**
	 * Returns true if the room price is valid
	 *
	 * @param roomPrice
	 * @param roomPriceDao
	 * @param roomService
	 * @return true if the room price is valid
	 */
	boolean isValid(RoomPrice roomPrice, RoomPriceDao roomPriceDao, RoomService roomService);
}
