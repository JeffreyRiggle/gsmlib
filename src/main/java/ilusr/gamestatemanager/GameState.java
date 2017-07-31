package ilusr.gamestatemanager;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class GameState implements IGameState {

	private List<IGameStateListener> completionListeners;
	private List<IFinishListener> finishListeners;
	private Object content;
	
	/**
	 * 
	 * @param content The initial content for the game state.
	 */
	public <T>GameState(T content) {
		this.content = content;
		completionListeners = new ArrayList<IGameStateListener>();
		finishListeners = new ArrayList<IFinishListener>();
	}
	
	@Override
	public void addCompletedEventListener(IGameStateListener listener) {
		completionListeners.add(listener);
	}

	@Override
	public void removeCompletedEventListener(IGameStateListener listener) {
		completionListeners.remove(listener);
	}

	@Override
	public <T>void stateCompleted(T data) {
		// Clone in case the listener removes itself in the completed event.
		List<IGameStateListener> listeners = new ArrayList<IGameStateListener>(completionListeners);
        for (IGameStateListener listener : listeners) {
            listener.completed(data);
        }
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T stateContent() {
		return (T)content;
	}

	@Override
	public <T> void stateContent(T value) {
		content = value;
	}

	@Override
	public <T> void run(T data) {
		
	}
	
	@Override
	public void dispose() {
		if (completionListeners != null) {
			completionListeners.clear();
			completionListeners = null;
		}

		if (finishListeners != null) {
			finishListeners.clear();
			finishListeners = null;
		}
		
		content = null;
	}

	@Override
	public void addFinishEventListener(IFinishListener listener) {
		finishListeners.add(listener);
	}

	@Override
	public void removeFinishEventListener(IFinishListener listener) {
		finishListeners.remove(listener);
	}

	@Override
	public void finish() {
		// Clone in case the listener removes itself in the finished event.
		List<IFinishListener> listeners = new ArrayList<IFinishListener>(finishListeners);
		for (IFinishListener listener : listeners) {
			listener.onFinished();
		}
	}
}