package tu.sofia.bookings.dao;

import java.util.List;

import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tu.sofia.bookings.entity.RoomReview;
import tu.sofia.bookings.entity.key.RoomReviewKey;

/**
 * DAO class for {@link RoomReview}
 */
public class RoomReviewDao extends AbstractJpaDao<RoomReviewKey, RoomReview> {

	private static final Logger logger = LoggerFactory.getLogger(RoomReviewDao.class);

	protected RoomReviewDao() {
		super(RoomReview.class);
	}

	/**
	 * Returns a list of all reviews filtered by room Id
	 *
	 * @param roomId
	 * @return a list of all reviews filtered by room Id
	 */
	public List<RoomReview> findByRoomId(Long roomId) {
		TypedQuery<RoomReview> query = getEntityManager().createNamedQuery(RoomReview.QUERY_NAME_FIND_BY_ROOM_ID, RoomReview.class);
		query.setParameter(RoomReview.PARAM_ROOM_ID, roomId);
		return query.getResultList();
	}

	@Override
	protected Logger getLogger() {
		return logger;
	}

}
