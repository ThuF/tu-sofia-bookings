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
import tu.sofia.bookings.entity.RoomPrice;

@SuppressWarnings("javadoc")
public interface PaymentAPI {

	@GET("/protected/admin/payment/rooms")
	@Headers("Accept: application/json")
	List<RoomPrice> get();

	@GET("/protected/admin/payment/rooms/{id}/price")
	Double getPrice(@Path("id") final Long id);

	@GET("/protected/admin/payment/rooms/count")
	Long count();

	@POST("/protected/admin/payment/rooms")
	@Headers("Accept: application/json")
	Response add(@Body final RoomPrice body);

	@PUT("/protected/admin/payment/rooms/{id}/price")
	Response updatePrice(@Path("id") final Long id, @Body final Double body);

	@DELETE("/protected/admin/payment/rooms/{id}")
	Response remove(@Path("id") final Long id);
}
