package sx.neura.bts.json.exceptions;

import sx.neura.bts.json.CommandJson.Error;

public class BTSUserException extends BTSException {
	private static final long serialVersionUID = 1L;
	
	public BTSUserException(Error error) {
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
//	public BTSUserException(String message) {
//		super(message);
//	}
//	
//	public BTSUserException(Reason reason) {
//		super(reason.toString());
//		this.reason = reason;
//	}
	
}
