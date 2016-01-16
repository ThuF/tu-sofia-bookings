package tu.sofia.bookings.entity.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import tu.sofia.bookings.entity.RoomReview;

/**
 * Data Transfer Object for the RoomReview entity
 */
public class RoomReviewDto implements Serializable {

	private static final long serialVersionUID = 7163006927294898554L;

	private Long roomId;

	private String userName;

	private Integer rating;

	private String comment;

	private Long daysBefore;

	/**
	 * Constructor
	 *
	 * @param roomReview
	 */
	public RoomReviewDto(RoomReview roomReview) {
		this.roomId = roomReview.getRoomId();
		this.userName = roomReview.getUserName();
		this.rating = roomReview.getRating();
		this.comment = roomReview.getComment();
		this.daysBefore = TimeUnit.DAYS.convert((new Date().getTime() - roomReview.getDate().getTime()), TimeUnit.MILLISECONDS);
	}

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
	 * Returns the days before
	 *
	 * @return the days before
	 */
	public Long getDaysBefore() {
		return daysBefore;
	}

	/**
	 * Sets the days before
	 *
	 * @param daysBefore
	 */
	public void setDaysBefore(Long daysBefore) {
		this.daysBefore = daysBefore;
	}

}
