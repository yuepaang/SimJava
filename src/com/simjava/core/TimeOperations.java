package com.simjava.core;

//import java.util.concurrent.TimeUnit;
//
//public final class TimeOperations {
//
//    private static TimeUnit epsilon = TimeUnit.MICROSECONDS;
//    private static TimeUnit referenceUnit = TimeUnit.SECONDS;
//
//    private static TimeInstant startTime;
//
//    private static TimeFormatter myTimeFormatter = getDefaultTimeFormatter();
//
//    private static boolean customTimeFormatterUsed = false;
//
//    protected static boolean timeObjectsCreated = false;
//
//    private TimeOperations() {}
//
//    public static TimeSpan add(TimeSpan a, TimeSpan b) {
//        if (Long.MAX_VALUE - a.getTimeInEpsilon() - b.getTimeInEpsilon() < 1) {
//            throw new RuntimeException("Can't add TimeSpan objects!");
//        }
//        return new TimeSpan(a.getTimeInEpsilon() + b.getTimeInEpsilon(), epsilon);
//    }
//
//    public static TimeInstant add(TimeSpan span, TimeInstant instant) {
//        return TimeOperations.add(instant, span);
//    }
//
//    // TODO
//
//}
