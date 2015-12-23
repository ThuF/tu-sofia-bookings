package tu.sofia.bookings.integration.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.core.Response.Status;

import org.junit.BeforeClass;
import org.junit.Test;

import retrofit.client.Response;
import tu.sofia.bookings.entity.Booking;
import tu.sofia.bookings.entity.additional.BookingStatus;
import tu.sofia.bookings.entity.additional.PaymentStatus;
import tu.sofia.bookings.integration.UserRole;

@SuppressWarnings("javadoc")
public class BookingsIntegrationTest extends AbstractBookingsIntegrationTest {

	private static final List<Booking> testData = new ArrayList<Booking>();

	@BeforeClass
	public static void initialize() throws Exception {
		Booking data1 = new Booking();
		data1.setStartDate(new Date());
		data1.setEndDate(new Date());
		data1.getRoomsId().add(1L);

		Booking data2 = new Booking();
		data2.setStartDate(new Date());
		data2.setEndDate(new Date());
		data2.getRoomsId().add(2L);

		testData.add(data1);
		testData.add(data2);
	}

	@Override
	public void cleanUp() throws Exception {
		super.cleanUp();
		for (Booking next : testData) {
			next.setBookingId(null);
		}
	}

	@Test
	public void testEmpty() throws Exception {
		List<Booking> results = API_BOOKINGS.get();
		assertEquals(0, results.size());
		assertEquals(new Long(0), API_BOOKINGS.count());
	}

	@Test
	public void testAddEntity() throws Exception {
		Response response = API_BOOK.add(testData.get(0));
		assertResponseStatus(Status.CREATED, response);
		assertEquals(new Long(1), API_BOOKINGS.count());
	}

	@Test
	public void testUpdateBookingStatus() throws Exception {
		Response response = API_BOOK.add(testData.get(0));
		assertResponseStatus(Status.CREATED, response);
		testData.get(0).setBookingId(getResponseAsLong(response));

		response = API_BOOKINGS.updateBookingStatus(testData.get(0).getBookingId(), BookingStatus.CONFIRMED);
		assertResponseStatus(Status.NO_CONTENT, response);

		List<Booking> results = API_BOOKINGS.get();
		assertEquals(1, results.size());
		assertEquals(new Long(1), API_BOOKINGS.count());
		assertNotNull(results.get(0).getBookingStatus());
		assertEquals(BookingStatus.CONFIRMED, results.get(0).getBookingStatus());
	}

	@Test
	public void testUpdatePaymentStatus() throws Exception {
		Response response = API_BOOK.add(testData.get(0));
		assertResponseStatus(Status.CREATED, response);
		testData.get(0).setBookingId(getResponseAsLong(response));

		response = API_BOOKINGS.updatePaymentStatus(testData.get(0).getBookingId(), PaymentStatus.FULLY_PAID);
		assertResponseStatus(Status.NO_CONTENT, response);

		List<Booking> results = API_BOOKINGS.get();
		assertEquals(1, results.size());
		assertEquals(new Long(1), API_BOOKINGS.count());
		assertNotNull(results.get(0).getPaymentStatus());
		assertEquals(PaymentStatus.FULLY_PAID, results.get(0).getPaymentStatus());
	}

	@Test
	public void testRemoveEntity() throws Exception {
		Response response = API_BOOK.add(testData.get(0));
		assertResponseStatus(Status.CREATED, response);
		testData.get(0).setBookingId(getResponseAsLong(response));

		response = API_BOOKINGS.remove(testData.get(0).getBookingId());
		assertResponseStatus(Status.NO_CONTENT, response);
		assertEquals(new Long(0), API_BOOKINGS.count());
	}

	@Test
	public void testGetSingleEntity() throws Exception {
		Response response = API_BOOK.add(testData.get(0));
		assertResponseStatus(Status.CREATED, response);
		testData.get(0).setBookingId(getResponseAsLong(response));

		Booking result = API_BOOKINGS.get(testData.get(0).getBookingId());

		assertBookingEquals(testData.get(0), result);
		assertEquals(new Long(1), API_BOOKINGS.count());
	}

	@Test
	public void testAddTwoEntities() throws Exception {
		Response response = API_BOOK.add(testData.get(0));
		assertResponseStatus(Status.CREATED, response);

		response = API_BOOK.add(testData.get(1));
		assertResponseStatus(Status.CREATED, response);

		List<Booking> results = API_BOOKINGS.get();
		assertNotNull(results);
		assertEquals(2, results.size());
		for (int i = 0; i < results.size(); i++) {
			assertBookingEquals(testData.get(i), results.get(i));
		}

		assertEquals(new Long(2), API_BOOKINGS.count());
	}

	@Override
	protected UserRole loginAs() {
		return UserRole.ADMIN;
	}
}
