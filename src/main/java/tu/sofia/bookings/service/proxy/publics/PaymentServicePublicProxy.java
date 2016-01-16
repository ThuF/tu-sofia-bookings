package tu.sofia.bookings.service.proxy.publics;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import tu.sofia.bookings.service.PaymentService;

/**
 * Proxy service for payment
 */
@Singleton
@Path("/public/payment")
public class PaymentServicePublicProxy {

	private PaymentService paymentService;

	/**
	 * Constructor
	 *
	 * @param paymentService
	 */
	@Inject
	public PaymentServicePublicProxy(PaymentService paymentService) {
		this.paymentService = paymentService;
	}

	/**
	 * Return the price of a specific room
	 *
	 * @param id
	 * @return the price of a specific room
	 */
	@GET
	@Path("/rooms/{id}/price")
	@Produces(MediaType.TEXT_PLAIN)
	public Double getRoomPrice(@PathParam("id") Long id) {
		return paymentService.getRoomPrice(id);
	}
}
