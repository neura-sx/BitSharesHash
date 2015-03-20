package sx.neura.bts.util;

import java.util.ArrayList;
import java.util.List;

import sx.neura.bts.Text;
import sx.neura.bts.util.Util;


public abstract class Validator {
	
	private boolean canBeEmpty = false;
	
	public void setCanBeEmpty(boolean canBeEmpty) {
		this.canBeEmpty = canBeEmpty;
	}
	
	public boolean validate(String s) {
		if (!Util.isValidString(s) && canBeEmpty)
			return true;
		return false;
	}
	
	protected static boolean error(Object e) {
		System.err.println(e);
		return false;
	}
	
//	public static class IsId extends Validator {
//		@Override
//		public boolean validate(String s) {
//			if (super.validate(s))
//				return true;
//			if (!Module.isValidId(s))
//				return error(Text.VALIDATION_MUST_BE_ID);
//			return true;
//		}
//	}

	public static class IsIntegerList extends Validator {
		private Integer min = null;
		private Integer max = null;
		public void setMinMax(Integer min, Integer max) {
			this.min = min;
			this.max = max;
		}
		@Override
		public boolean validate(String s) {
			if (super.validate(s))
				return true;
			if (!Util.isValidString(s))
				return false;
			String[] split = s.split(Text.INTEGER_LIST_SEPARATOR.toString());
			List<Integer> list = new ArrayList<Integer>();
			try {
				for (String n : split)
					list.add(new Integer(n));
			} catch (Exception e) {
				return error(Text.VALIDATION_MUST_BE_INTEGER_LIST);
			}
			if (min != null && max != null) {
				for (Integer i : list) {
					if (i < min || i > max)
						return error(String.format("%s [%d,%d]", Text.VALIDATION_MUST_BE_INTEGER_LIST_RANGE, min, max));
				}
			}
			return true;
		}
	}

	public static class IsBoolean extends Validator {
		@Override
		public boolean validate(String s) {
			if (super.validate(s))
				return true;
			boolean ok = s.equalsIgnoreCase("true") || s.equalsIgnoreCase("t") || s.equalsIgnoreCase("false") || s.equalsIgnoreCase("f");
			if (!ok)
				return error(Text.VALIDATION_MUST_BE_BOOLEAN);
			return true;
		}
	}

//	public static class IsUTC extends Validator {
//		@Override
//		public boolean validate(String s) {
//			if (super.validate(s))
//				return true;
//			UTC u = null;
//			try {
//				u = new UTC(s);
//			} catch (Exception e) {
//				return error(Text.VALIDATION_MUST_BE_DATE);
//			}
//			if (u.isBefore(Module.getTime()))
//				return error(Text.VALIDATION_MUST_BE_DATE_FUTURE);
//			return true;
//		}
//	}

	public static abstract class IsNumber<T> extends Validator {
		protected T min = null;
		protected T max = null;
		public void setMinMax(T min, T max) {
			this.min = min;
			this.max = max;
		}
		protected abstract T cast(String s);
		protected abstract boolean range(T t);
		protected abstract boolean onCastError(String s);
		protected abstract boolean onRangeError();
		@Override
		public boolean validate(String s) {
			if (super.validate(s))
				return true;
			T t = null;
			try {
				t = cast(s);
			} catch (Exception e) {
				return onCastError(s);
			}
			if (min != null && max != null && !range(t))
				return onRangeError();
			return true;
		}
	}

	public static class IsInteger extends IsNumber<Integer> {
		@Override
		public Integer cast(String s) {
			return new Integer(s);
		}
		@Override
		public boolean range(Integer n) {
			return (n >= min && n <= max);
		}
		@Override
		protected boolean onCastError(String s) {
			return error(Text.VALIDATION_MUST_BE_INTEGER);
		}
		@Override
		protected boolean onRangeError() {
			return error(String.format("%s [%d,%d]", Text.VALIDATION_MUST_BE_INTEGER_RANGE, min, max));
		}
	}

//	public static class IsIntegerOrId extends IsInteger {
//		@Override
//		protected boolean onCastError(String s) {
//			if (Module.isValidId(s))
//				return true;
//			return error(Text.VALIDATION_MUST_BE_INTEGER_OR_ID);
//		}
//	}

	public static class IsDouble extends IsNumber<Double> {
		@Override
		public Double cast(String s) {
			return new Double(s);
		}
		@Override
		public boolean range(Double n) {
			return (n >= min && n <= max);
		}
		@Override
		protected boolean onCastError(String s) {
			return error(Text.VALIDATION_MUST_BE_NUMBER);
		}
		@Override
		protected boolean onRangeError() {
			return error(String.format("%s [%f,%f]", Text.VALIDATION_MUST_BE_NUMBER_RANGE, min, max));
		}
	}

}
