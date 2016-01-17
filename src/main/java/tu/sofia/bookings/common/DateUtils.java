package tu.sofia.bookings.common;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import com.google.inject.Singleton;

/**
 * Utility class for working with dates
 */
@Singleton
public class DateUtils {

	/**
	 * Returns the difference in days between two dates
	 * 
	 * @param date1
	 * @param date2
	 * @return the difference in days between two dates
	 */
	public static long getDaysBetween(Date date1, Date date2) {
		return TimeUnit.DAYS.convert((date1.getTime() - date2.getTime()), TimeUnit.MILLISECONDS);
	}

}
