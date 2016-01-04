package tu.sofia.bookings.integration.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import javax.ws.rs.core.Response.Status;

import org.junit.After;
import org.junit.Before;

import retrofit.client.Response;
import tu.sofia.bookings.api.UserProfilesAPI;
import tu.sofia.bookings.api.UserRegistrationsAPI;
import tu.sofia.bookings.api.UsersAPI;
import tu.sofia.bookings.entity.User;
import tu.sofia.bookings.integration.IntegrationTestSupport;
import tu.sofia.bookings.integration.UserRole;

@SuppressWarnings("javadoc")
public abstract class AbstractUsersIntegrationTest extends IntegrationTestSupport {

	protected UsersAPI API_USERS;
	protected UserProfilesAPI API_USER_PROFILES;
	protected UserRegistrationsAPI API_USER_REGISTRATION;

	@Before
	public void setUp() throws Exception {
		API_USERS = createRestAdapter().create(UsersAPI.class);
		API_USER_PROFILES = createRestAdapter().create(UserProfilesAPI.class);
		API_USER_REGISTRATION = createRestAdapter().create(UserRegistrationsAPI.class);
		login(loginAs());
	}

	@After
	public void cleanUp() throws Exception {
		logout();
		login(UserRole.ADMIN);
		removeUsers();
		logout();
	}

	protected abstract UserRole loginAs();

	protected void removeUsers() {
		for (User next : API_USERS.get()) {
			Response response = API_USERS.remove(next.getUserId());
			assertResponseStatus(Status.NO_CONTENT, response);
		}
		assertEquals(new Long(0), API_USERS.count());
	}

	protected void assertUsersCountEquals(Long expectedCount) {
		logout();
		login(UserRole.ADMIN);
		assertEquals(expectedCount, API_USERS.count());
		logout();
		login(loginAs());
	}

	protected void assertUserEquals(User expected, User actual) {
		assertNotNull(expected);
		assertEquals(expected.getUserId(), actual.getUserId());
		assertEquals(expected.getFirstName(), actual.getFirstName());
		assertEquals(expected.getLastName(), actual.getLastName());
		assertEquals(expected.getEmail(), actual.getEmail());
	}
}
