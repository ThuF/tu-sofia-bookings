package tu.sofia.bookings.entity.key;

import java.io.Serializable;

/**
 * Composite key for the T_ROOM_REVIEW table
 */
public class RoomReviewKey implements Serializable {

	private static final long serialVersionUID = 1207391386097418284L;

	private Long roomId;

	private String userId;

	/**
	 * Constructor
	 *
	 * @param roomId
	 * @param userId
	 */
	public RoomReviewKey(Long roomId, String userId) {
		this.roomId = roomId;
		this.userId = userId;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (prime * result) + ((roomId == null) ? 0 : roomId.hashCode());
		result = (prime * result) + ((userId == null) ? 0 : userId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof RoomReviewKey)) {
			return false;
		}
		RoomReviewKey other = (RoomReviewKey) obj;
		if (roomId == null) {
			if (other.roomId != null) {
				return false;
			}
		} else if (!roomId.equals(other.roomId)) {
			return false;
		}
		if (userId == null) {
			if (other.userId != null) {
				return false;
			}
		} else if (!userId.equals(other.userId)) {
			return false;
		}
		return true;
	}
}
