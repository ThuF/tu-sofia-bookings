package tu.sofia.bookings.api;

import java.util.List;

import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.PUT;
import retrofit.http.Path;
import tu.sofia.bookings.entity.Booking;
import tu.sofia.bookings.entity.additional.BookingStatus;
import tu.sofia.bookings.entity.additional.PaymentStatus;

@SuppressWarnings("javadoc")
public interface BookingsAPI {

	@GET("/entities/bookings")
	@Headers("Accept: application/json")
	List<Booking> get();

	@GET("/entities/bookings/{id}")
	@Headers("Accept: application/json")
	Booking get(@Path("id") final Long id);

	@GET("/entities/bookings/count")
	Long count();

	@PUT("/entities/bookings/{id}/booking/status")
	@Headers("Accept: application/json")
	Response updateBookingStatus(@Path("id") final Long id, @Body final BookingStatus body);

	@PUT("/entities/bookings/{id}/payment/status")
	@Headers("Accept: application/json")
	Response updatePaymentStatus(@Path("id") final Long id, @Body final PaymentStatus body);

	@DELETE("/entities/bookings/{id}")
	@Headers("Accept: application/json")
	Response remove(@Path("id") final Long id);
}
