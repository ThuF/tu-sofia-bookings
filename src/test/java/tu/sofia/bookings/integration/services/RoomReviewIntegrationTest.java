package tu.sofia.bookings.integration.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.List;

import javax.ws.rs.core.Response.Status;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import retrofit.RetrofitError;
import retrofit.client.Response;
import tu.sofia.bookings.api.RoomReviewAPI;
import tu.sofia.bookings.api.RoomsAPI;
import tu.sofia.bookings.api.UsersAPI;
import tu.sofia.bookings.entity.Room;
import tu.sofia.bookings.entity.RoomReview;
import tu.sofia.bookings.entity.User;
import tu.sofia.bookings.entity.additional.BedType;
import tu.sofia.bookings.entity.additional.RoomType;
import tu.sofia.bookings.entity.additional.RoomView;
import tu.sofia.bookings.integration.IntegrationTestSupport;
import tu.sofia.bookings.integration.UserRole;
import tu.sofia.bookings.validation.ApplicationExceptionMessage;

@SuppressWarnings("javadoc")
public class RoomReviewIntegrationTest extends IntegrationTestSupport {

	private RoomReviewAPI API_ROOM_REVIEW;
	private RoomsAPI API_ROOMS;
	private UsersAPI API_USERS;

	private RoomReview review;
	private RoomReview review2;

	@Before
	public void setUp() throws Exception {
		API_ROOM_REVIEW = createRestAdapter().create(RoomReviewAPI.class);
		API_ROOMS = createRestAdapter().create(RoomsAPI.class);
		API_USERS = createRestAdapter().create(UsersAPI.class);
		login(UserRole.ADMIN);

		addUser(AdminUserLoginService.username);
		review = addReview(addRoom(), "Very cool room!", 5);
		review2 = addReview(addRoom(), "Not so cool room!", 0);
	}

	private void addUser(String userId) {
		User user = new User();
		user.setUserId(userId);
		API_USERS.add(user);
	}

	private RoomReview addReview(Room room, String comment, Integer rating) {
		RoomReview roomReview = new RoomReview();
		roomReview.setRoomId(room.getRoomId());
		roomReview.setComment(comment);
		roomReview.setRating(rating);
		return roomReview;
	}

	private Room addRoom() throws IOException {
		Room room = new Room();
		room.setRoomType(RoomType.DELUXE);
		room.setRoomView(RoomView.OCEAN_VIEW);
		room.setBedType(BedType.CALIFORNIA_KING);
		room.setDescription("The perfect room for a vacation!");
		Response response = API_ROOMS.add(room);
		room.setRoomId(getResponseAsLong(response));
		return room;
	}

	@After
	public void cleanUp() throws Exception {
		logout();
		login(UserRole.ADMIN);

		removeReviews();
		removeRooms();
		removeUsers();

		logout();
	}

	private void removeReviews() {
		for (RoomReview next : API_ROOM_REVIEW.get()) {
			Response response = API_ROOM_REVIEW.remove(next.getRoomId(), AdminUserLoginService.username);
			assertResponseStatus(Status.NO_CONTENT, response);
		}
		assertEquals(new Long(0), API_ROOM_REVIEW.count());
	}

	private void removeRooms() {
		for (Room next : API_ROOMS.get()) {
			Response response = API_ROOMS.remove(next.getRoomId());
			assertResponseStatus(Status.NO_CONTENT, response);
		}
		assertEquals(new Long(0), API_ROOMS.count());
	}

	private void removeUsers() {
		for (User next : API_USERS.get()) {
			Response response = API_USERS.remove(next.getUserId());
			assertResponseStatus(Status.NO_CONTENT, response);
		}
		assertEquals(new Long(0), API_USERS.count());
	}

	@Test
	public void testEmpty() throws Exception {
		List<RoomReview> results = API_ROOM_REVIEW.get();
		assertEquals(0, results.size());
		assertEquals(new Long(0), API_ROOM_REVIEW.count());
	}

	@Test
	public void testAddEntity() throws Exception {
		Response response = API_ROOM_REVIEW.add(review);
		assertResponseStatus(Status.CREATED, response);
		assertEquals(new Long(1), API_ROOM_REVIEW.count());
	}

	@Test
	public void testUpdateEntity() throws Exception {
		Response response = API_ROOM_REVIEW.add(review);
		assertResponseStatus(Status.CREATED, response);

		review2.setRoomId(review.getRoomId());

		response = API_ROOM_REVIEW.update(review.getRoomId(), AdminUserLoginService.username, review2);
		assertResponseStatus(Status.NO_CONTENT, response);

		List<RoomReview> results = API_ROOM_REVIEW.get();
		assertEquals(1, results.size());
		assertRoomReviewEquals(review2, AdminUserLoginService.username, results.get(0));
		assertEquals(new Long(1), API_ROOM_REVIEW.count());
	}

	@Test
	public void testRemoveEntity() throws Exception {
		Response response = API_ROOM_REVIEW.add(review);
		assertResponseStatus(Status.CREATED, response);

		response = API_ROOM_REVIEW.remove(review.getRoomId(), AdminUserLoginService.username);
		assertResponseStatus(Status.NO_CONTENT, response);
		assertEquals(new Long(0), API_ROOM_REVIEW.count());
	}

	@Test
	public void testGetSingleEntity() throws Exception {
		Response response = API_ROOM_REVIEW.add(review);
		assertResponseStatus(Status.CREATED, response);

		RoomReview result = API_ROOM_REVIEW.get(review.getRoomId(), AdminUserLoginService.username);

		assertRoomReviewEquals(review, AdminUserLoginService.username, result);
		assertEquals(new Long(1), API_ROOM_REVIEW.count());
	}

	@Test
	public void testAddTwoEntities() throws Exception {
		Response response = API_ROOM_REVIEW.add(review);
		assertResponseStatus(Status.CREATED, response);

		response = API_ROOM_REVIEW.add(review2);
		assertResponseStatus(Status.CREATED, response);

		List<RoomReview> results = API_ROOM_REVIEW.get();
		assertNotNull(results);
		assertEquals(2, results.size());

		assertRoomReviewEquals(review, AdminUserLoginService.username, results.get(0));
		assertRoomReviewEquals(review2, AdminUserLoginService.username, results.get(1));

		assertEquals(new Long(2), API_ROOM_REVIEW.count());
	}

	@Test
	public void testAddTwoEntitiesWithTheSameRoomId() throws Exception {
		Response response = API_ROOM_REVIEW.add(review);
		assertResponseStatus(Status.CREATED, response);

		try {
			API_ROOM_REVIEW.add(review);
		} catch (RetrofitError e) {
			assertResponseStatus(Status.BAD_REQUEST, e.getResponse());
			ApplicationExceptionMessage message = getApplicationExceptionMessage(e);
			assertNotNull(message);
			assertEquals(Status.BAD_REQUEST.getStatusCode(), message.getStatus());
			assertEquals(MessageFormat.format("There is already a review for [roomId={0}] from the current user", review.getRoomId()),
					message.getMessage());
		}

		assertEquals(new Long(1), API_ROOM_REVIEW.count());
	}

	private void assertRoomReviewEquals(RoomReview expected, String expectedUserId, RoomReview actual) {
		assertNotNull(actual);
		assertEquals(expected.getRoomId(), actual.getRoomId());
		assertEquals(expectedUserId, actual.getUserId());
		assertEquals(expected.getComment(), actual.getComment());
		assertEquals(expected.getRating(), actual.getRating());
	}
}
