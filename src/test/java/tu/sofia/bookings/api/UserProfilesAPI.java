package tu.sofia.bookings.api;

import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.PUT;
import tu.sofia.bookings.entity.User;

@SuppressWarnings("javadoc")
public interface UserProfilesAPI {

	@GET("/protected/user/profile")
	User get();

	@PUT("/protected/user/profile")
	Response update(@Body final User body);
}
