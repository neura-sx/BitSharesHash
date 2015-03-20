package sx.neura.bts.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;

public class Time {
	
	private static final String UNDEFINED = "1970-01-01T00:00:00";
	//private static final String UNDEFINED = encode(LocalDateTime.of(1970, Month.JANUARY, 1, 0, 0, 0));
	

	public enum Format {
		DATE_LONG_FORMAT("dd-MMM-yyyy"),
		DATE_SHORT_FORMAT("dd/MM/yy"),
		DATE_AND_TIME_LONG_FORMAT("dd-MMM-yyyy hh:mm:ss"),
		DATE_AND_TIME_MEDIUM_FORMAT("dd-MMM-yyyy hh:mm"),
		DATE_AND_TIME_SHORT_FORMAT("dd/MM/yy hh:mm");
		private Format(String pattern) {
			this.formatter = DateTimeFormatter.ofPattern(pattern);
		}
		private DateTimeFormatter formatter;
	}
	
	private static final DateTimeFormatter LOCAL_DATE_TIME_FORMATTER = new DateTimeFormatterBuilder()
        .parseCaseInsensitive()
        .appendValue(ChronoField.YEAR, 4)
        .appendLiteral('-')
        .appendValue(ChronoField.MONTH_OF_YEAR, 2)
        .appendLiteral('-')
        .appendValue(ChronoField.DAY_OF_MONTH, 2)
        .appendLiteral('T')
        .appendValue(ChronoField.HOUR_OF_DAY, 2)
        .appendLiteral(':')
        .appendValue(ChronoField.MINUTE_OF_HOUR, 2)
        .appendLiteral(':')
        .appendValue(ChronoField.SECOND_OF_MINUTE, 2)
        .toFormatter();
	
	public static LocalDateTime decode(String s) {
		if (!Util.isValidString(s))
			return null;
		return LocalDateTime.parse(s, LOCAL_DATE_TIME_FORMATTER);
	}
	public static String encode(LocalDateTime t) {
		if (t == null)
			return null;
		return t.format(LOCAL_DATE_TIME_FORMATTER);
	}
	
	public static boolean isUndefined(String s) {
		return s.equals(UNDEFINED);
	}
	
	public static String format(String s) {
		return format(decode(s));
	}
	public static String format(String s, String undefined) {
		if (isUndefined(s))
			return undefined;
		return format(s);
	}
	public static String format(String s, Format format) {
		return decode(s).format(format.formatter);
	}
	
	public static String format(LocalDateTime t) {
		return format(t, Format.DATE_AND_TIME_LONG_FORMAT);
	}
	public static String format(LocalDateTime t, Format format) {
		if (t == null)
			return null;
		return t.format(format.formatter);
	}
	
	public static String format(LocalDate t, Format format) {
		if (t == null)
			return null;
		return t.format(format.formatter);
	}
	
	public static void main(String[] args) {
//		{
//			LocalDateTime t = LocalDateTime.of(2014, Month.OCTOBER, 17, 14, 28, 19);
//			System.out.println(encode(t));
//		}
		{
			String s = "2014-10-17T14:28:19";
//			System.out.println(format(s, FormatStyle.FULL));
//			System.out.println(format(s, FormatStyle.MEDIUM));
//			System.out.println(format(s, FormatStyle.SHORT));
			System.out.println(format(s, Format.DATE_AND_TIME_LONG_FORMAT));
			System.out.println(format(s, Format.DATE_LONG_FORMAT));
		}
//		{
//			LocalDateTime t = LocalDateTime.of(2014, Month.NOVEMBER, 5, 10, 0, 19);
//			System.out.println(t.format(DateTimeFormatter.ofPattern("dd-MMM-yyyy")));
//		}
//		{
//			String s = "";
//			LocalDateTime t = decode(s);
//			System.out.println(format(t));
//		}
	}
}
