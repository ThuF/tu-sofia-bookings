package tu.sofia.bookings.api;

import java.util.List;

import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;
import tu.sofia.bookings.entity.User;

@SuppressWarnings("javadoc")
public interface UsersAPI {

	@GET("/entities/users")
	@Headers("Accept: application/json")
	List<User> get();

	@GET("/entities/users/{id}")
	@Headers("Accept: application/json")
	User get(@Path("id") final String id);

	@GET("/entities/users/count")
	Long count();

	@POST("/entities/users")
	@Headers("Accept: application/json")
	Response add(@Body final User body);

	@PUT("/entities/users/{id}")
	@Headers("Accept: application/json")
	Response update(@Path("id") final String id, @Body final User body);

	@DELETE("/entities/users/{id}")
	@Headers("Accept: application/json")
	Response remove(@Path("id") final String id);
}
