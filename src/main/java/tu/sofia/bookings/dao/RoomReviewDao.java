package tu.sofia.bookings.dao;

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

	@Override
	protected Logger getLogger() {
		return logger;
	}
}
