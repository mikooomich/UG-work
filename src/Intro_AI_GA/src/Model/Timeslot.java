package Model;

import java.time.DayOfWeek;

public class Timeslot {
	public DayOfWeek day;
	public int hour;

	public Timeslot(String day, int hour) throws Exception {
		this.day = stringToDay(day);

		this.hour = hour;

	}

	public static DayOfWeek stringToDay(String num) throws Exception {
		return switch (num) {
			case "Monday" -> DayOfWeek.MONDAY;
			case "Tuesday" -> DayOfWeek.TUESDAY;
			case "Wednesday" -> DayOfWeek.WEDNESDAY;
			case "Thursday" -> DayOfWeek.THURSDAY;
			case "Friday" -> DayOfWeek.FRIDAY;
			case "Saturday" -> DayOfWeek.SATURDAY;
			case "Sunday" -> DayOfWeek.SUNDAY;
			default -> throw new Exception("Tis not a day: " + num);
		};
	}

	/**
	 * Convert the day enum to a single character representation. Well technically Sunday is "Su" but we will never
	 * schedule classes on Sundays... right?
	 *
	 * @param day
	 * @return
	 */
	public static String dayToChar(DayOfWeek day) {
		return switch (day) {
			case MONDAY -> "M";
			case TUESDAY -> "T";
			case WEDNESDAY -> "W";
			case THURSDAY -> "R";
			case FRIDAY -> "F";
			case SATURDAY -> "S";
			case SUNDAY -> "Su";
		};
	}


	/**
	 * Convert the day enum to a single character representation. Well technically Sunday is "Su" but we will never
	 * schedule classes on Sundays... right?
	 *
	 * @param day
	 * @return
	 */
	public static String intDayToChar(int day) throws Exception {
		return switch (day) {
			case 1 -> "M";
			case 2 -> "T";
			case 3 -> "W";
			case 4 -> "R";
			case 5 -> "F";
			case 6 -> "S";
			case 7 -> "Su";
			default -> throw new Exception("Tis not a day: " + day +". Monday starts at 1. Sunday is 7.");
		};
	}


}
