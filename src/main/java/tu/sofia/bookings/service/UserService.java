package tu.sofia.bookings.service;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.cxf.common.util.StringUtils;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import tu.sofia.bookings.common.UnitOfWorkUtils;
import tu.sofia.bookings.dao.UserDao;
import tu.sofia.bookings.entity.User;

/**
 * Service for registering users
 */
@Singleton
@Path("/entities/users")
public class UserService {

	private UnitOfWorkUtils unitOfWorkUtils;
	private UserDao userDao;

	/**
	 * Constructor
	 *
	 * @param unitOfWorkUtils
	 * @param userDao
	 */
	@Inject
	public UserService(UnitOfWorkUtils unitOfWorkUtils, UserDao userDao) {
		this.unitOfWorkUtils = unitOfWorkUtils;
		this.userDao = userDao;
	}

	/**
	 * Returns a list of all users
	 *
	 * @return list of all users
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<User> getUsers() {
		unitOfWorkUtils.begin();

		List<User> users = userDao.findAll();

		unitOfWorkUtils.end();
		return users;
	}

	/**
	 * Return the information about a specific user
	 *
	 * @param id
	 * @return the information about a specific user
	 */
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public User getUser(@PathParam("id") String id) {
		unitOfWorkUtils.begin();

		User user = userDao.findById(id);
		if (user == null) {
			throw new NotFoundException();
		}

		unitOfWorkUtils.end();
		return user;
	}

	/**
	 * Returns the count of users
	 *
	 * @return the count of users
	 */
	@GET
	@Path("/count")
	@Produces(MediaType.TEXT_PLAIN)
	public Long count() {
		unitOfWorkUtils.begin();

		long count = userDao.countAll();

		unitOfWorkUtils.end();
		return count;
	}

	/**
	 * Adds new user
	 *
	 * @param user
	 * @return HTTP 200 OK if the user was successfully added or
	 *         HTTP 400 if something went wrong
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addUser(User user) {
		unitOfWorkUtils.begin();

		Response response = null;
		if (!StringUtils.isEmpty(user.getUserId()) && (userDao.findById(user.getUserId()) == null)) {
			userDao.create(user);
			response = Response.status(Status.CREATED).build();
		} else {
			response = Response.status(Status.BAD_REQUEST).build();
		}

		unitOfWorkUtils.end();
		return response;
	}

	/**
	 * Updates existing user
	 *
	 * @param id
	 * @param user
	 * @return HTTP 201 NO CONTENT if the update was successful or
	 *         HTTP 404 NOT FOUND if there was no such user
	 */
	@PUT
	@Path("/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateUser(@PathParam("id") String id, User user) {
		unitOfWorkUtils.begin();

		Response response = null;
		User persistedUser = userDao.findById(id);
		if (persistedUser != null) {
			updateUserProperties(persistedUser, user);
			userDao.update(persistedUser);
			response = Response.noContent().build();
		} else {
			response = Response.status(Status.NOT_FOUND).build();
		}

		unitOfWorkUtils.end();
		return response;
	}

	private void updateUserProperties(User persistedUser, User user) {
		persistedUser.setFirstName(user.getFirstName());
		persistedUser.setLastName(user.getLastName());
		persistedUser.setEmail(user.getEmail());
	}

	/**
	 * Deletes specific user
	 *
	 * @param id
	 * @return HTTP 201 NO CONTENT if the deletion was successful or
	 *         HTTP 404 if there was no such user
	 */
	@DELETE
	@Path("/{id}")
	public Response deleteUser(@PathParam("id") String id) {
		unitOfWorkUtils.begin();

		Response response = null;
		User user = userDao.findById(id);
		if (user != null) {
			userDao.delete(user);
			response = Response.noContent().build();
		} else {
			response = Response.status(Status.NOT_FOUND).build();
		}

		unitOfWorkUtils.end();
		return response;
	}
}
