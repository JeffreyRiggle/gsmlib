package ilusr.gamestatemanager;

import java.util.Map;

/**
 * 
 * @author Jeff Riggle
 *
 */
public interface IDataSource {
	/**
	 * 
	 * @param bufferSize The number of items to return
	 * @param state The current state.
	 * @return A Map of sequential states.
	 */
	Map<Object, IGameState> provideGameStates(int bufferSize, Object state);
}
