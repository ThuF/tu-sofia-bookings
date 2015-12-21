package tu.sofia.bookings.service;

import java.util.Set;

import javax.ws.rs.core.Application;

import tu.sofia.bookings.service.inject.ApplicationContextListener;

/**
 * This class extends the CXF Application and adds CXF services
 */
public class BookingsApplication extends Application {

	@Override
	public Set<Object> getSingletons() {
		return ApplicationContextListener.getSingletons();
	}
}
