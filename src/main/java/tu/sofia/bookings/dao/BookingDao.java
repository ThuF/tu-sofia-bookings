package tu.sofia.bookings.dao;

import java.util.Date;
import java.util.List;

import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tu.sofia.bookings.entity.Booking;
import tu.sofia.bookings.entity.User;

/**
 * DAO class for {@link Booking}
 */
public class BookingDao extends AbstractJpaDao<Long, Booking> {

	private static final Logger logger = LoggerFactory.getLogger(BookingDao.class);

	protected BookingDao() {
		super(Booking.class);
	}

	/**
	 * Find a booking by booking id and user
	 *
	 * @param id
	 * @param user
	 * @return booking
	 */
	public Booking findByIdAndUser(Long id, User user) {
		TypedQuery<Booking> query = getEntityManager().createNamedQuery(Booking.QUERY_NAME_FIND_BY_ID_AND_USER, Booking.class);
		query.setParameter(Booking.PARAM_BOOKING_ID, id);
		query.setParameter(Booking.PARAM_USER, user);
		List<Booking> resultList = query.getResultList();
		return resultList.isEmpty() ? null : resultList.get(0);
	}

	/**
	 * Finds all bookings in the selected booking period
	 * 
	 * @param startDate
	 * @param endDate
	 * @return all bookings in the selected booking period
	 */
	public List<Booking> findByBookingPeriod(Date startDate, Date endDate) {
		TypedQuery<Booking> query = getEntityManager().createNamedQuery(Booking.QUERY_NAME_FIND_BY_BOOKING_PERIOD, Booking.class);
		query.setParameter(Booking.PARAM_START_DATE, startDate);
		query.setParameter(Booking.PARAM_END_DATE, endDate);
		return query.getResultList();
	}

	/**
	 * Find all bookings for user
	 *
	 * @param user
	 * @return all bookings for user
	 */
	public List<Booking> findAllByUser(User user) {
		TypedQuery<Booking> query = getEntityManager().createNamedQuery(Booking.QUERY_NAME_FIND_ALL_BY_USER, Booking.class);
		query.setParameter(Booking.PARAM_USER, user);
		return query.getResultList();
	}

	@Override
	protected Logger getLogger() {
		return logger;
	}
}
