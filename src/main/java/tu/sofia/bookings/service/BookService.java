package tu.sofia.bookings.service;

import java.text.MessageFormat;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import tu.sofia.bookings.common.UnitOfWorkUtils;
import tu.sofia.bookings.dao.BookingDao;
import tu.sofia.bookings.dao.UserDao;
import tu.sofia.bookings.entity.Booking;
import tu.sofia.bookings.entity.User;
import tu.sofia.bookings.validation.BookValidator;
import tu.sofia.bookings.validation.ValidationErrorResponseBuilder;
import tu.sofia.bookings.validation.interfaces.IBookingValidator;

/**
 * Service for creating bookings
 */
@Singleton
@Path("/protected/user/book")
public class BookService {

	private static final String VALIDATION_MESSAGE_NO_USER_REGISTERED_WITH_USER_ID = "No user registered with userId [{0}]";
	private static final String VALIDATION_MESSAGE_THE_USER_ID_PROPERTY_CAN_T_BE_EXTRACTED_FROM_THE_REQUEST = "The [userId] property can't be extracted from the request";

	private UnitOfWorkUtils unitOfWorkUtils;
	private IBookingValidator bookingValidator;
	private BookingDao bookingDao;
	private UserDao userDao;
	private PaymentService paymentService;

	/**
	 * Constructor
	 *
	 * @param unitOfWorkUtils
	 * @param bookingValidator
	 * @param bookingDao
	 * @param userDao
	 * @param paymentService
	 */
	@Inject
	public BookService(UnitOfWorkUtils unitOfWorkUtils, BookValidator bookingValidator, BookingDao bookingDao, UserDao userDao,
			PaymentService paymentService) {
		this.unitOfWorkUtils = unitOfWorkUtils;
		this.bookingValidator = bookingValidator;
		this.bookingDao = bookingDao;
		this.userDao = userDao;
		this.paymentService = paymentService;
	}

	/**
	 * Adds new booking
	 *
	 * @param booking
	 * @param request
	 * @return HTTP 200 OK if the booking was successfully added or
	 *         HTTP 400 if something went wrong
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addBooking(Booking booking, @Context HttpServletRequest request) {
		unitOfWorkUtils.begin();

		Response response = null;
		if (bookingValidator.isValid(booking)) {
			String userId = request.getRemoteUser();
			if (userId != null) {
				User user = userDao.findById(userId);
				if (user != null) {
					booking.setUser(user);
					booking.setPaymentAmount(paymentService.getRoomPrice(booking.getRoomId()));
					bookingDao.create(booking);
					response = Response.status(Status.CREATED).entity(booking.getBookingId()).build();
				} else {
					response = ValidationErrorResponseBuilder.toResponse(Status.BAD_REQUEST,
							MessageFormat.format(VALIDATION_MESSAGE_NO_USER_REGISTERED_WITH_USER_ID, userId));
				}
			} else {
				response = ValidationErrorResponseBuilder.toResponse(Status.UNAUTHORIZED,
						VALIDATION_MESSAGE_THE_USER_ID_PROPERTY_CAN_T_BE_EXTRACTED_FROM_THE_REQUEST);
			}
		} else {
			response = ValidationErrorResponseBuilder.toResponse(bookingValidator);
		}

		unitOfWorkUtils.end();
		return response;
	}
}
