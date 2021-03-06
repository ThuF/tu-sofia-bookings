package tu.sofia.bookings.service.inject;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.ServletContextEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.persist.PersistService;
import com.google.inject.servlet.GuiceServletContextListener;

import tu.sofia.bookings.service.BookService;
import tu.sofia.bookings.service.BookingService;
import tu.sofia.bookings.service.GeneralExceptionHandler;
import tu.sofia.bookings.service.GsonMessageBodyHandler;
import tu.sofia.bookings.service.PaymentService;
import tu.sofia.bookings.service.RoomReviewService;
import tu.sofia.bookings.service.RoomService;
import tu.sofia.bookings.service.UserBookingService;
import tu.sofia.bookings.service.UserService;
import tu.sofia.bookings.service.proxy.protecteds.ProfileServiceUserProxy;
import tu.sofia.bookings.service.proxy.protecteds.RegistrationServiceUserProxy;
import tu.sofia.bookings.service.proxy.protecteds.RoomReviewServiceUserProxy;
import tu.sofia.bookings.service.proxy.publics.PaymentServicePublicProxy;
import tu.sofia.bookings.service.proxy.publics.RoomReviewServicePublicProxy;
import tu.sofia.bookings.service.proxy.publics.RoomServicePublicProxy;
import tu.sofia.bookings.service.proxy.publics.UserProfileServicePublicProxy;

/**
 * This class handles the initialization of all Guice modules and all REST API
 * resources.
 */
public class ApplicationContextListener extends GuiceServletContextListener {

	private static final Logger logger = LoggerFactory.getLogger(ApplicationContextListener.class);

	private static final String DEBUG_INITIALIZING_GUICE_INJECTOR_MESSAGE = "Initializing Guice Injector with modules for dependency injection";
	private static final String DEBUG_REGISTERING_API_SERVICES_MESSAGE = "Registering API services";
	private static final String DEBUG_API_SERVICES_REGISTED_MESSAGE = "API services registed: [{}]";
	private static final String DEBUG_STARTING_PERSISTENCE_SERVICE_MESSAGE = "Starting persistence service throuh Guice injector";

	private static final Set<Object> singletons = new HashSet<Object>();
	private static Injector staticInjector;

	private Injector injector;

	@Override
	protected Injector getInjector() {
		logger.debug(DEBUG_INITIALIZING_GUICE_INJECTOR_MESSAGE);
		injector = Guice.createInjector(new DataSourceModule(), new PersistFilterModule());
		ApplicationContextListener.staticInjector = injector;
		return injector;
	}

	@Override
	public void contextInitialized(ServletContextEvent servletContextEvent) {
		super.contextInitialized(servletContextEvent);
		startPersistenceService();
		registerAPIServices();
	}

	private void startPersistenceService() {
		logger.debug(DEBUG_STARTING_PERSISTENCE_SERVICE_MESSAGE);
		final PersistService persistService = injector.getInstance(PersistService.class);
		persistService.start();
	}

	private void registerAPIServices() {
		logger.debug(DEBUG_REGISTERING_API_SERVICES_MESSAGE);
		addServices();
		logger.debug(DEBUG_API_SERVICES_REGISTED_MESSAGE, Arrays.asList(getSingletons().toArray()));
	}

	private void addServices() {
		getSingletons().add(new GsonMessageBodyHandler<Object>());
		getSingletons().add(new GeneralExceptionHandler());

		// Services protected with the Admin role
		getSingletons().add(injector.getInstance(UserService.class));
		getSingletons().add(injector.getInstance(RoomService.class));
		getSingletons().add(injector.getInstance(RoomReviewService.class));
		getSingletons().add(injector.getInstance(BookingService.class));
		getSingletons().add(injector.getInstance(PaymentService.class));

		// Services protected with the Everyone role
		getSingletons().add(injector.getInstance(BookService.class));
		getSingletons().add(injector.getInstance(UserBookingService.class));

		// Proxy services protected with the Everyone role
		getSingletons().add(injector.getInstance(ProfileServiceUserProxy.class));
		getSingletons().add(injector.getInstance(RegistrationServiceUserProxy.class));
		getSingletons().add(injector.getInstance(RoomReviewServiceUserProxy.class));

		// Public proxy services
		getSingletons().add(injector.getInstance(UserProfileServicePublicProxy.class));
		getSingletons().add(injector.getInstance(RoomServicePublicProxy.class));
		getSingletons().add(injector.getInstance(RoomReviewServicePublicProxy.class));
		getSingletons().add(injector.getInstance(PaymentServicePublicProxy.class));
	}

	/**
	 * Get singleton services registered to this application.
	 *
	 * @return all singleton services.
	 */
	public static Set<Object> getSingletons() {
		return singletons;
	}

	/**
	 * @return injector
	 */
	public static Injector getStaticInjector() {
		return staticInjector;
	}
}
