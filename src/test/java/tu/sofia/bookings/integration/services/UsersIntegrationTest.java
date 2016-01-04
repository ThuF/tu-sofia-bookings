package tu.sofia.bookings.integration.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response.Status;

import org.junit.BeforeClass;
import org.junit.Test;

import retrofit.client.Response;
import tu.sofia.bookings.entity.User;
import tu.sofia.bookings.integration.UserRole;

@SuppressWarnings("javadoc")
public class UsersIntegrationTest extends AbstractUsersIntegrationTest {

	private static final List<User> testData = new ArrayList<User>();

	@BeforeClass
	public static void initialize() throws Exception {
		User data1 = new User();
		data1.setUserId("1");
		data1.setFirstName("Yordan");

		User data2 = new User();
		data2.setUserId("2");
		data2.setFirstName("Desislava");

		testData.add(data1);
		testData.add(data2);
	}

	@Override
	protected UserRole loginAs() {
		return UserRole.ADMIN;
	}

	@Test
	public void testEmpty() throws Exception {
		List<User> results = API_USERS.get();
		assertEquals(0, results.size());
		assertEquals(new Long(0), API_USERS.count());
	}

	@Test
	public void testAddEntity() throws Exception {
		Response response = API_USERS.add(testData.get(0));
		assertResponseStatus(Status.CREATED, response);
		assertEquals(new Long(1), API_USERS.count());
	}

	@Test
	public void testUpdateEntity() throws Exception {
		Response response = API_USERS.add(testData.get(0));
		assertResponseStatus(Status.CREATED, response);

		response = API_USERS.update(testData.get(0).getUserId(), testData.get(1));
		assertResponseStatus(Status.NO_CONTENT, response);

		List<User> results = API_USERS.get();
		assertEquals(1, results.size());
		assertEquals(new Long(1), API_USERS.count());
	}

	@Test
	public void testRemoveEntity() throws Exception {
		Response response = API_USERS.add(testData.get(0));
		assertResponseStatus(Status.CREATED, response);

		response = API_USERS.remove(testData.get(0).getUserId());
		assertResponseStatus(Status.NO_CONTENT, response);
		assertEquals(new Long(0), API_USERS.count());
	}

	@Test
	public void testGetSingleEntity() throws Exception {
		Response response = API_USERS.add(testData.get(0));
		assertResponseStatus(Status.CREATED, response);

		User result = API_USERS.get(testData.get(0).getUserId());

		assertUserEquals(testData.get(0), result);
		assertEquals(new Long(1), API_USERS.count());
	}

	@Test
	public void testAddTwoEntities() throws Exception {
		Response response = API_USERS.add(testData.get(0));
		assertResponseStatus(Status.CREATED, response);

		response = API_USERS.add(testData.get(1));
		assertResponseStatus(Status.CREATED, response);

		List<User> results = API_USERS.get();
		assertNotNull(results);
		assertEquals(2, results.size());
		for (int i = 0; i < results.size(); i++) {
			assertUserEquals(testData.get(i), results.get(i));
		}

		assertEquals(new Long(2), API_USERS.count());
	}
}
