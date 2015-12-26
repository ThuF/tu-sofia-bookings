package tu.sofia.bookings.api;

import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.Headers;
import retrofit.http.POST;
import tu.sofia.bookings.entity.Booking;

@SuppressWarnings("javadoc")
public interface BookAPI {

	@POST("/protected/user/book")
	@Headers("Accept: application/json")
	Response add(@Body final Booking body);
}
