package tu.sofia.bookings.entity.dto;

import java.io.Serializable;
import java.util.Date;

import tu.sofia.bookings.common.DateUtils;
import tu.sofia.bookings.entity.Booking;
import tu.sofia.bookings.entity.additional.BookingStatus;
import tu.sofia.bookings.entity.additional.PaymentStatus;

/**
 * Data Transfer Object for the {@link Booking} entity
 */
public class BookingDto implements Serializable {

	private static final long serialVersionUID = 1271659134124051134L;

	private BookingStatus bookingStatus;

	private Date startDate;

	private Date endDate;

	private PaymentStatus paymentStatus;

	private Double paymentAmount;

	private Date paymentDate;

	/**
	 * Constructor
	 *
	 * @param booking
	 */
	public BookingDto(Booking booking) {
		this.bookingStatus = booking.getBookingStatus();
		this.startDate = booking.getStartDate();
		this.endDate = booking.getEndDate();
		this.paymentStatus = booking.getPaymentStatus();
		this.paymentAmount = booking.getPaymentAmount() * DateUtils.getDaysBetween(endDate, startDate);
		this.paymentDate = booking.getPaymentDate();
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
	 * Returns the start date
	 *
	 * @return the start date
	 */
	public Date getStartDate() {
		return startDate;
	}

	/**
	 * Sets the start date
	 *
	 * @param startDate
	 *            the start date
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	/**
	 * Returns the end date
	 *
	 * @return the end date
	 */
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * Sets the end date
	 *
	 * @param endDate
	 *            the end date
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	/**
	 * Returns the payment status
	 *
	 * @return the payment status
	 */
	public PaymentStatus getPaymentStatus() {
		return paymentStatus;
	}

	/**
	 * Sets the payment status
	 *
	 * @param paymentStatus
	 *            the payment status
	 */
	public void setPaymentStatus(PaymentStatus paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	/**
	 * Returns the payment amount
	 *
	 * @return the payment amount
	 */
	public Double getPaymentAmount() {
		return paymentAmount;
	}

	/**
	 * Sets the payment amount
	 *
	 * @param paymentAmount
	 *            the payment amount
	 */
	public void setPaymentAmount(Double paymentAmount) {
		this.paymentAmount = paymentAmount;
	}

	/**
	 * Returns the payment date
	 *
	 * @return the payment date
	 */
	public Date getPaymentDate() {
		return paymentDate;
	}

	/**
	 * Sets the payment date
	 *
	 * @param paymentDate
	 *            the payment date
	 */
	public void setPaymentDate(Date paymentDate) {
		this.paymentDate = paymentDate;
	}
}
