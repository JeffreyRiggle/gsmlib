package ilusr.gamestatemanager;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class BufferOverflowException extends Exception{
	
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @param newSize The new attempted size.
	 * @param bufferLimit The limit
	 */
	public BufferOverflowException(int newSize, int bufferLimit) {
		super(String.format("The value %s exceeds the buffer size of %s", newSize, bufferLimit));
	}
}
