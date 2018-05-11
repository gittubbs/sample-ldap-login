package utils;

public class CannotAuthenticateException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public CannotAuthenticateException(){
		super();
	}
	
	public CannotAuthenticateException(String message){
		super(message);
	}
}
