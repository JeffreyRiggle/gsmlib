package ilusr.gamestatemanager;

/**
 * 
 * @author Jeff Riggle
 *
 */
public interface IGameStateListener {
	/**
	 * 
	 * @param completionData The data ran at completion.
	 */
	<T>void completed(T completionData);
}