package tu.sofia.bookings.service;

import java.util.Date;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import tu.sofia.bookings.common.UnitOfWorkUtils;
import tu.sofia.bookings.dao.BookingDao;
import tu.sofia.bookings.entity.Booking;
import tu.sofia.bookings.entity.additional.BookingStatus;
import tu.sofia.bookings.entity.additional.PaymentStatus;

/**
 * Service for creating bookings
 */
@Singleton
@Path("/protected/admin/bookings")
public class BookingService {

	private UnitOfWorkUtils unitOfWorkUtils;
	private BookingDao bookingDao;

	/**
	 * Constructor
	 *
	 * @param unitOfWorkUtils
	 * @param bookingDao
	 */
	@Inject
	public BookingService(UnitOfWorkUtils unitOfWorkUtils, BookingDao bookingDao) {
		this.unitOfWorkUtils = unitOfWorkUtils;
		this.bookingDao = bookingDao;
	}

	/**
	 * Returns a list of all bookings
	 *
	 * @return list of all bookings
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Booking> getBookings() {
		unitOfWorkUtils.begin();

		List<Booking> bookings = bookingDao.findAll();

		unitOfWorkUtils.end();
		return bookings;
	}

	/**
	 * Return the information about a specific booking
	 *
	 * @param id
	 * @return the information about a specific booking
	 */
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Booking getBooking(@PathParam("id") Long id) {
		unitOfWorkUtils.begin();

		Booking booking = bookingDao.findById(id);
		if (booking == null) {
			throw new NotFoundException();
		}

		unitOfWorkUtils.end();
		return booking;
	}

	/**
	 * Returns the count of booking
	 *
	 * @return the count of booking
	 */
	@GET
	@Path("/count")
	@Produces(MediaType.TEXT_PLAIN)
	public Long count() {
		unitOfWorkUtils.begin();

		long count = bookingDao.countAll();

		unitOfWorkUtils.end();
		return count;
	}

	/**
	 * Updates the booking status of an existing booking
	 *
	 * @param id
	 * @param bookingStatus
	 * @return HTTP 204 NO CONTENT if the update was successful or
	 *         HTTP 404 NOT FOUND if there was no such booking
	 */
	@PUT
	@Path("/{id}/booking/status")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateBookingStatus(@PathParam("id") Long id, BookingStatus bookingStatus) {
		unitOfWorkUtils.begin();

		Response response = null;
		Booking persistedBooking = bookingDao.findById(id);
		if (persistedBooking != null) {
			persistedBooking.setBookingStatus(bookingStatus);
			bookingDao.update(persistedBooking);
			response = Response.status(Status.NO_CONTENT).build();
		} else {
			response = Response.status(Status.NOT_FOUND).build();
		}

		unitOfWorkUtils.end();
		return response;
	}

	/**
	 * Updates the payment status of an existing booking
	 *
	 * @param id
	 * @param paymentStatus
	 * @return HTTP 204 NO CONTENT if the update was successful or
	 *         HTTP 404 NOT FOUND if there was no such booking
	 */
	@PUT
	@Path("/{id}/payment/status")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updatePaymentStatus(@PathParam("id") Long id, PaymentStatus paymentStatus) {
		unitOfWorkUtils.begin();

		Response response = null;
		Booking persistedBooking = bookingDao.findById(id);
		if (persistedBooking != null) {
			updatePaymentProperties(paymentStatus, persistedBooking);
			bookingDao.update(persistedBooking);
			response = Response.status(Status.NO_CONTENT).build();
		} else {
			response = Response.status(Status.NOT_FOUND).build();
		}

		unitOfWorkUtils.end();
		return response;
	}

	/**
	 * Deletes specific booking
	 *
	 * @param id
	 * @return HTTP 204 NO CONTENT if the deletion was successful or
	 *         HTTP 404 NOT FOUND if there was no such booking
	 */
	@DELETE
	@Path("/{id}")
	public Response deleteBooking(@PathParam("id") Long id) {
		unitOfWorkUtils.begin();

		Response response = null;
		Booking booking = bookingDao.findById(id);
		if (booking != null) {
			bookingDao.delete(booking);
			response = Response.status(Status.NO_CONTENT).build();
		} else {
			response = Response.status(Status.NOT_FOUND).build();
		}

		unitOfWorkUtils.end();
		return response;
	}

	private void updatePaymentProperties(PaymentStatus paymentStatus, Booking persistedBooking) {
		persistedBooking.setPaymentStatus(paymentStatus);
		if (!paymentStatus.equals(PaymentStatus.UNPAID)) {
			persistedBooking.setPaymentDate(new Date());
		} else {
			persistedBooking.setPaymentDate(null);
		}
	}
}
