package tu.sofia.bookings.api;

import java.util.List;

import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.Path;
import tu.sofia.bookings.entity.Booking;

@SuppressWarnings("javadoc")
public interface UserBookingsAPI {

	@GET("/protected/user/bookings")
	@Headers("Accept: application/json")
	List<Booking> get();

	@GET("/protected/user/bookings/{id}")
	@Headers("Accept: application/json")
	Booking get(@Path("id") final Long id);
}
