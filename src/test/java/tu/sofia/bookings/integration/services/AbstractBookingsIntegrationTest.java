package tu.sofia.bookings.integration.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import javax.ws.rs.core.Response.Status;

import org.junit.After;
import org.junit.Before;

import retrofit.client.Response;
import tu.sofia.bookings.api.BookAPI;
import tu.sofia.bookings.api.BookingsAPI;
import tu.sofia.bookings.api.PaymentAPI;
import tu.sofia.bookings.api.RoomsAPI;
import tu.sofia.bookings.api.UsersAPI;
import tu.sofia.bookings.entity.Booking;
import tu.sofia.bookings.entity.Room;
import tu.sofia.bookings.entity.RoomPrice;
import tu.sofia.bookings.entity.User;
import tu.sofia.bookings.entity.additional.BedType;
import tu.sofia.bookings.entity.additional.RoomType;
import tu.sofia.bookings.entity.additional.RoomView;
import tu.sofia.bookings.integration.IntegrationTestSupport;
import tu.sofia.bookings.integration.UserRole;

@SuppressWarnings("javadoc")
public abstract class AbstractBookingsIntegrationTest extends IntegrationTestSupport {

	protected BookAPI API_BOOK;
	protected BookingsAPI API_BOOKINGS;
	protected UsersAPI API_USERS;
	protected RoomsAPI API_ROOMS;
	protected PaymentAPI API_PAYMENT;

	private Room room;

	@Before
	public void setUp() throws Exception {
		API_BOOK = createRestAdapter().create(BookAPI.class);
		API_BOOKINGS = createRestAdapter().create(BookingsAPI.class);
		API_USERS = createRestAdapter().create(UsersAPI.class);
		API_ROOMS = createRestAdapter().create(RoomsAPI.class);
		API_PAYMENT = createRestAdapter().create(PaymentAPI.class);

		createUsers();
		createRoom();
		login(loginAs());
	}

	@After
	public void cleanUp() throws Exception {
		logout();
		login(UserRole.ADMIN);

		removeBookings();
		removeRooms();
		removeUsers();
		removeRoomPrices();

		logout();
	}

	protected abstract UserRole loginAs();

	protected void createUsers() {
		logout();
		login(UserRole.ADMIN);

		User user = new User();
		user.setUserId(EveryoneLoginService.username);
		API_USERS.add(user);

		user.setUserId(AdminUserLoginService.username);
		API_USERS.add(user);

		logout();
	}

	protected void createRoom() throws IOException {
		logout();
		login(UserRole.ADMIN);

		room = new Room();
		room.setRoomType(RoomType.DELUXE);
		room.setRoomView(RoomView.BEACH_FRONT);
		room.setBedType(BedType.DOUBLE);

		long roomId = getResponseAsLong(API_ROOMS.add(room));
		room.setRoomId(roomId);

		RoomPrice roomPrice = new RoomPrice();
		roomPrice.setRoomId(roomId);
		roomPrice.setPrice(100D);

		API_PAYMENT.add(roomPrice);

		logout();
	}

	protected void removeBookings() {
		for (Booking next : API_BOOKINGS.get()) {
			Response response = API_BOOKINGS.remove(next.getBookingId());
			assertResponseStatus(Status.NO_CONTENT, response);
		}
		assertEquals(new Long(0), API_BOOKINGS.count());
	}

	protected void removeRooms() {
		for (Room next : API_ROOMS.get()) {
			Response response = API_ROOMS.remove(next.getRoomId());
			assertResponseStatus(Status.NO_CONTENT, response);
		}
		assertEquals(new Long(0), API_ROOMS.count());
	}

	protected void removeUsers() {
		for (User next : API_USERS.get()) {
			Response response = API_USERS.remove(next.getUserId());
			assertResponseStatus(Status.NO_CONTENT, response);
		}
		assertEquals(new Long(0), API_USERS.count());
	}

	protected void removeRoomPrices() {
		for (RoomPrice next : API_PAYMENT.get()) {
			Response response = API_PAYMENT.remove(next.getRoomId());
			assertResponseStatus(Status.NO_CONTENT, response);
		}
		assertEquals(new Long(0), API_PAYMENT.count());
	}

	protected Booking createBooking(Date startDate, Date endDate, Long roomId) {
		Booking entity = new Booking();
		entity.setStartDate(startDate);
		entity.setEndDate(endDate);
		entity.setRoomId(roomId);
		return entity;
	}

	protected Room getPersistedRoom() {
		return room;
	}

	protected void assertBookingEquals(Booking expected, Booking actual) {
		assertNotNull(expected);
		assertEquals(getDate(expected.getStartDate()), getDate(actual.getStartDate()));
		assertEquals(getDate(expected.getEndDate()), getDate(actual.getEndDate()));
		assertEquals(expected.getBookingStatus(), actual.getBookingStatus());
		assertEquals(expected.getPaymentStatus(), actual.getPaymentStatus());
	}

	private Date getDate(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}
}
