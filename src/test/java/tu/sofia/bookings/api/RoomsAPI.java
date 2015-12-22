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
import tu.sofia.bookings.entity.Room;

@SuppressWarnings("javadoc")
public interface RoomsAPI {

	@GET("/entities/rooms")
	@Headers("Accept: application/json")
	List<Room> get();

	@GET("/entities/rooms/{id}")
	@Headers("Accept: application/json")
	Room get(@Path("id") final Long id);

	@GET("/entities/rooms/count")
	Long count();

	@POST("/entities/rooms")
	@Headers("Accept: application/json")
	Response add(@Body final Room body);

	@PUT("/entities/rooms/{id}")
	@Headers("Accept: application/json")
	Response update(@Path("id") final Long id, @Body final Room body);

	@DELETE("/entities/rooms/{id}")
	@Headers("Accept: application/json")
	Response remove(@Path("id") final Long id);
}
