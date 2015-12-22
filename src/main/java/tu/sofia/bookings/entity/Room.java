package tu.sofia.bookings.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import tu.sofia.bookings.entity.additional.BedType;
import tu.sofia.bookings.entity.additional.RoomType;
import tu.sofia.bookings.entity.additional.RoomView;

/**
 * Entity class representing the T_ROOM database table
 */
@Entity
@Table(name = "T_ROOM")
public class Room implements Serializable {

	private static final long serialVersionUID = 1370127930303639423L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long roomId;

	@Column(nullable = false)
	private RoomType roomType;

	@Column(nullable = false)
	private RoomView roomView;

	@Column(nullable = false)
	private BedType bedType;

	@Column(length = 1024)
	private String description;

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
	 * Returns the room type
	 *
	 * @return the room type
	 */
	public RoomType getRoomType() {
		return roomType;
	}

	/**
	 * Sets the room type
	 *
	 * @param roomType
	 *            the room type
	 */
	public void setRoomType(RoomType roomType) {
		this.roomType = roomType;
	}

	/**
	 * Returns the room view
	 *
	 * @return the room view
	 */
	public RoomView getRoomView() {
		return roomView;
	}

	/**
	 * Sets the room view
	 *
	 * @param roomView
	 *            the room view
	 */
	public void setRoomView(RoomView roomView) {
		this.roomView = roomView;
	}

	/**
	 * Returns the bed type
	 *
	 * @return the bed type
	 */
	public BedType getBedType() {
		return bedType;
	}

	/**
	 * Sets the bed type
	 *
	 * @param bedType
	 *            the bed type
	 */
	public void setBedType(BedType bedType) {
		this.bedType = bedType;
	}

	/**
	 * Returns the description
	 *
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the description
	 *
	 * @param description
	 *            the description
	 */
	public void setDescription(String description) {
		this.description = description;
	}
}
