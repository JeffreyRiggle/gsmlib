import static org.junit.Assert.*;

import org.junit.Test;

import ilusr.gamestatemanager.GameState;
import ilusr.gamestatemanager.GameStateManager;
import ilusr.gamestatemanager.IFinishListener;
import ilusr.gamestatemanager.IGameStateManager;


public class GameStateManagerUnitTest {

	private final TestGameState _gameState1 = new TestGameState("Content", "2");
	private final TestGameState _gameState2 = new TestGameState(12, "3");
	private final TestGameState _gameState3 = new TestGameState("Content2", "4");
	private final TestGameState _gameState4 = new TestGameState(100, "5");
	private final TestGameState _gameState5 = new TestGameState(true, "1");
	private final String _id1 = "1";
	private final String _id2 = "2";
	private final String _id3 = "3";
	private final String _id4 = "4";
	private final String _id5 = "5";
	
	@Test
	public void createGameStateManager() {
		IGameStateManager gameStateManager = new GameStateManager(_id1, _gameState1);
		assertEquals(_gameState1, gameStateManager.currentGameState());
		assertNull(gameStateManager.runtimeData());
	}
	
	@Test
	public void createGameStateManagerFull() {
		IGameStateManager gameStateManager = new GameStateManager(_id1, _gameState1, 15);
		assertEquals(_gameState1, gameStateManager.currentGameState());
		assertEquals(new Integer(15), gameStateManager.<Integer>runtimeData());
	}
	
	@Test
	public void addGameState() {
		IGameStateManager gameStateManager = new GameStateManager(_id1, _gameState1);
		gameStateManager.addGameState(_id2, _gameState2);
		gameStateManager.addGameState(_id3, _gameState3);
		gameStateManager.addGameState(_id4, _gameState4);
		gameStateManager.addGameState(_id5, _gameState5);
	}
	
	@Test
	public void clearGameState() {
		IGameStateManager gameStateManager = new GameStateManager(_id1, _gameState1);
		gameStateManager.addGameState(_id2, _gameState2);
		gameStateManager.addGameState(_id3, _gameState3);
		gameStateManager.addGameState(_id4, _gameState4);
		gameStateManager.addGameState(_id5, _gameState5);
		gameStateManager.clearGameStates();
		assertEquals(_gameState1, gameStateManager.currentGameState());
	}
	
	@Test
	public void testSetRuntimeData() {
		IGameStateManager gameStateManager = new GameStateManager(_id1, _gameState1);
		assertNull(gameStateManager.runtimeData());
		
		gameStateManager.<String>runtimeData("SomeData");
		assertEquals("SomeData", gameStateManager.<String>runtimeData());
		
		gameStateManager.<Integer>runtimeData(15);
		assertEquals(new Integer(15), gameStateManager.<Integer>runtimeData());
	}
	
	@Test
	public void testStartGameStateManager() {
		IGameStateManager gameStateManager = new GameStateManager(_id1, _gameState1, "SomeData");
		gameStateManager.addGameState(_id2, _gameState2);
		gameStateManager.addGameState(_id3, _gameState3);
		gameStateManager.addGameState(_id4, _gameState4);
		gameStateManager.addGameState(_id5, _gameState5);
		
		gameStateManager.start();
		assertEquals(true, _gameState1.ran());
		assertEquals("SomeData", _gameState1.runtimeData());
	}
	
	@Test
	public void testGameStateCompletion() {
		IGameStateManager gameStateManager = new GameStateManager(_id1, _gameState1, "SomeData");
		gameStateManager.addGameState(_id2, _gameState2);
		gameStateManager.addGameState(_id3, _gameState3);
		gameStateManager.addGameState(_id4, _gameState4);
		gameStateManager.addGameState(_id5, _gameState5);
		
		gameStateManager.start();
		
		_gameState1.complete();
		assertEquals(true, _gameState2.ran());
		
		_gameState2.complete();
		assertEquals(true, _gameState3.ran());
		
		_gameState3.complete();
		assertEquals(true, _gameState4.ran());
		
		_gameState4.complete();
		assertEquals(true, _gameState5.ran());
		
		_gameState5.complete();
		assertEquals(true, _gameState1.ran());
	}
	
	@Test
	public void testGameStateCompletionWithChangingData() {
		IGameStateManager gameStateManager = new GameStateManager(_id1, _gameState1, "SomeData");
		gameStateManager.addGameState(_id2, _gameState2);
		gameStateManager.addGameState(_id3, _gameState3);
		gameStateManager.addGameState(_id4, _gameState4);
		gameStateManager.addGameState(_id5, _gameState5);
		
		gameStateManager.start();
		assertEquals("SomeData", _gameState1.runtimeData());
		
		gameStateManager.runtimeData(15);
		_gameState1.complete();
		assertEquals(_gameState2, gameStateManager.currentGameState());
		assertEquals(new Integer(15), _gameState2.<Integer>runtimeData());
		
		gameStateManager.runtimeData(false);
		_gameState2.complete();
		assertEquals(_gameState3, gameStateManager.currentGameState());
		assertEquals(new Boolean(false), _gameState3.<Boolean>runtimeData());
		
		gameStateManager.runtimeData("SomeOtherData");
		_gameState3.complete();
		assertEquals(_gameState4, gameStateManager.currentGameState());
		assertEquals("SomeOtherData", _gameState4.runtimeData());
		
		_gameState4.complete();
		assertEquals(_gameState5, gameStateManager.currentGameState());
		assertEquals("SomeOtherData", _gameState5.runtimeData());
		
		_gameState5.complete();
		assertEquals(_gameState1, gameStateManager.currentGameState());
		assertEquals("SomeOtherData", _gameState1.runtimeData());
	}
	
	@Test
	public void testGameManagerFinished() {
		GameState state = new TestGameState(null, "Test");
		IGameStateManager gameStateManager = new GameStateManager(_id1, state, "SomeData");
		gameStateManager.start();
		FinishListener listener = new FinishListener();
		gameStateManager.addFinishListener(listener);
		gameStateManager.finish();
		assertTrue(listener.finished());
	}
	
	@Test
	public void testGameManagerFinishedWithNoListeners() {
		GameState state = new TestGameState(null, "Test");
		IGameStateManager gameStateManager = new GameStateManager(_id1, state, "SomeData");
		gameStateManager.start();
		FinishListener listener = new FinishListener();
		gameStateManager.addFinishListener(listener);
		gameStateManager.removeFinishListener(listener);
		gameStateManager.finish();
		assertFalse(listener.finished());
	}
	
	@Test
	public void testGameManagerFinishedFromGameState() {
		GameState state = new TestGameState(null, "Test");
		IGameStateManager gameStateManager = new GameStateManager(_id1, state, "SomeData");
		gameStateManager.start();
		FinishListener listener = new FinishListener();
		gameStateManager.addFinishListener(listener);
		state.finish();
		assertTrue(listener.finished());
	}
	
	@Test
	public void testGameManagerFinishedFromGameStateWithNoListeners() {
		GameState state = new TestGameState(null, "Test");
		IGameStateManager gameStateManager = new GameStateManager(_id1, state, "SomeData");
		gameStateManager.start();
		FinishListener listener = new FinishListener();
		gameStateManager.addFinishListener(listener);
		gameStateManager.removeFinishListener(listener);
		state.finish();
		assertFalse(listener.finished());
	}
	
	private class TestGameState extends GameState {
		
		private boolean _ran;
		private Object _runtimeData;
		private String _completionData;
		
		public <T> TestGameState(T content, String completionData) {
			super(content);
			_ran = false;
			_completionData = completionData;
		}
		
		public boolean ran() {
			return _ran;
		}
		
		public void complete() {
			super.stateCompleted(_completionData);
		}
		
		@SuppressWarnings("unchecked")
		public <D> D runtimeData() {
			return (D)_runtimeData;
		}
		
		public <D> void run(D data) {
			super.run(data);
			_ran = true;
			_runtimeData = (D)data;
		}
	}
	
	private class FinishListener implements IFinishListener {
		private boolean finished;
		
		public FinishListener() {
			finished = false;
		}
		
		@Override
		public void onFinished() {
			finished = true;
		}
		
		public boolean finished() {
			return finished;
		}
	}
}