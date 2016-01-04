package tu.sofia.bookings.integration.services;

import javax.ws.rs.core.Response.Status;

import org.junit.Test;

import retrofit.RetrofitError;
import retrofit.client.Response;
import tu.sofia.bookings.entity.User;
import tu.sofia.bookings.integration.UserRole;

@SuppressWarnings("javadoc")
public class UserProfilesIntegrationTest extends AbstractUsersIntegrationTest {

	@Override
	protected UserRole loginAs() {
		return UserRole.EVERYONE;
	}

	@Test
	public void testEmpty() throws Exception {
		try {
			API_USER_PROFILES.get();
		} catch (RetrofitError e) {
			assertResponseStatus(Status.NOT_FOUND, e.getResponse());
		}
	}

	@Test
	public void testAddEntity() throws Exception {
		User expectedUser = new User();
		expectedUser.setUserId(EveryoneLoginService.username);

		Response response = API_USER_REGISTRATION.add();
		assertResponseStatus(Status.CREATED, response);

		User actualUser = API_USER_PROFILES.get();
		assertUserEquals(expectedUser, actualUser);
	}

	@Test
	public void testAddEntityAndLoginWithDifferentUserShouldReturnEmpty() throws Exception {
		testAddEntity();
		logout();
		login(UserRole.ADMIN);

		try {
			API_USER_PROFILES.get();
		} catch (RetrofitError e) {
			assertResponseStatus(Status.NOT_FOUND, e.getResponse());
		}
	}

	@Test
	public void testUpdateEntity() throws Exception {
		User expectedUser = new User();
		expectedUser.setUserId(EveryoneLoginService.username);
		expectedUser.setFirstName("Yordan");
		expectedUser.setLastName("Pavlov");

		Response response = API_USER_REGISTRATION.add();
		assertResponseStatus(Status.CREATED, response);

		response = API_USER_PROFILES.update(expectedUser);
		assertResponseStatus(Status.NO_CONTENT, response);

		User actualUser = API_USER_PROFILES.get();
		assertUserEquals(expectedUser, actualUser);
	}
}
