package tu.sofia.bookings.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tu.sofia.bookings.entity.Room;

/**
 * DAO class for {@link Room}
 */
public class RoomDao extends AbstractJpaDao<Long, Room> {

	private static final Logger logger = LoggerFactory.getLogger(RoomDao.class);

	protected RoomDao() {
		super(Room.class);
	}

	@Override
	protected Logger getLogger() {
		return logger;
	}

}
