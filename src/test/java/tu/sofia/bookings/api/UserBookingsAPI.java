package tu.sofia.bookings.api;

import java.util.List;

import retrofit.http.GET;
import retrofit.http.Path;
import tu.sofia.bookings.entity.Booking;

@SuppressWarnings("javadoc")
public interface UserBookingsAPI {

	@GET("/protected/user/bookings")
	List<Booking> get();

	@GET("/protected/user/bookings/{id}")
	Booking get(@Path("id") final Long id);
}
