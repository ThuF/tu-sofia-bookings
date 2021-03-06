package tu.sofia.bookings.service;

import java.text.MessageFormat;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import tu.sofia.bookings.common.UnitOfWorkUtils;
import tu.sofia.bookings.dao.RoomPriceDao;
import tu.sofia.bookings.entity.Room;
import tu.sofia.bookings.entity.RoomPrice;
import tu.sofia.bookings.validation.RoomPriceValidator;
import tu.sofia.bookings.validation.ValidationErrorResponseBuilder;
import tu.sofia.bookings.validation.interfaces.IRoomPriceValidator;

/**
 * Service for payment
 */
@Singleton
@Path("/protected/admin/payment")
public class PaymentService {

	private static final String VALIDATION_MESSAGE_NO_ROOM_PRICE_FOUND_WITH_ROOM_ID = "No room price found with [roomId={0}]";
	private static final String VALIDATION_MESSAGE_THERE_IS_NO_PRICE_FOR_A_ROOM_WITH_ROOM_ID = "There is no price for a room with [roomId={0}]";

	private UnitOfWorkUtils unitOfWorkUtils;
	private IRoomPriceValidator roomPriceValidator;
	private RoomPriceDao roomPriceDao;
	private RoomService roomService;

	/**
	 * Constructor
	 *
	 * @param unitOfWorkUtils
	 * @param roomPriceValidator
	 * @param roomPriceDao
	 * @param roomService
	 */
	@Inject
	public PaymentService(UnitOfWorkUtils unitOfWorkUtils, RoomPriceValidator roomPriceValidator, RoomPriceDao roomPriceDao,
			RoomService roomService) {
		this.unitOfWorkUtils = unitOfWorkUtils;
		this.roomPriceValidator = roomPriceValidator;
		this.roomPriceDao = roomPriceDao;
		this.roomService = roomService;
	}

	/**
	 * Returns a list of all rooms prices
	 *
	 * @return list of all rooms prices
	 */
	@GET
	@Path("/rooms")
	@Produces(MediaType.APPLICATION_JSON)
	public List<RoomPrice> getRoomsPrices() {
		unitOfWorkUtils.begin();

		List<RoomPrice> roomsPrices = roomPriceDao.findAll();

		unitOfWorkUtils.end();
		return roomsPrices;
	}

	/**
	 * Return the price of a specific room
	 *
	 * @param id
	 * @return the price of a specific room
	 */
	@GET
	@Path("/rooms/{id}/price")
	@Produces(MediaType.TEXT_PLAIN)
	public Double getRoomPrice(@PathParam("id") Long id) {
		unitOfWorkUtils.begin();

		Double price = null;
		RoomPrice roomPrice = getRoomPriceEntity(id);
		if (roomPrice != null) {
			price = roomPrice.getPrice();
		} else {
			Room room = roomService.getRoomEntity(id);
			if (room != null) {
				price = room.getDefaultPricePerNight();
			} else {
				throw new NotFoundException(MessageFormat.format(VALIDATION_MESSAGE_THERE_IS_NO_PRICE_FOR_A_ROOM_WITH_ROOM_ID, id));
			}
		}

		unitOfWorkUtils.end();
		return price;
	}

	/**
	 * Returns the count of rooms prices
	 *
	 * @return the count of rooms prices
	 */
	@GET
	@Path("/rooms/count")
	@Produces(MediaType.TEXT_PLAIN)
	public Long count() {
		unitOfWorkUtils.begin();

		long count = roomPriceDao.countAll();

		unitOfWorkUtils.end();
		return count;
	}

	/**
	 * Adds new room price
	 *
	 * @param roomPrice
	 * @return HTTP 201 CREATED if the room price was successfully added or
	 *         HTTP 400 BAD REQUEST if something went wrong
	 */
	@POST
	@Path("/rooms")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addRoomPrice(RoomPrice roomPrice) {
		unitOfWorkUtils.begin();

		Response response = null;
		if (roomPriceValidator.isValid(roomPrice, roomPriceDao, roomService)) {
			roomPriceDao.create(roomPrice);
			response = Response.status(Status.CREATED).entity(roomPrice.getRoomId()).build();
		} else {
			response = ValidationErrorResponseBuilder.toResponse(roomPriceValidator);
		}

		unitOfWorkUtils.end();
		return response;
	}

	/**
	 * Updates existing room's price
	 *
	 * @param id
	 * @param price
	 * @return HTTP 204 NO CONTENT if the update was successful or
	 *         HTTP 404 NOT FOUND if there was no such room price
	 */
	@PUT
	@Path("/rooms/{id}/price")
	@Produces(MediaType.APPLICATION_JSON)
	public Response updatePrice(@PathParam("id") Long id, Double price) {
		unitOfWorkUtils.begin();

		Response response = null;
		RoomPrice persistedRoomPrice = roomPriceDao.findById(id);
		if (persistedRoomPrice != null) {
			persistedRoomPrice.setPrice(price);
			roomPriceDao.update(persistedRoomPrice);
			response = Response.status(Status.NO_CONTENT).build();
		} else {
			response = ValidationErrorResponseBuilder.toResponse(Status.NOT_FOUND,
					MessageFormat.format(VALIDATION_MESSAGE_NO_ROOM_PRICE_FOUND_WITH_ROOM_ID, id));
		}

		unitOfWorkUtils.end();
		return response;
	}

	/**
	 * Deletes specific room
	 *
	 * @param id
	 * @return HTTP 204 NO CONTENT if the deletion was successful or
	 *         HTTP 404 NOT FOUND if there was no such room price
	 */
	@DELETE
	@Path("/rooms/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteRoomPrice(@PathParam("id") Long id) {
		unitOfWorkUtils.begin();

		Response response = null;
		RoomPrice roomPrice = roomPriceDao.findById(id);
		if (roomPrice != null) {
			roomPriceDao.delete(roomPrice);
			response = Response.status(Status.NO_CONTENT).build();
		} else {
			response = ValidationErrorResponseBuilder.toResponse(Status.NOT_FOUND,
					MessageFormat.format(VALIDATION_MESSAGE_NO_ROOM_PRICE_FOUND_WITH_ROOM_ID, id));
		}

		unitOfWorkUtils.end();
		return response;
	}

	/**
	 * Return the room price entity
	 *
	 * @param id
	 * @return the room price entity
	 */
	public RoomPrice getRoomPriceEntity(Long id) {
		return roomPriceDao.findById(id);
	}
}
