package com.simjava.core;

import static java.util.concurrent.TimeUnit.*;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;


public final class TimeInstant implements Comparable<TimeInstant> {

   private final static TimeZone DEFAULT_PREFERRED_TIMEZONE = TimeZone.getTimeZone("GMT+8:00");
   private final long _timeInEpsilon;
   private TimeZone _preferredTimeZone;

   public TimeInstant(long time, TimeUnit unit) {
       if (unit == null) {
           throw new RuntimeException("Can't create TimeInstant object!");
       }

       if (unit.compareTo(TimeOperations.getEpsilon()) < 0) {

           if (TimeOperations.getStartTime() != null){
                System.out.println("Starttime: " + TimeOperations.getStartTime());
				long timeSinceStart = time - unit.convert(TimeOperations.getStartTime().getTimeInEpsilon(), TimeOperations.getEpsilon());
                System.out.println("TimeSinceStart: " + timeSinceStart);
                if (timeSinceStart != 0) {
                    time = time - (timeSinceStart % unit.convert(1, TimeOperations.getEpsilon()));
                }
           }
       }
        this._timeInEpsilon = TimeOperations.getEpsilon().convert(time, unit);
	    TimeOperations.timeObjectsCreated = true;
		if (_timeInEpsilon == Long.MAX_VALUE) {
			/*The timeInstant is too big.
			(The method TimeUnit.convert(duration,unit)returns Long.MAX_VALUE if
			the result of the conversion is to big*/

			throw new RuntimeException("Can't create TimeInstant object! Simulation aborted.");
		}

		this._preferredTimeZone = DEFAULT_PREFERRED_TIMEZONE;
   }

   public TimeInstant(long timeInReferenceUnit) {
       this(timeInReferenceUnit, TimeOperations.getReferenceUnit());
   }
}
