package org.ss.generators.table.render.renderers;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.TimeZone;

import org.ss.generators.table.elems.abstraction.XMLElement_A;
import org.ss.generators.table.render.abstraction.ChainRenderer_A;

public class AgeDisplayRenderer extends ChainRenderer_A {

	@Override
	public Object renderContent(XMLElement_A enclosingElm, Object element) {
		Date dateVal = null;
		try {
			dateVal = (Date)element;
		} catch(Exception exception) {
			if(this.getNextRenderer() != null)
				this.getNextRenderer().renderContent(enclosingElm, element);
		}
		
		if(dateVal == null)
			return "-";
		
		String formattedAge = _getFormattedAge(dateVal);
		return formattedAge;
	}

	private String _getFormattedAge(Date dateTime) {
		try {
			long value = dateTime.getTime();
			LocalDateTime fromDate = LocalDateTime.now();

			Instant instant = Instant.ofEpochMilli(value);
			LocalDateTime toDate = LocalDateTime.ofInstant(instant, TimeZone.getDefault().toZoneId());

			LocalDateTime tempDateTime = LocalDateTime.from(fromDate);

			long years = tempDateTime.until(toDate, ChronoUnit.YEARS);
			tempDateTime = tempDateTime.plusYears(years);

			long months = tempDateTime.until(toDate, ChronoUnit.MONTHS);
			tempDateTime = tempDateTime.plusMonths(months);

			long days = tempDateTime.until(toDate, ChronoUnit.DAYS);
			tempDateTime = tempDateTime.plusDays(days);

			long hours = tempDateTime.until(toDate, ChronoUnit.HOURS);
			tempDateTime = tempDateTime.plusHours(hours);

			long minutes = tempDateTime.until(toDate, ChronoUnit.MINUTES);
			tempDateTime = tempDateTime.plusMinutes(minutes);

			String pastOrPresent = (minutes < 0 || hours < 0 || days < 0 || months < 0 || years < 0) ? "old" : "to go";

			StringBuilder outValue = new StringBuilder();
			if (years != 0)
				outValue.append(Math.abs(years) + "y ");
			if (months != 0)
				outValue.append(Math.abs(months) + "m ");
			if (days != 0)
				outValue.append(Math.abs(days) + "d ");
			if (hours != 0)
				outValue.append(Math.abs(hours) + "hr ");
			if (minutes != 0)
				outValue.append(Math.abs(minutes) + "min ");

			outValue.append(pastOrPresent);

			return outValue.toString();
		} catch (Exception theException) {
			return "-";
		}
	}

}
