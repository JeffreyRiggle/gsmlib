package ilusr.gamestatemanager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//TODO: make _runtimeData not a Object but D.
/**
 * 
 * @author Jeffrey Riggle
 *
 */
public class GameStateManager implements IGameStateManager {

	private Map<Object, IGameState> gameStateMap;
	private IGameState currentGameState;
	private Object lastKey;
	private boolean started;
	private Object runtimeData;
	private List<IFinishListener> finishListeners;
	
	/**
	 * 
	 * @param id The identifier to map the first game state to.
	 * @param currentGameState The @see IGameState that should be the first game state to run.
	 */
	public <T>GameStateManager(T id, IGameState currentGameState) {
		this(id, currentGameState, null);
	}
	
	/**
	 * 
	 * @param id The identifier to map the first game state to.
	 * @param currentGameState The @see IGameState that should be the first game state to run.
	 * @param runtimeData The data to pass into game states at run time.
	 */
	public <T, D>GameStateManager(T id, IGameState currentGameState, D runtimeData) {
		gameStateMap = new HashMap<Object, IGameState>();
		gameStateMap.put(id, currentGameState);
		this.currentGameState = currentGameState;
		this.currentGameState.addCompletedEventListener(this);
		this.currentGameState.addFinishEventListener(this);
		this.runtimeData = runtimeData;
		finishListeners = new ArrayList<IFinishListener>();
	}
	
	@Override
	public <T> void completed(T completionData) {
		lastKey = completionData;
		currentGameState(gameStateMap.get(completionData));
	}

	@Override
	public IGameState currentGameState() {
		return currentGameState;
	}

	@Override
	public void currentGameState(IGameState gameState) {
		currentGameState.removeCompletedEventListener(this);
		currentGameState.removeFinishEventListener(this);
		currentGameState = gameState;
		currentGameState.addCompletedEventListener(this);
		currentGameState.addFinishEventListener(this);
		
		if (started) {
			currentGameState.run(runtimeData);
		}
	}

	/**
	 * 
	 * @return A Map of completion data to Game state.
	 */
	protected Map<Object,IGameState> gameStateMap() {
		return gameStateMap;
	}
	
	@Override
	public <T> void addGameState(T identifier, IGameState gameState) {
		gameStateMap.put(identifier, gameState);
	}

	@Override
	public <T> void removeGameState(T identifier) {
		gameStateMap.remove(identifier);
	}

	@Override
	public void clearGameStates() {
		gameStateMap.clear();
		
		if (started) {
			gameStateMap.put(lastKey, currentGameState);
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <D> D runtimeData() {
		return (D)runtimeData;
	}

	@Override
	public <D> void runtimeData(D data) {
		runtimeData = (D)data;
	}
	
	@Override
	public void start() {
		currentGameState.run(runtimeData);
		started = true;
	}

	@Override
	public void finish() {
		for (IFinishListener listener : finishListeners) {
			listener.onFinished();
		}
	}

	@Override
	public void addFinishListener(IFinishListener listener) {
		finishListeners.add(listener);
	}

	@Override
	public void removeFinishListener(IFinishListener listener) {
		finishListeners.remove(listener);
	}

	@Override
	public void onFinished() {
		finish();
	}
}