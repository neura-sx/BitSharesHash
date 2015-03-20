package sx.neura.bts.json.exceptions;

import sx.neura.bts.json.CommandJson.Error;

public class BTSSystemException extends BTSException {
	private static final long serialVersionUID = 1L;
	
	public BTSSystemException(Error error) {
		super(error);
	}
	
//	public enum Reason {
//		
//	}
//	
//	private Reason reason;
//	
//	public Reason getReason() {
//		return reason;
//	}
//	
//	public BTSSystemException(String message) {
//		super(message);
//	}
//	
//	public BTSSystemException(Reason reason) {
//		super(reason.toString());
//		this.reason = reason;
//	}
	
}
