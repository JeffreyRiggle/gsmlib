package ilusr.gamestatemanager;

import java.util.Map;
import java.util.Map.Entry;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class BufferedGameStateManager extends GameStateManager {

	private int bufferSize;
	private IDataSource source;
	
	public <T, D>BufferedGameStateManager(int bufferSize, IDataSource source, T id, IGameState currentGameState, D runtimeData) {
		super(id, currentGameState, runtimeData);
		// +1 for current game state.
		this.bufferSize = bufferSize+1;
		this.source = source;
	}

	/**
	 * 
	 * @param gameState The game state to add
	 * @param identifier The id of the game state.
	 * @throws BufferOverflowException
	 */
	public <T>void addGameState(IGameState gameState, T identifier) throws BufferOverflowException {
		if (super.gameStateMap().size() >= bufferSize) {
			throw new BufferOverflowException(super.gameStateMap().size() + 1, bufferSize);
		}
		
		super.addGameState(identifier, gameState);
	}
	
	@Override
	public <T> void completed(T completionData) {
		if (super.gameStateMap().get(completionData) == null) {
			super.clearGameStates();
			Map<Object, IGameState> nextStates = source.provideGameStates(bufferSize, completionData);
			
			for (Entry<Object, IGameState> gameState: nextStates.entrySet()) {
				try {
					addGameState(gameState.getValue(), gameState.getKey());
				} catch (Exception e) {
					//TODO: I dont like this.
				}
			}
		}
		
		super.completed(completionData);
	}
}
