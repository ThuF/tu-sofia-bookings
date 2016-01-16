package tu.sofia.bookings.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import tu.sofia.bookings.entity.key.RoomReviewKey;

/**
 * Entity class representing the T_ROOM_REVIEW database table
 */
@Entity
@Table(name = "T_ROOM_REVIEW")
@IdClass(RoomReviewKey.class)
@NamedQueries(@NamedQuery(name = RoomReview.QUERY_NAME_FIND_BY_ROOM_ID, query = RoomReview.QUERY_FIND_BY_ROOM_ID))
public class RoomReview implements Serializable {

	private static final long serialVersionUID = -3038006539103362713L;

	/**
	 * The name of a query for finding all reviews filtered by room Id
	 */
	public static final String QUERY_NAME_FIND_BY_ROOM_ID = "findByRoomId";

	/**
	 * The roomId parameter
	 */
	public static final String PARAM_ROOM_ID = "roomId";

	static final String QUERY_FIND_BY_ROOM_ID = "select r from RoomReview r where r.roomId = :" + PARAM_ROOM_ID;

	@Id
	private Long roomId;

	@Id
	@Column(length = 256)
	private String userId;

	private String userName;

	private Integer rating = 0;

	@Column(length = 4096)
	private String comment;

	@Temporal(TemporalType.DATE)
	@Column(nullable = false)
	private Date date = new Date();

	/**
	 * Returns the room Id
	 *
	 * @return the room Id
	 */
	public Long getRoomId() {
		return roomId;
	}

	/**
	 * Sets the room Id
	 *
	 * @param roomId
	 *            the room Id
	 */
	public void setRoomId(Long roomId) {
		this.roomId = roomId;
	}

	/**
	 * Returns the user Id
	 *
	 * @return the user Id
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * Sets the user Id
	 *
	 * @param userId
	 *            the user Id
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * Returns the user name
	 *
	 * @return the user name
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * Sets the user name
	 *
	 * @param userName
	 *            the user name
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * Returns the rating
	 *
	 * @return the rating
	 */
	public Integer getRating() {
		return rating;
	}

	/**
	 * Sets the rating
	 *
	 * @param rating
	 *            the rating
	 */
	public void setRating(Integer rating) {
		this.rating = rating;
	}

	/**
	 * Returns the comment
	 *
	 * @return the comment
	 */
	public String getComment() {
		return comment;
	}

	/**
	 * Sets the comment
	 *
	 * @param comment
	 *            the comment
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}

	/**
	 * Returns the date
	 * 
	 * @return the date
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * Sets the date
	 * 
	 * @param date
	 *            the date
	 */
	public void setDate(Date date) {
		this.date = date;
	}
}
