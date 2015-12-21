package tu.sofia.bookings.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tu.sofia.bookings.entity.User;

/**
 * DAO class for {@link User}
 */
public class UserDao extends AbstractJpaDao<String, User> {

	private static final Logger logger = LoggerFactory.getLogger(UserDao.class);

	protected UserDao() {
		super(User.class);
	}

	@Override
	protected Logger getLogger() {
		return logger;
	}

}
