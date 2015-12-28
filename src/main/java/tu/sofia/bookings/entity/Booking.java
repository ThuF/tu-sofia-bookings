package tu.sofia.bookings.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import tu.sofia.bookings.entity.additional.BookingStatus;
import tu.sofia.bookings.entity.additional.PaymentStatus;

/**
 * Entity class representing the T_BOOKING database table
 */
@Entity
@Table(name = "T_BOOKING")
@NamedQueries({ @NamedQuery(name = Booking.QUERY_NAME_FIND_BY_ID_AND_USER, query = Booking.QUERY_FIND_BY_ID_AND_USER),
		@NamedQuery(name = Booking.QUERY_NAME_FIND_BY_BOOKING_PERIOD, query = Booking.QUERY_FIND_BY_BOOKING_PERIOD),
		@NamedQuery(name = Booking.QUERY_NAME_FIND_ALL_BY_USER, query = Booking.QUERY_FIND_ALL_BY_USER) })
public class Booking implements Serializable {

	/**
	 * The name of a query for finding a booking by booking id and user
	 */
	public static final String QUERY_NAME_FIND_BY_ID_AND_USER = "findByIdAndUser";

	/**
	 * The name of a query for finding a bookings by booking period
	 */
	public static final String QUERY_NAME_FIND_BY_BOOKING_PERIOD = "findByBookingPeriod";

	/**
	 * The name of a query for finding all bookings for user
	 */
	public static final String QUERY_NAME_FIND_ALL_BY_USER = "findAllByUser";

	/**
	 * The bookingId parameter
	 */
	public static final String PARAM_BOOKING_ID = "bookingId";

	/**
	 * The user parameter
	 */
	public static final String PARAM_USER = "user";

	/**
	 * The startDate parameter
	 */
	public static final String PARAM_START_DATE = "startDate";

	/**
	 * The endDate parameter
	 */
	public static final String PARAM_END_DATE = "endDate";

	static final String QUERY_FIND_BY_ID_AND_USER = "select b from Booking b where b.bookingId = :" + PARAM_BOOKING_ID + " and b.user = :"
			+ PARAM_USER;
	static final String QUERY_FIND_BY_BOOKING_PERIOD = "select b from Booking b where b.startDate >= :" + PARAM_START_DATE + " and b.endDate <= :"
			+ PARAM_END_DATE;
	static final String QUERY_FIND_ALL_BY_USER = "select b from Booking b where b.user = :" + PARAM_USER;

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

	@OneToOne
	private User user;

	private Long roomId;

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
	 * Returns the user for the booking
	 *
	 * @return the user for the booking
	 */
	public User getUser() {
		return user;
	}

	/**
	 * Sets the user for the booking
	 *
	 * @param user
	 *            the user for the booking
	 */
	public void setUser(User user) {
		this.user = user;
	}

	/**
	 * Returns the room id for the booking
	 *
	 * @return the room id for the booking
	 */
	public Long getRoomId() {
		return roomId;
	}

	/**
	 * Sets the room id for the booking
	 *
	 * @param roomId
	 *            the room id for the booking
	 */
	public void setRoomId(Long roomId) {
		this.roomId = roomId;
	}

}
