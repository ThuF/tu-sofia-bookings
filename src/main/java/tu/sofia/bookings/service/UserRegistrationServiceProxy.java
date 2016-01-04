package tu.sofia.bookings.service;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import tu.sofia.bookings.entity.User;

/**
 * Service for registering users
 */
@Singleton
@Path("/protected/user/register")
public class UserRegistrationServiceProxy {

	private UserService userService;

	/**
	 * Constructor
	 *
	 * @param userService
	 */
	@Inject
	public UserRegistrationServiceProxy(UserService userService) {
		this.userService = userService;
	}

	/**
	 * Creates new user profile for the currently logged in user
	 *
	 * @param request
	 * @return HTTP 200 OK if the user profile was successfully added or
	 *         HTTP 400 if something went wrong
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addUser(@Context HttpServletRequest request) {
		User user = new User();
		user.setUserId(request.getRemoteUser());
		return userService.addUser(user);
	}
}
