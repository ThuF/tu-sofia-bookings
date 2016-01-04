package tu.sofia.bookings.integration.services;

import javax.ws.rs.core.Response.Status;

import org.junit.Test;

import retrofit.RetrofitError;
import retrofit.client.Response;
import tu.sofia.bookings.entity.User;
import tu.sofia.bookings.integration.UserRole;

@SuppressWarnings("javadoc")
public class UserRegistrationIntegrationTest extends AbstractUsersIntegrationTest {

	@Override
	protected UserRole loginAs() {
		return UserRole.EVERYONE;
	}

	@Test
	public void testAddEntity() throws Exception {
		User expectedUser = new User();
		expectedUser.setUserId(EveryoneLoginService.username);

		Response response = API_USER_REGISTRATION.add();
		assertResponseStatus(Status.CREATED, response);

		assertUsersCountEquals(new Long(1));

		User actualUser = API_USER_PROFILES.get();
		assertUserEquals(expectedUser, actualUser);
	}

	@Test
	public void testAddEntityFromDifferentUsers() throws Exception {
		User expectedUser = new User();
		expectedUser.setUserId(EveryoneLoginService.username);

		Response response = API_USER_REGISTRATION.add();
		assertResponseStatus(Status.CREATED, response);

		User actualUser = API_USER_PROFILES.get();
		assertUserEquals(expectedUser, actualUser);

		logout();
		login(UserRole.ADMIN);

		response = API_USER_REGISTRATION.add();
		assertResponseStatus(Status.CREATED, response);

		expectedUser.setUserId(AdminUserLoginService.username);
		actualUser = API_USER_PROFILES.get();
		assertUserEquals(expectedUser, actualUser);

		assertUsersCountEquals(new Long(2));

	}

	@Test
	public void testAddEntityTwiceShouldResultInBadRequest() throws Exception {
		Response response = API_USER_REGISTRATION.add();
		assertResponseStatus(Status.CREATED, response);

		try {
			API_USER_REGISTRATION.add();
		} catch (RetrofitError e) {
			assertResponseStatus(Status.BAD_REQUEST, e.getResponse());
		}

		assertUsersCountEquals(new Long(1));
	}

}
