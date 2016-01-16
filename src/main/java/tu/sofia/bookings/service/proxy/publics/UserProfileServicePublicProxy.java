package tu.sofia.bookings.service.proxy.publics;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import tu.sofia.bookings.entity.User;
import tu.sofia.bookings.service.proxy.protecteds.ProfileServiceUserProxy;

/**
 * Service for registering users
 */
@Singleton
@Path("/public/user/profile")
public class UserProfileServicePublicProxy {

	private ProfileServiceUserProxy userProfileServiceProxy;

	/**
	 * Constructor
	 *
	 * @param userProfileServiceProxy
	 */
	@Inject
	public UserProfileServicePublicProxy(ProfileServiceUserProxy userProfileServiceProxy) {
		this.userProfileServiceProxy = userProfileServiceProxy;
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
		return userProfileServiceProxy.getUserProfile(request);
	}
}
