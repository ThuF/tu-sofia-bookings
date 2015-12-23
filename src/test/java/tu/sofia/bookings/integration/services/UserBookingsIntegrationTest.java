package tu.sofia.bookings.integration.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Date;
import java.util.List;

import javax.ws.rs.core.Response.Status;

import org.junit.Test;

import retrofit.RetrofitError;
import retrofit.client.Response;
import tu.sofia.bookings.api.UserBookingsAPI;
import tu.sofia.bookings.entity.Booking;
import tu.sofia.bookings.integration.UserRole;
import tu.sofia.bookings.validation.ApplicationExceptionMessage;

@SuppressWarnings("javadoc")
public class UserBookingsIntegrationTest extends AbstractBookingsIntegrationTest {

	private UserBookingsAPI API;

	@Override
	public void setUp() throws Exception {
		super.setUp();
		API = createRestAdapter().create(UserBookingsAPI.class);
	}

	@Test
	public void testEmpty() throws Exception {
		List<Booking> results = API.get();
		assertNotNull(results);
		assertEquals(0, results.size());
	}

	@Test
	public void testAddBookingAndListAll() throws Exception {
		Response response = API_BOOK.add(createBooking(new Date(), new Date(), 1L));
		assertResponseStatus(Status.CREATED, response);

		List<Booking> results = API.get();
		assertNotNull(results);
		assertEquals(1, results.size());
	}

	@Test
	public void testAddBookingAndListAllFromDifferentUsers() throws Exception {
		Response response = API_BOOK.add(createBooking(new Date(), new Date(), 1L));
		assertResponseStatus(Status.CREATED, response);

		logout();
		login(UserRole.ADMIN);

		List<Booking> results = API.get();
		assertNotNull(results);
		assertEquals(0, results.size());

		assertEquals(new Long(1), API_BOOKINGS.count());
	}

	@Test
	public void testAddBookingAndFindByIdAndUser() throws Exception {
		Booking expectedBooking = createBooking(new Date(), new Date(), 1L);
		Response response = API_BOOK.add(expectedBooking);
		assertResponseStatus(Status.CREATED, response);

		long bookingId = getResponseAsLong(response);
		assertBookingEquals(expectedBooking, API.get(bookingId));
	}

	@Test
	public void testAddBookingAndFindByIdAndUserFromDifferentUser() throws Exception {
		Response response = API_BOOK.add(createBooking(new Date(), new Date(), 1L));
		assertResponseStatus(Status.CREATED, response);

		long bookingId = getResponseAsLong(response);

		logout();
		login(UserRole.ADMIN);

		try {
			API.get(bookingId);
		} catch (RetrofitError e) {
			assertResponseStatus(Status.NOT_FOUND, e.getResponse());
			ApplicationExceptionMessage message = getApplicationExceptionMessage(e);
			assertEquals(Status.NOT_FOUND.getStatusCode(), message.getStatus());
			assertEquals("There is no booking with [bookingId=" + bookingId + "]", message.getMessage());
		}
	}

	@Override
	protected UserRole loginAs() {
		return UserRole.EVERYONE;
	}
}
