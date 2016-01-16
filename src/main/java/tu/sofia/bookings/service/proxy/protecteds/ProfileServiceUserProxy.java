package tu.sofia.bookings.service.proxy.protecteds;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import tu.sofia.bookings.entity.User;
import tu.sofia.bookings.service.UserService;

/**
 * Service for registering users
 */
@Singleton
@Path("/protected/user/profile")
public class ProfileServiceUserProxy {

	private UserService userService;

	/**
	 * Constructor
	 *
	 * @param userService
	 */
	@Inject
	public ProfileServiceUserProxy(UserService userService) {
		this.userService = userService;
	}

	/**
	 * Return the user profile of the currently logged in user
	 *
	 * @param request
	 * @return the user profile of the currently logged in user
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public User getUserProfile(@Context HttpServletRequest request) {
		String userId = request.getRemoteUser();
		if (userId == null) {
			throw new NotAuthorizedException("There is no currently logged in user");
		}
		return userService.getUser(userId);
	}

	/**
	 * Updates the user profile of the currently logged in user
	 *
	 * @param request
	 * @param user
	 * @return HTTP 201 NO CONTENT if the update was successful or
	 *         HTTP 404 NOT FOUND if there was no such user
	 */
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateUserProfile(@Context HttpServletRequest request, User user) {
		return userService.updateUser(request.getRemoteUser(), user);
	}
}
