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
import tu.sofia.bookings.entity.RoomReview;

@SuppressWarnings("javadoc")
public interface RoomReviewAPI {

	@GET("/protected/admin/reviews")
	List<RoomReview> get();

	@GET("/protected/admin/reviews/room/{roomId}/user/{userId}")
	RoomReview get(@Path("roomId") final Long roomId, @Path("userId") final String userId);

	@GET("/protected/admin/reviews/count")
	Long count();

	@POST("/protected/admin/reviews")
	@Headers("Accept: application/json")
	Response add(@Body final RoomReview roomReview);

	@PUT("/protected/admin/reviews/room/{roomId}/user/{userId}")
	Response update(@Path("roomId") final Long roomId, @Path("userId") final String userId, @Body final RoomReview body);

	@DELETE("/protected/admin/reviews/room/{roomId}/user/{userId}")
	Response remove(@Path("roomId") final Long roomId, @Path("userId") final String userId);
}
