package tu.sofia.bookings.service;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.GET;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import tu.sofia.bookings.common.UnitOfWorkUtils;
import tu.sofia.bookings.dao.BookingDao;
import tu.sofia.bookings.dao.UserDao;
import tu.sofia.bookings.entity.Booking;
import tu.sofia.bookings.entity.User;
import tu.sofia.bookings.entity.dto.BookingDto;

/**
 * Service for creating bookings
 */
@Singleton
@Path("/protected/user/bookings")
public class UserBookingService {

	private static final String VALIDATION_MESSAGE_THE_USER_ID_PROPERTY_CAN_T_BE_EXTRACTED_FROM_THE_REQUEST = "The [userId] property can't be extracted from the request";
	private static final String VALIDATION_MESSAGE_THERE_IS_NO_USER_REGISTERED_WITH_USER_ID = "There is no user registered with [userId={0}]";
	private static final String VALIDATION_MESSAGE_THERE_IS_NO_BOOKING_WITH_BOOKING_ID = "There is no booking with [bookingId={0}]";

	private UnitOfWorkUtils unitOfWorkUtils;
	private UserDao userDao;
	private BookingDao bookingDao;

	/**
	 * Constructor
	 *
	 * @param unitOfWorkUtils
	 * @param userDao
	 * @param bookingDao
	 */
	@Inject
	public UserBookingService(UnitOfWorkUtils unitOfWorkUtils, UserDao userDao, BookingDao bookingDao) {
		this.unitOfWorkUtils = unitOfWorkUtils;
		this.userDao = userDao;
		this.bookingDao = bookingDao;
	}

	/**
	 * Return the information about a specific booking
	 *
	 * @param id
	 * @param request
	 * @return the information about a specific booking
	 */
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public BookingDto getBooking(@PathParam("id") Long id, @Context HttpServletRequest request) {
		unitOfWorkUtils.begin();

		Booking booking = null;
		String userId = request.getRemoteUser();
		if (userId != null) {
			User user = userDao.findById(userId);
			if (user != null) {
				booking = bookingDao.findByIdAndUser(id, user);
				if (booking == null) {
					throw new NotFoundException(MessageFormat.format(VALIDATION_MESSAGE_THERE_IS_NO_BOOKING_WITH_BOOKING_ID, id));
				}
			} else {
				throw new BadRequestException(MessageFormat.format(VALIDATION_MESSAGE_THERE_IS_NO_USER_REGISTERED_WITH_USER_ID, userId));
			}
		} else {
			throw new NotAuthorizedException(VALIDATION_MESSAGE_THE_USER_ID_PROPERTY_CAN_T_BE_EXTRACTED_FROM_THE_REQUEST);
		}

		unitOfWorkUtils.end();
		return new BookingDto(booking);
	}

	/**
	 * Returns a list of all bookings
	 *
	 * @param request
	 * @return list of all bookings
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<BookingDto> getBookings(@Context HttpServletRequest request) {
		unitOfWorkUtils.begin();

		List<BookingDto> bookings = new ArrayList<BookingDto>();
		String userId = request.getRemoteUser();
		if (userId != null) {
			User user = userDao.findById(userId);
			if (user != null) {
				for (Booking next : bookingDao.findAllByUser(user)) {
					bookings.add(new BookingDto(next));
				}
			} else {
				throw new BadRequestException(MessageFormat.format(VALIDATION_MESSAGE_THERE_IS_NO_USER_REGISTERED_WITH_USER_ID, userId));
			}
		} else {
			throw new NotAuthorizedException(VALIDATION_MESSAGE_THE_USER_ID_PROPERTY_CAN_T_BE_EXTRACTED_FROM_THE_REQUEST);
		}

		unitOfWorkUtils.end();
		return bookings;
	}
}
