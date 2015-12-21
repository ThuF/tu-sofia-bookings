package tu.sofia.bookings.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Entity class representing the T_USER database table
 */
@Entity
@Table(name = "T_USER")
public class User implements Serializable {

	private static final long serialVersionUID = 3662442049878132804L;

	@Id
	@Column(length = 256)
	private String userId;

	@Column(length = 256)
	private String firstName;

	@Column(length = 256)
	private String lastName;

	@Column(length = 256)
	private String email;

	/**
	 * Returns the id of the user
	 *
	 * @return the id of the user
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * Sets the id of the user
	 *
	 * @param userId
	 *            the id of the user
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * Returns the first name of the user
	 *
	 * @return the first name of the user
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * Sets the first name of the user
	 *
	 * @param firstName
	 *            the first name of the user
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * Returns the last name of the user
	 *
	 * @return the last name of the user
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * Sets the last name of the user
	 *
	 * @param lastName
	 *            the last name of the user
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * Returns the email of the user
	 *
	 * @return the email of the user
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Sets the email of the user
	 *
	 * @param email
	 *            the email of the user
	 */
	public void setEmail(String email) {
		this.email = email;
	}
}
