package tu.sofia.bookings.integration.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response.Status;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import retrofit.RetrofitError;
import retrofit.client.Response;
import tu.sofia.bookings.api.RoomsAPI;
import tu.sofia.bookings.entity.BedType;
import tu.sofia.bookings.entity.Room;
import tu.sofia.bookings.entity.RoomType;
import tu.sofia.bookings.entity.RoomView;
import tu.sofia.bookings.integration.IntegrationTestSupport;
import tu.sofia.bookings.integration.UserRole;
import tu.sofia.bookings.validation.ApplicationExceptionMessage;

@SuppressWarnings("javadoc")
public class RoomsIntegrationTest extends IntegrationTestSupport {

	private static final List<Room> testData = new ArrayList<Room>();
	private RoomsAPI API;

	@BeforeClass
	public static void initialize() throws Exception {
		Room data1 = new Room();
		data1.setRoomType(RoomType.DELUXE);
		data1.setRoomView(RoomView.OCEAN_VIEW);
		data1.setBedType(BedType.CALIFORNIA_KING);
		data1.setDescription("The perfect room for a vacation!");

		Room data2 = new Room();
		data2.setRoomType(RoomType.STANDARD);
		data2.setRoomView(RoomView.CITY_VIEW);
		data2.setBedType(BedType.DOUBLE);

		testData.add(data1);
		testData.add(data2);
	}

	@Before
	public void setUp() throws Exception {
		API = createRestAdapter().create(RoomsAPI.class);
		login(UserRole.ADMIN);
	}

	@After
	public void cleanUp() throws Exception {
		logout();
		login(UserRole.ADMIN);

		for (Room next : API.get()) {
			Response response = API.remove(next.getRoomId());
			assertResponseStatus(Status.NO_CONTENT, response);
		}
		assertEquals(new Long(0), API.count());

		logout();
		for (Room next : testData) {
			next.setRoomId(null);
		}
	}

	@Test
	public void testEmpty() throws Exception {
		List<Room> results = API.get();
		assertEquals(0, results.size());
		assertEquals(new Long(0), API.count());
	}

	@Test
	public void testAddEntity() throws Exception {
		Response response = API.add(testData.get(0));
		assertResponseStatus(Status.CREATED, response);
		assertEquals(new Long(1), API.count());
	}

	@Test
	public void testAddInvalidEntityShouldReturnBadRequest() throws Exception {
		try {
			API.add(new Room());
		} catch (RetrofitError e) {
			Response response = e.getResponse();
			assertResponseStatus(Status.BAD_REQUEST, response);
			ApplicationExceptionMessage message = getApplicationExceptionMessage(e);
			assertNotNull(message);
			assertEquals(Status.BAD_REQUEST.getStatusCode(), message.getStatus());
			assertEquals("The [roomType] property can't be null", message.getMessage());
		}
	}

	@Test
	public void testAddInvalidEntity2ShouldReturnBadRequest() throws Exception {
		try {
			Room room = new Room();
			room.setRoomType(RoomType.DELUXE);
			API.add(room);
		} catch (RetrofitError e) {
			Response response = e.getResponse();
			assertResponseStatus(Status.BAD_REQUEST, response);
			ApplicationExceptionMessage message = getApplicationExceptionMessage(e);
			assertNotNull(message);
			assertEquals(Status.BAD_REQUEST.getStatusCode(), message.getStatus());
			assertEquals("The [roomView] property can't be null", message.getMessage());
		}
	}

	@Test
	public void testAddInvalidEntity3ShouldReturnBadRequest() throws Exception {
		try {
			Room room = new Room();
			room.setRoomType(RoomType.DELUXE);
			room.setRoomView(RoomView.OCEAN_VIEW);
			API.add(room);
		} catch (RetrofitError e) {
			Response response = e.getResponse();
			assertResponseStatus(Status.BAD_REQUEST, response);
			ApplicationExceptionMessage message = getApplicationExceptionMessage(e);
			assertNotNull(message);
			assertEquals(Status.BAD_REQUEST.getStatusCode(), message.getStatus());
			assertEquals("The [bedType] property can't be null", message.getMessage());
		}
	}

	@Test
	public void testUpdateEntity() throws Exception {
		Response response = API.add(testData.get(0));
		assertResponseStatus(Status.CREATED, response);
		testData.get(0).setRoomId(getResponseAsLong(response));

		response = API.update(testData.get(0).getRoomId(), testData.get(1));
		assertResponseStatus(Status.NO_CONTENT, response);

		List<Room> results = API.get();
		assertEquals(1, results.size());
		assertEquals(new Long(1), API.count());
	}

	@Test
	public void testRemoveEntity() throws Exception {
		Response response = API.add(testData.get(0));
		assertResponseStatus(Status.CREATED, response);
		testData.get(0).setRoomId(getResponseAsLong(response));

		response = API.remove(testData.get(0).getRoomId());
		assertResponseStatus(Status.NO_CONTENT, response);
		assertEquals(new Long(0), API.count());
	}

	@Test
	public void testGetSingleEntity() throws Exception {
		Response response = API.add(testData.get(0));
		assertResponseStatus(Status.CREATED, response);
		testData.get(0).setRoomId(getResponseAsLong(response));

		Room result = API.get(testData.get(0).getRoomId());

		assertRoomEquals(testData.get(0), result);
		assertEquals(new Long(1), API.count());
	}

	@Test
	public void testAddTwoEntities() throws Exception {
		for (Room next : testData) {
			Response response = API.add(next);
			assertResponseStatus(Status.CREATED, response);
			next.setRoomId(getResponseAsLong(response));
		}

		List<Room> results = API.get();
		assertNotNull(results);
		assertEquals(2, results.size());
		for (int i = 0; i < testData.size(); i++) {
			assertRoomEquals(testData.get(i), results.get(i));
		}

		assertEquals(new Long(2), API.count());
	}

	private void assertRoomEquals(Room expected, Room actual) {
		assertNotNull(actual);
		assertEquals(expected.getRoomId(), actual.getRoomId());
		assertEquals(expected.getRoomType(), actual.getRoomType());
		assertEquals(expected.getRoomView(), actual.getRoomView());
		assertEquals(expected.getBedType(), actual.getBedType());
		assertEquals(expected.getDescription(), actual.getDescription());
	}
}
