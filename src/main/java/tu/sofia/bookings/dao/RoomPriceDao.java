package tu.sofia.bookings.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tu.sofia.bookings.entity.RoomPrice;

/**
 * DAO class for {@link RoomPrice}
 */
public class RoomPriceDao extends AbstractJpaDao<Long, RoomPrice> {

	private static final Logger logger = LoggerFactory.getLogger(RoomPriceDao.class);

	protected RoomPriceDao() {
		super(RoomPrice.class);
	}

	@Override
	protected Logger getLogger() {
		return logger;
	}

}
