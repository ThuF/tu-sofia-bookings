package tu.sofia.bookings.service.proxy.publics;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import tu.sofia.bookings.entity.RoomReview;
import tu.sofia.bookings.entity.dto.RoomReviewDto;
import tu.sofia.bookings.service.RoomReviewService;

/**
 * Proxy service for rooms
 */
@Singleton
@Path("/public/reviews")
public class RoomReviewServicePublicProxy {

	private RoomReviewService roomReviewService;

	/**
	 * Constructor
	 *
	 * @param roomReviewService
	 */
	@Inject
	public RoomReviewServicePublicProxy(RoomReviewService roomReviewService) {
		this.roomReviewService = roomReviewService;
	}

	/**
	 * Returns a list of all rooms
	 *
	 * @param roomId
	 * @return list of all rooms
	 */
	@GET
	@Path("/room/{roomId}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<RoomReviewDto> getReviews(@PathParam("roomId") Long roomId) {
		List<RoomReviewDto> reviews = new ArrayList<RoomReviewDto>();
		for (RoomReview next : roomReviewService.getReviewsByRoomId(roomId)) {
			reviews.add(new RoomReviewDto(next));
		}
		return reviews;
	}

}
