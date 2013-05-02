package com.example.mobilelastfm;

import java.util.Date;

import de.umass.lastfm.Event;

public class EventDate {
	
	public static String getDuration(Event event) {
		String duration = "";
		Date start = event.getStartDate();
		Date end = event.getEndDate();

		if (end == null)
			duration = dateToString(start.toString(), null);
		else
			duration = dateToString(start.toString(), end.toString());
		return duration;
	}

	public static String dateToString(String start, String end) {
		String to_return = "";
		String start_month = getMonth(start);
		String start_day = getDay(start);

		if (end != null)
		{
			String end_month = getMonth(end);
			String end_day = getDay(end);

			if (start_month.equals(end_month))
				to_return = start_day + "-" + end_day + " " + start_month;
			else
				to_return = start_day + " " + start_month + "-" + end_day + " " + end_month;
		}
		else
			to_return = start_day + " " + start_month;
		return to_return;
	}

	private static String getMonth(String date) {
		String result = "";
		result = date.substring(4, 7);
		return result;
	}

	private static String getDay(String date) {
		String result = "";
		result = date.substring(8, 10);
		return result;
	}

}
