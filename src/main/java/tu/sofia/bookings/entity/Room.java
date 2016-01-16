package tu.sofia.bookings.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import tu.sofia.bookings.entity.additional.BedType;
import tu.sofia.bookings.entity.additional.RoomType;
import tu.sofia.bookings.entity.additional.RoomView;

/**
 * Entity class representing the T_ROOM database table
 */
@Entity
@Table(name = "T_ROOM")
@NamedQueries({ @NamedQuery(name = Room.QUERY_NAME_FIND_ALL_EXCEPT, query = Room.QUERY_FIND_ALL_EXCEPT) })
public class Room implements Serializable {

	/**
	 * The name of a query for finding all rooms except the chosen one
	 */
	public static final String QUERY_NAME_FIND_ALL_EXCEPT = "findAllExcept";

	/**
	 * The roomIds parameter
	 */
	public static final String PARAM_ROOM_IDS = "roomIds";

	static final String QUERY_FIND_ALL_EXCEPT = "select r from Room r where r.roomId not in :" + PARAM_ROOM_IDS;

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

	@Column(nullable = false)
	private Double defaultPricePerNight = 0.0;

	@Column(length = 4096)
	private String description;

	private List<String> imagesUrl;

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
	 * Returns the default price per night
	 *
	 * @return the default price per night
	 */
	public Double getDefaultPricePerNight() {
		return defaultPricePerNight;
	}

	/**
	 * Sets the default price per night
	 *
	 * @param defaultPrice
	 */
	public void setDefaultPricePerNight(Double defaultPrice) {
		this.defaultPricePerNight = defaultPrice;
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

	/**
	 * Returns the images url
	 *
	 * @return the images url
	 */
	public List<String> getImagesUrl() {
		if (imagesUrl == null) {
			imagesUrl = new ArrayList<String>();
		}
		return imagesUrl;
	}

	/**
	 * Sets the images url
	 *
	 * @param imagesUrl
	 *            the images url
	 */
	public void setImagesUrl(List<String> imagesUrl) {
		this.imagesUrl = imagesUrl;
	}
}
