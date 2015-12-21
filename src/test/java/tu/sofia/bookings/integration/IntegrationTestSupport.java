package tu.sofia.bookings.integration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import javax.ws.rs.core.Response.Status;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RestAdapter.LogLevel;
import retrofit.RetrofitError;
import retrofit.client.Header;
import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.Headers;

@SuppressWarnings("javadoc")
public abstract class IntegrationTestSupport {

	public static final String ENDPOINT = "http://localhost:" + System.getProperty("local.server.http.port") + "/backend/";
	public static final String ENDPOINT_API = ENDPOINT + "api/v1";

	private static final StringBuilder cookies = new StringBuilder();
	private static final LogLevel logLevel = RestAdapter.LogLevel.NONE;

	protected RestAdapter createRestAdapter() {
		return new RestAdapter.Builder().setRequestInterceptor(new RequestInterceptorHandler()).setEndpoint(ENDPOINT_API).setLogLevel(logLevel)
				.build();
	}

	protected void login(UserRole userRole) {
		RestAdapter logingRestAdapter = new RestAdapter.Builder().setEndpoint(ENDPOINT_API).setLogLevel(logLevel).build();
		Response response = null;
		switch (userRole) {
			case EVERYONE:
				response = loginAsEveryone(logingRestAdapter);
				break;
			case ADMIN:
				response = loginAsAdminUser(logingRestAdapter);
				break;
		}
		saveCookies(response);
	}

	protected void logout() {
		cookies.delete(0, cookies.length());
	}

	protected void assertResponseStatus(Status expectedStatus, Response response) {
		assertEquals(expectedStatus.getStatusCode(), response.getStatus());
	}

	private Response loginAsEveryone(RestAdapter logingRestAdapter) {
		Response response;
		try {
			EveryoneLoginService login = logingRestAdapter.create(EveryoneLoginService.class);
			// try to authenticate
			response = login.authenticate();
		} catch (RetrofitError e) {
			response = e.getResponse();
		}
		return response;
	}

	private Response loginAsAdminUser(RestAdapter logingRestAdapter) {
		Response response;
		try {
			AdminUserLoginService login = logingRestAdapter.create(AdminUserLoginService.class);
			// try to authenticate
			response = login.authenticate();
		} catch (RetrofitError e) {
			response = e.getResponse();
		}
		return response;
	}

	private static void saveCookies(final Response response) {
		assertNotNull("Response should not be null when saving cookies. Check request", response);
		List<Header> headers = response.getHeaders();
		for (final Header header : headers) {
			if ((header.getName() != null) && header.getName().equals("Set-Cookie")) {
				cookies.append(header.getValue());
				cookies.append(';');
			}
		}
	}

	private static class RequestInterceptorHandler implements RequestInterceptor {

		@Override
		public void intercept(final RequestFacade request) {
			request.addHeader("Cookie", cookies.toString());
		}
	}

	protected interface EveryoneLoginService {
		String username = "user";

		/**
		 * Authenticates with user name: user, password: user. Use any
		 * Base64Encoder("user:user")
		 **/
		@GET("/")
		@Headers({ "Accept: application/json", "Authorization: Basic dXNlcjp1c2Vy" })
		Response authenticate();
	}

	protected interface AdminUserLoginService {
		String username = "admin";

		/**
		 * Authenticates with user name: support-user, password: support. Use any
		 * Base64Encoder("support-user:support")
		 **/
		@GET("/")
		@Headers({ "Accept: application/json", "Authorization: Basic YWRtaW46YWRtaW4=" })
		Response authenticate();
	}
}
