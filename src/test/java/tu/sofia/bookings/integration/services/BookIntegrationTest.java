package tu.sofia.bookings.integration.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ws.rs.core.Response.Status;

import org.junit.Test;

import retrofit.RetrofitError;
import retrofit.client.Response;
import tu.sofia.bookings.entity.Booking;
import tu.sofia.bookings.entity.Room;
import tu.sofia.bookings.integration.UserRole;
import tu.sofia.bookings.validation.ApplicationExceptionMessage;

@SuppressWarnings("javadoc")
public class BookIntegrationTest extends AbstractBookingsIntegrationTest {

	@Test
	public void testAdd() throws Exception {
		Response response = API_BOOK.add(createBooking(new Date(), new Date(), getPersistedRoom().getRoomId()));
		assertResponseStatus(Status.CREATED, response);
		assertNotNull(getResponseAsLong(response));
	}

	@Test
	public void testAddInvalidEntityShouldReturnBadRequest() throws Exception {
		try {
			API_BOOK.add(createBooking(null, null, null));
		} catch (RetrofitError e) {
			assertResponseStatus(Status.BAD_REQUEST, e.getResponse());
			ApplicationExceptionMessage message = getApplicationExceptionMessage(e);
			assertEquals(Status.BAD_REQUEST.getStatusCode(), message.getStatus());
			assertEquals("The [startDate] property can't be null", message.getMessage());
		}
	}

	@Test
	public void testAddInvalidEntityShouldReturnBadRequest2() throws Exception {
		try {
			API_BOOK.add(createBooking(new Date(), null, null));
		} catch (RetrofitError e) {
			assertResponseStatus(Status.BAD_REQUEST, e.getResponse());
			ApplicationExceptionMessage message = getApplicationExceptionMessage(e);
			assertEquals(Status.BAD_REQUEST.getStatusCode(), message.getStatus());
			assertEquals("The [endDate] property can't be null", message.getMessage());
		}
	}

	@Test
	public void testAddInvalidEntityShouldReturnBadRequest3() throws Exception {
		try {
			API_BOOK.add(createBooking(new Date(), new Date(), null));
		} catch (RetrofitError e) {
			assertResponseStatus(Status.BAD_REQUEST, e.getResponse());
			ApplicationExceptionMessage message = getApplicationExceptionMessage(e);
			assertEquals(Status.BAD_REQUEST.getStatusCode(), message.getStatus());
			assertEquals("No rooms are provided for the booking", message.getMessage());
		}
	}

	@Test
	public void testAddValidEntityWithoutRegisteredUserShouldReturnBadRequest() throws Exception {
		logout();
		login(UserRole.ADMIN);
		removeUsers();
		logout();
		login(UserRole.EVERYONE);

		try {
			API_BOOK.add(createBooking(new Date(), new Date(), 1L));
		} catch (RetrofitError e) {
			assertResponseStatus(Status.BAD_REQUEST, e.getResponse());
			ApplicationExceptionMessage message = getApplicationExceptionMessage(e);
			assertEquals(Status.BAD_REQUEST.getStatusCode(), message.getStatus());
			assertEquals("No user registered with [userId=user]", message.getMessage());
		}
	}

	@Test
	public void testGetAvailableEntitiesShouldReturnOnlyTheOnesThatAnswersTheFilterCriteria() throws Exception {
		Booking booking = new Booking();
		booking.setStartDate(getTime(Calendar.DAY_OF_MONTH, 1));
		booking.setEndDate(getTime(Calendar.DAY_OF_MONTH, 3));
		booking.setRoomId(getPersistedRoom().getRoomId());
		API_BOOK.add(booking);

		booking.setStartDate(getTime(Calendar.DAY_OF_MONTH, 7));
		booking.setEndDate(getTime(Calendar.DAY_OF_MONTH, 10));
		booking.setRoomId(getPersistedRoom().getRoomId());

		API_BOOK.add(booking);

		logout();
		login(UserRole.ADMIN);
		List<Room> results = API_ROOMS.getAvailableRooms(getTime(Calendar.DAY_OF_MONTH, 4).getTime(), getTime(Calendar.DAY_OF_MONTH, 20).getTime());
		assertNotNull(results);
		assertEquals(0, results.size());
		logout();
	}

	@Test
	public void testGetAvailableEntitiesShouldReturnOnlyTheOnesThatAnswersTheFilterCriteria1() throws Exception {
		Booking booking = new Booking();
		booking.setStartDate(getTime(Calendar.DAY_OF_MONTH, 1));
		booking.setEndDate(getTime(Calendar.DAY_OF_MONTH, 3));
		booking.setRoomId(getPersistedRoom().getRoomId());
		API_BOOK.add(booking);

		booking.setStartDate(getTime(Calendar.DAY_OF_MONTH, 5));
		booking.setEndDate(getTime(Calendar.DAY_OF_MONTH, 6));
		booking.setRoomId(getPersistedRoom().getRoomId());

		API_BOOK.add(booking);

		logout();
		login(UserRole.ADMIN);
		List<Room> results = API_ROOMS.getAvailableRooms(getTime(Calendar.DAY_OF_MONTH, 10).getTime(), getTime(Calendar.DAY_OF_MONTH, 15).getTime());
		assertNotNull(results);
		assertEquals(1, results.size());
		assertEquals(getPersistedRoom().getRoomId(), results.get(0).getRoomId());
		logout();
	}

	@Test
	public void testGetAvailableEntitiesShouldReturnEmptyListWhenNoEntitiesAdded() throws Exception {
		logout();
		login(UserRole.ADMIN);
		removeRooms();
		List<Room> results = API_ROOMS.getAvailableRooms(getTime(Calendar.DAY_OF_MONTH, 0).getTime(), getTime(Calendar.DAY_OF_MONTH, 10).getTime());
		assertNotNull(results);
		assertEquals(0, results.size());
		logout();
	}

	@Override
	protected UserRole loginAs() {
		return UserRole.EVERYONE;
	}

	private Date getTime(int field, int amount) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(field, amount);
		return calendar.getTime();
	}
}
