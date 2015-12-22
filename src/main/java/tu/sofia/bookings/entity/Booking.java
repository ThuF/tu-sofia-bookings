package tu.sofia.bookings.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Entity class representing the T_BOOKING database table
 */
@Entity
@Table(name = "T_BOOKING")
public class Booking implements Serializable {

	private static final long serialVersionUID = 4930072322837450442L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long bookingId;

	@Temporal(TemporalType.DATE)
	@Column(nullable = false)
	private Date startDate;

	@Temporal(TemporalType.DATE)
	@Column(nullable = false)
	private Date endDate;

	@Column(nullable = false)
	private BookingStatus bookingStatus = BookingStatus.PROCESSING;

	@Column(nullable = false)
	private PaymentStatus paymentStatus = PaymentStatus.UNPAID;

	@Column(nullable = false)
	private Double paymentAmount;

	@Temporal(TemporalType.DATE)
	private Date paymentDate;

	@OneToMany
	private List<User> users;

	@OneToMany
	private List<Room> rooms;

	/**
	 * Returns the booking id
	 *
	 * @return the booking id
	 */
	public Long getBookingId() {
		return bookingId;
	}

	/**
	 * Sets the booking id
	 *
	 * @param bookingId
	 *            the booking id
	 */
	public void setBookingId(Long bookingId) {
		this.bookingId = bookingId;
	}

	/**
	 * Returns the start date of the booking
	 *
	 * @return the start date of the booking
	 */
	public Date getStartDate() {
		return startDate;
	}

	/**
	 * Sets the start date of the booking
	 *
	 * @param startDate
	 *            the start date of the booking
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	/**
	 * Returns the end date of the booking
	 *
	 * @return the end date of the booking
	 */
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * Sets the end date of the booking
	 *
	 * @param endDate
	 *            the end date of the booking
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	/**
	 * Returns the booking status
	 *
	 * @return the booking status
	 */
	public BookingStatus getBookingStatus() {
		return bookingStatus;
	}

	/**
	 * Sets the booking status
	 *
	 * @param bookingStatus
	 *            the booking status
	 */
	public void setBookingStatus(BookingStatus bookingStatus) {
		this.bookingStatus = bookingStatus;
	}

	/**
	 * Returns the payment status of the booking
	 *
	 * @return the payment status of the booking
	 */
	public PaymentStatus getPaymentStatus() {
		return paymentStatus;
	}

	/**
	 * Sets the payment status of the booking
	 *
	 * @param paymentStatus
	 *            the payment status of the booking
	 */
	public void setPaymentStatus(PaymentStatus paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	/**
	 * Returns the payment amount of the booking
	 *
	 * @return the payment amount of the booking
	 */
	public Double getPaymentAmount() {
		return paymentAmount;
	}

	/**
	 * Sets the payment amount of the booking
	 *
	 * @param paymentAmount
	 *            the payment amount of the booking
	 */
	public void setPaymentAmount(Double paymentAmount) {
		this.paymentAmount = paymentAmount;
	}

	/**
	 * Returns the payment date of the booking
	 *
	 * @return the payment date of the booking
	 */
	public Date getPaymentDate() {
		return paymentDate;
	}

	/**
	 * Sets the payment date of the booking
	 *
	 * @param paymentDate
	 *            the payment date of the booking
	 */
	public void setPaymentDate(Date paymentDate) {
		this.paymentDate = paymentDate;
	}

	/**
	 * Returns the users for the booking
	 * 
	 * @return the users for the booking
	 */
	public List<User> getUsers() {
		if (users == null) {
			users = new ArrayList<User>();
		}
		return users;
	}

	/**
	 * Sets the users for the booking
	 * 
	 * @param users
	 *            the users for the booking
	 */
	public void setUsers(List<User> users) {
		this.users = users;
	}

	/**
	 * Returns the rooms for the booking
	 * 
	 * @return the rooms for the booking
	 */
	public List<Room> getRooms() {
		if (rooms == null) {
			rooms = new ArrayList<Room>();
		}
		return rooms;
	}

	/**
	 * Sets the rooms for the booking
	 * 
	 * @param rooms
	 *            the rooms for the booking
	 */
	public void setRooms(List<Room> rooms) {
		this.rooms = rooms;
	}

}
