package tu.sofia.bookings.integration.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Date;

import javax.ws.rs.core.Response.Status;

import org.junit.Test;

import retrofit.RetrofitError;
import retrofit.client.Response;
import tu.sofia.bookings.integration.UserRole;
import tu.sofia.bookings.validation.ApplicationExceptionMessage;

@SuppressWarnings("javadoc")
public class BookIntegrationTest extends AbstractBookingsIntegrationTest {

	@Test
	public void testAdd() throws Exception {
		Response response = API_BOOK.add(createBooking(new Date(), new Date(), 1L));
		assertResponseStatus(Status.CREATED, response);
		assertNotNull(getResponseAsLong(response));
	}

	@Test
	public void testAddInvalidEntityShouldReturnBadRequest() throws Exception {
		try {
			API_BOOK.add(createBooking(null, null));
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
			API_BOOK.add(createBooking(new Date(), null));
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
			API_BOOK.add(createBooking(new Date(), new Date()));
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
			assertEquals("No user registered with userId [user]", message.getMessage());
		}
	}

	@Override
	protected UserRole loginAs() {
		return UserRole.EVERYONE;
	}
}
