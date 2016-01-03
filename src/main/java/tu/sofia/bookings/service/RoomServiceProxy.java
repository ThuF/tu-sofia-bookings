package tu.sofia.bookings.service;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import tu.sofia.bookings.entity.Room;

/**
 * Proxy service for rooms
 */
@Singleton
@Path("/public/rooms")
public class RoomServiceProxy {

	private RoomService roomService;

	/**
	 * Constructor
	 *
	 * @param roomService
	 */
	@Inject
	public RoomServiceProxy(RoomService roomService) {
		this.roomService = roomService;
	}

	/**
	 * Returns a list of all rooms
	 *
	 * @return list of all rooms
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Room> getRooms() {
		return roomService.getRooms();
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
		return roomService.getRoom(id);
	}

	/**
	 * Returns a list of all available rooms in the selected period
	 *
	 * @param startDate
	 * @param endDate
	 * @return list of all available rooms in the selected period
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/available/startdate/{startDate}/enddate/{endDate}")
	public List<Room> getAvailableRooms(@PathParam("startDate") Long startDate, @PathParam("endDate") Long endDate) {
		return roomService.getAvailableRooms(startDate, endDate);
	}

}
