package tu.sofia.bookings.service;

import java.text.MessageFormat;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import tu.sofia.bookings.common.UnitOfWorkUtils;
import tu.sofia.bookings.dao.RoomDao;
import tu.sofia.bookings.dao.RoomReviewDao;
import tu.sofia.bookings.dao.UserDao;
import tu.sofia.bookings.entity.RoomReview;
import tu.sofia.bookings.entity.User;
import tu.sofia.bookings.entity.key.RoomReviewKey;
import tu.sofia.bookings.validation.RoomReviewValidator;
import tu.sofia.bookings.validation.ValidationErrorResponseBuilder;
import tu.sofia.bookings.validation.interfaces.IRoomReviewValidator;

/**
 * Service for registering users
 */
@Singleton
@Path("/protected/admin/reviews")
public class RoomReviewService {

	private static final String ERROR_THERE_IS_ALREADY_A_REVIEW_MESSAGE = "There is already a review for [roomId={0}] from the current user";

	private static final String USER_NAME_ANONYMOUS = "Anonymous";

	private UnitOfWorkUtils unitOfWorkUtils;
	private IRoomReviewValidator roomReviewValidator;
	private RoomReviewDao roomReviewDao;
	private RoomDao roomDao;
	private UserDao userDao;

	/**
	 * Constructor
	 *
	 * @param unitOfWorkUtils
	 * @param roomReviewValidator
	 * @param roomReviewDao
	 * @param roomDao
	 * @param userDao
	 */
	@Inject
	public RoomReviewService(UnitOfWorkUtils unitOfWorkUtils, RoomReviewValidator roomReviewValidator, RoomReviewDao roomReviewDao, RoomDao roomDao,
			UserDao userDao) {
		this.unitOfWorkUtils = unitOfWorkUtils;
		this.roomReviewValidator = roomReviewValidator;
		this.roomReviewDao = roomReviewDao;
		this.roomDao = roomDao;
		this.userDao = userDao;
	}

	/**
	 * Returns a list of all rooms reviews
	 *
	 * @return list of all rooms reviews
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<RoomReview> getRoomReviews() {
		unitOfWorkUtils.begin();

		List<RoomReview> reviews = roomReviewDao.findAll();

		unitOfWorkUtils.end();
		return reviews;
	}

	/**
	 * Return the information about a room review for a specific user
	 *
	 * @param roomId
	 * @param userId
	 * @return the information about a room review for a specific user
	 */
	@GET
	@Path("/room/{roomId}/user/{userId}")
	@Produces(MediaType.APPLICATION_JSON)
	public RoomReview getRoomReview(@PathParam("roomId") Long roomId, @PathParam("userId") String userId) {
		unitOfWorkUtils.begin();

		RoomReview review = getRoomReviewEntity(new RoomReviewKey(roomId, userId));
		if (review == null) {
			throw new NotFoundException();
		}

		unitOfWorkUtils.end();
		return review;
	}

	/**
	 * Returns the count of rooms reviews
	 *
	 * @return the count of rooms reviews
	 */
	@GET
	@Path("/count")
	@Produces(MediaType.TEXT_PLAIN)
	public Long count() {
		unitOfWorkUtils.begin();

		long count = roomReviewDao.countAll();

		unitOfWorkUtils.end();
		return count;
	}

	/**
	 * Adds new room review
	 *
	 * @param roomReview
	 * @param request
	 * @return HTTP 200 OK if the room review was successfully added or
	 *         HTTP 400 if something went wrong
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addRoomReview(RoomReview roomReview, @Context HttpServletRequest request) {
		unitOfWorkUtils.begin();

		Response response = null;
		if (roomReviewValidator.isValid(roomReview, roomDao)) {
			setUserProperties(roomReview, request);
			if (roomReviewDao.findById(new RoomReviewKey(roomReview.getRoomId(), roomReview.getUserId())) == null) {
				roomReviewDao.create(roomReview);
				response = Response.status(Status.CREATED).entity(roomReview.getRoomId()).build();
			} else {
				response = ValidationErrorResponseBuilder.toResponse(Status.BAD_REQUEST,
						MessageFormat.format(ERROR_THERE_IS_ALREADY_A_REVIEW_MESSAGE, roomReview.getRoomId()));
			}
		} else {
			response = ValidationErrorResponseBuilder.toResponse(roomReviewValidator);
		}

		unitOfWorkUtils.end();
		return response;
	}

	private void setUserProperties(RoomReview roomReview, HttpServletRequest request) {
		User user = userDao.findById(request.getRemoteUser());
		roomReview.setUserId(user.getUserId());
		if ((user.getFirstName() != null) && (user.getLastName() != null)) {
			roomReview.setUserName(user.getFirstName() + " " + user.getLastName());
		} else {
			roomReview.setUserName(USER_NAME_ANONYMOUS);
		}
	}

	/**
	 * Updates existing room review
	 *
	 * @param roomId
	 * @param userId
	 * @param roomReview
	 * @return HTTP 201 NO CONTENT if the update was successful or
	 *         HTTP 404 NOT FOUND if there was no such room review
	 */
	@PUT
	@Path("/room/{roomId}/user/{userId}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateRoomReview(@PathParam("roomId") Long roomId, @PathParam("userId") String userId, RoomReview roomReview) {
		unitOfWorkUtils.begin();

		Response response = null;
		RoomReview persistedRoomReview = getRoomReviewEntity(new RoomReviewKey(roomId, userId));
		if (persistedRoomReview != null) {
			updateRoomReviewProperties(persistedRoomReview, roomReview);
			roomReviewDao.update(persistedRoomReview);
			response = Response.status(Status.NO_CONTENT).build();
		} else {
			response = Response.status(Status.NOT_FOUND).build();
		}

		unitOfWorkUtils.end();
		return response;
	}

	private void updateRoomReviewProperties(RoomReview persistedRoomReview, RoomReview roomReview) {
		persistedRoomReview.setComment(roomReview.getComment());
		persistedRoomReview.setRating(roomReview.getRating());
	}

	/**
	 * Deletes a room review of a specific user
	 *
	 * @param roomId
	 * @param userId
	 * @return HTTP 201 NO CONTENT if the deletion was successful or
	 *         HTTP 404 if there was no such room review
	 */
	@DELETE
	@Path("/room/{roomId}/user/{userId}")
	public Response deleteRoomReview(@PathParam("roomId") Long roomId, @PathParam("userId") String userId) {
		unitOfWorkUtils.begin();

		Response response = null;
		RoomReview roomReview = getRoomReviewEntity(new RoomReviewKey(roomId, userId));
		if (roomReview != null) {
			roomReviewDao.delete(roomReview);
			response = Response.status(Status.NO_CONTENT).build();
		} else {
			response = Response.status(Status.NOT_FOUND).build();
		}

		unitOfWorkUtils.end();
		return response;
	}

	/**
	 * Returns the room review entity
	 *
	 * @param id
	 * @return the room review entity
	 */
	public RoomReview getRoomReviewEntity(RoomReviewKey id) {
		return roomReviewDao.findById(id);
	}

	/**
	 * Returns a list of all reviews filtered by room Id
	 * 
	 * @param roomId
	 * @return a list of all reviews filtered by room Id
	 */
	public List<RoomReview> getReviewsByRoomId(Long roomId) {
		return roomReviewDao.findByRoomId(roomId);
	}
}
