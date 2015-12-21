package tu.sofia.bookings.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response.Status;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import retrofit.client.Response;
import tu.sofia.bookings.api.UsersAPI;
import tu.sofia.bookings.entity.User;
import tu.sofia.bookings.integration.IntegrationTestSupport;
import tu.sofia.bookings.integration.UserRole;

@SuppressWarnings("javadoc")
public class UserIntegrationTest extends IntegrationTestSupport {

	private static final List<User> testData = new ArrayList<User>();
	private UsersAPI API;

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

	@Before
	public void setUp() throws Exception {
		API = createRestAdapter().create(UsersAPI.class);
		login(UserRole.ADMIN);
	}

	@After
	public void cleanUp() throws Exception {
		logout();
		login(UserRole.ADMIN);

		for (User next : API.get()) {
			Response response = API.remove(next.getUserId());
			assertResponseStatus(Status.NO_CONTENT, response);
		}
		assertEquals(new Long(0), API.count());

		logout();
	}

	@Test
	public void testEmpty() throws Exception {
		List<User> results = API.get();
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
	public void testUpdateEntity() throws Exception {
		Response response = API.add(testData.get(0));
		assertResponseStatus(Status.CREATED, response);

		response = API.update(testData.get(0).getUserId(), testData.get(1));
		assertResponseStatus(Status.NO_CONTENT, response);

		List<User> results = API.get();
		assertEquals(1, results.size());
		assertEquals(new Long(1), API.count());
	}

	@Test
	public void testRemoveEntity() throws Exception {
		Response response = API.add(testData.get(0));
		assertResponseStatus(Status.CREATED, response);

		response = API.remove(testData.get(0).getUserId());
		assertResponseStatus(Status.NO_CONTENT, response);
		assertEquals(new Long(0), API.count());
	}

	@Test
	public void testGetSingleEntity() throws Exception {
		Response response = API.add(testData.get(0));
		assertResponseStatus(Status.CREATED, response);

		User result = API.get(testData.get(0).getUserId());

		assertNotNull(result);
		assertEquals(testData.get(0).getUserId(), result.getUserId());
		assertEquals(testData.get(0).getFirstName(), result.getFirstName());
		assertEquals(new Long(1), API.count());
	}

	@Test
	public void testAddTwoEntities() throws Exception {
		Response response = API.add(testData.get(0));
		assertResponseStatus(Status.CREATED, response);

		response = API.add(testData.get(1));
		assertResponseStatus(Status.CREATED, response);

		List<User> results = API.get();
		assertNotNull(results);

		assertEquals(2, results.size());
		assertEquals(testData.get(0).getUserId(), results.get(0).getUserId());
		assertEquals(testData.get(0).getFirstName(), results.get(0).getFirstName());
		assertEquals(testData.get(1).getUserId(), results.get(1).getUserId());
		assertEquals(testData.get(1).getFirstName(), results.get(1).getFirstName());
		assertEquals(new Long(2), API.count());
	}
}
