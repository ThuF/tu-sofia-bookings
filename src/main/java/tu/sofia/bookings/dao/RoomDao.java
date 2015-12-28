package tu.sofia.bookings.dao;

import java.util.Collection;
import java.util.List;

import javax.persistence.TypedQuery;

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

	/**
	 * Returns all rooms except the chose ones
	 *
	 * @param bookedRooms
	 * @return all rooms except the chose ones
	 */
	public List<Room> findAllExcept(Collection<Long> bookedRooms) {
		List<Room> results = null;
		if (bookedRooms.isEmpty()) {
			results = findAll();
		} else {
			TypedQuery<Room> query = getEntityManager().createNamedQuery(Room.QUERY_NAME_FIND_ALL_EXCEPT, Room.class);
			query.setParameter(Room.PARAM_ROOM_IDS, bookedRooms);
			results = query.getResultList();
		}
		return results;
	}

	@Override
	protected Logger getLogger() {
		return logger;
	}
}
