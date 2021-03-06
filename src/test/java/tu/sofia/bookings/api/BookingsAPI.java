package tu.sofia.bookings.api;

import java.util.List;

import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.PUT;
import retrofit.http.Path;
import tu.sofia.bookings.entity.Booking;
import tu.sofia.bookings.entity.additional.BookingStatus;
import tu.sofia.bookings.entity.additional.PaymentStatus;

@SuppressWarnings("javadoc")
public interface BookingsAPI {

	@GET("/protected/admin/bookings")
	List<Booking> get();

	@GET("/protected/admin/bookings/{id}")
	Booking get(@Path("id") final Long id);

	@GET("/protected/admin/bookings/count")
	Long count();

	@PUT("/protected/admin/bookings/{id}/booking/status")
	Response updateBookingStatus(@Path("id") final Long id, @Body final BookingStatus body);

	@PUT("/protected/admin/bookings/{id}/payment/status")
	Response updatePaymentStatus(@Path("id") final Long id, @Body final PaymentStatus body);

	@DELETE("/protected/admin/bookings/{id}")
	Response remove(@Path("id") final Long id);
}
