package tu.sofia.bookings.api;

import retrofit.client.Response;
import retrofit.http.POST;

@SuppressWarnings("javadoc")
public interface UserRegistrationsAPI {

	@POST("/protected/user/register")
	Response add();
}
