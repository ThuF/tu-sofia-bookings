package tu.sofia.bookings.service.proxy.protecteds;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import tu.sofia.bookings.entity.RoomReview;
import tu.sofia.bookings.entity.key.RoomReviewKey;
import tu.sofia.bookings.service.RoomReviewService;

/**
 * Proxy service for rooms
 */
@Singleton
@Path("/protected/user/reviews")
public class RoomReviewServiceUserProxy {

	private RoomReviewService roomReviewService;

	/**
	 * Constructor
	 *
	 * @param roomReviewService
	 */
	@Inject
	public RoomReviewServiceUserProxy(RoomReviewService roomReviewService) {
		this.roomReviewService = roomReviewService;
	}

	/**
	 * Returns true if the currently logged in user has submitted a review for the specified room
	 * 
	 * @param roomId
	 * @param request
	 * @return true if the currently logged in user has submitted a review for the specified room
	 */
	@GET
	@Path("/room/{roomId}")
	public Boolean isReviewSubmited(@PathParam("roomId") Long roomId, @Context HttpServletRequest request) {
		return roomReviewService.getRoomReviewEntity(new RoomReviewKey(roomId, request.getRemoteUser())) != null;
	}

	/**
	 * Adds new room review
	 *
	 * @param review
	 * @param request
	 * @return HTTP 200 OK if the room review was successfully added or
	 *         HTTP 400 if something went wrong
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addReview(RoomReview review, @Context HttpServletRequest request) {
		return roomReviewService.addRoomReview(review, request);
	}

}
