package sx.neura.bts.json.exceptions;

import sx.neura.bts.json.CommandJson;

public class BTSException extends Exception {
	private static final long serialVersionUID = 1L;
	
	protected CommandJson.Error error;
	
	public BTSException(CommandJson.Error error) {
		this.error = error;
	}
	
	public CommandJson.Error getError() {
		return error;
	}
	
	public BTSException(String message) {
		super(message);
	}
}
