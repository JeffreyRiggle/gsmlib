package ilusr.gamestatemanager;

import ilusr.core.interfaces.IDispose;

/**
 * 
 * @author Jeff Riggle
 *
 */
public interface IGameState extends IDispose {
	/**
	 * Add a listener for the game state completed event.
	 */
	void addCompletedEventListener(IGameStateListener listener);
	/**
	 * Remove a listener from the game state completed event.
	 */
	void removeCompletedEventListener(IGameStateListener listener);
	/**
	 * 
	 * @param listener A @see IFinishListener to run if this game state finishes the game.
	 */
	void addFinishEventListener(IFinishListener listener);
	/**
	 * 
	 * @param listener A @see IFinishListener to remove.
	 */
	void removeFinishEventListener(IFinishListener listener);
	/**
	 * Fire the game state completed event.
	 */
	<T>void stateCompleted(T data);
	/**
	 * 
	 * @return The content for the game state.
	 */
	<T> T stateContent();
	/**
	 * 
	 * @param value The new content for the game state.
	 */
	<T> void stateContent(T value);
	//TODO: Should run() also be an option?
	/**
	 * Runs the game state with some data.
	 */
	<T> void run(T data);
	/**
	 * Finishes the game only signal this if you want to end the game completely.
	 */
	void finish();
}