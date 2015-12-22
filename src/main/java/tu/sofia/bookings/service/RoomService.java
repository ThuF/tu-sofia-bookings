package tu.sofia.bookings.service;

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
import tu.sofia.bookings.dao.RoomDao;
import tu.sofia.bookings.entity.Room;
import tu.sofia.bookings.validation.RoomValidator;
import tu.sofia.bookings.validation.ValidationErrorResponseBuilder;

/**
 * Service for registering users
 */
@Singleton
@Path("/rooms")
public class RoomService {

	private UnitOfWorkUtils unitOfWorkUtils;
	private RoomDao roomDao;
	private RoomValidator roomValidator;

	/**
	 * Constructor
	 *
	 * @param unitOfWorkUtils
	 * @param roomDao
	 * @param roomValidator
	 */
	@Inject
	public RoomService(UnitOfWorkUtils unitOfWorkUtils, RoomDao roomDao, RoomValidator roomValidator) {
		this.unitOfWorkUtils = unitOfWorkUtils;
		this.roomDao = roomDao;
		this.roomValidator = roomValidator;
	}

	/**
	 * Returns a list of all rooms
	 *
	 * @return list of all rooms
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Room> getRooms() {
		unitOfWorkUtils.begin();

		List<Room> rooms = roomDao.findAll();

		unitOfWorkUtils.end();
		return rooms;
	}

	/**
	 * Return the information about a specific room
	 *
	 * @param id
	 * @return the information about a specific room
	 */
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Room getRoom(@PathParam("id") Long id) {
		unitOfWorkUtils.begin();

		Room room = roomDao.findById(id);
		if (room == null) {
			throw new NotFoundException();
		}

		unitOfWorkUtils.end();
		return room;
	}

	/**
	 * Returns the count of rooms
	 *
	 * @return the count of rooms
	 */
	@GET
	@Path("/count")
	@Produces(MediaType.TEXT_PLAIN)
	public Long count() {
		unitOfWorkUtils.begin();

		long count = roomDao.countAll();

		unitOfWorkUtils.end();
		return count;
	}

	/**
	 * Adds new room
	 *
	 * @param room
	 * @return HTTP 200 OK if the room was successfully added or
	 *         HTTP 400 if something went wrong
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addRoom(Room room) {
		unitOfWorkUtils.begin();

		Response response = null;
		if (roomValidator.isValid(room)) {
			roomDao.create(room);
			response = Response.status(Status.CREATED).entity(room.getRoomId()).build();
		} else {
			response = ValidationErrorResponseBuilder.toResponse(roomValidator);
		}

		unitOfWorkUtils.end();
		return response;
	}

	/**
	 * Updates existing room
	 *
	 * @param id
	 * @param room
	 * @return HTTP 201 NO CONTENT if the update was successful or
	 *         HTTP 404 NOT FOUND if there was no such room
	 */
	@PUT
	@Path("/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateRoom(@PathParam("id") Long id, Room room) {
		unitOfWorkUtils.begin();

		Response response = null;
		if (roomValidator.isValid(room)) {
			Room persistedRoom = roomDao.findById(id);
			if (persistedRoom != null) {
				updateRoomProperties(persistedRoom, room);
				roomDao.update(persistedRoom);
				response = Response.status(Status.NO_CONTENT).build();
			} else {
				response = Response.status(Status.NOT_FOUND).build();
			}
		} else {
			response = ValidationErrorResponseBuilder.toResponse(roomValidator);
		}

		unitOfWorkUtils.end();
		return response;
	}

	private void updateRoomProperties(Room persistedRoom, Room room) {
		persistedRoom.setRoomType(room.getRoomType());
		persistedRoom.setRoomView(room.getRoomView());
		persistedRoom.setBedType(room.getBedType());
		persistedRoom.setDescription(room.getDescription());
	}

	/**
	 * Deletes specific room
	 *
	 * @param id
	 * @return HTTP 201 NO CONTENT if the deletion was successful or
	 *         HTTP 404 if there was no such room
	 */
	@DELETE
	@Path("/{id}")
	public Response deleteUser(@PathParam("id") Long id) {
		unitOfWorkUtils.begin();

		Response response = null;
		Room room = roomDao.findById(id);
		if (room != null) {
			roomDao.delete(room);
			response = Response.noContent().build();
		} else {
			response = Response.status(Status.NOT_FOUND).build();
		}

		unitOfWorkUtils.end();
		return response;
	}
}
