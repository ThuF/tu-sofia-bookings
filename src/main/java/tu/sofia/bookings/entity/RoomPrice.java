package tu.sofia.bookings.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Entity class representing the T_ROOM_PRICE database table
 */
@Entity
@Table(name = "T_ROOM_PRICE")
public class RoomPrice implements Serializable {

	private static final long serialVersionUID = 4969339263331724303L;

	// TODO Use composite key - roomId - startDate - endDate
	@Id
	private Long roomId;

	@Column(nullable = false)
	private Double price;

	/**
	 * Returns the room id
	 *
	 * @return the room id
	 */
	public Long getRoomId() {
		return roomId;
	}

	/**
	 * Sets the room id
	 *
	 * @param roomId
	 *            the room id
	 */
	public void setRoomId(Long roomId) {
		this.roomId = roomId;
	}

	/**
	 * Returns the price
	 *
	 * @return the price
	 */
	public Double getPrice() {
		return price;
	}

	/**
	 * Sets the price
	 *
	 * @param price
	 *            the price
	 */
	public void setPrice(Double price) {
		this.price = price;
	}
}
