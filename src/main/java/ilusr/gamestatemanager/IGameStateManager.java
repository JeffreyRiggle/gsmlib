package ilusr.gamestatemanager;

/**
 * 
 * @author Jeff Riggle
 *
 */
public interface IGameStateManager extends IGameStateListener, IFinishListener {
	/**
	 * 
	 * @return The currently running game state.
	 */
	IGameState currentGameState();
	/**
	 * 
	 * @param gameState The new game state (maybe this should be removed).
	 */
	void currentGameState(IGameState gameState);
	/**
	 * 
	 * @param identifier The unique id for this game state.
	 * @param gameState The actual game state.
	 */
	<T>void addGameState(T identifier, IGameState gameState);
	/**
	 * 
	 * @param identifier The unique id for this game state.
	 */
	<T>void removeGameState(T identifier);
	/**
	 * Clears all game states but the current game state.
	 */
	void clearGameStates();
	/**
	 * 
	 * @param data The new runtime data for this game state manager.
	 */
	<D>void runtimeData(D data);
	/**
	 * 
	 * @return The runtime data associated with this game state manager.
	 */
	<D> D runtimeData();
	/**
	 * Starts running the current game state.
	 */
	void start();
	/**
	 * Finishes the game.
	 */
	void finish();
	/**
	 * 
	 * @param listener A @see IFinishListener to be called when the game is finished.
	 */
	void addFinishListener(IFinishListener listener);
	/**
	 * 
	 * @param listener A @see IFinishListener to be removed.
	 */
	void removeFinishListener(IFinishListener listener);
}