package UnitTests;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Test;

import ilusr.gamestatemanager.BufferOverflowException;
import ilusr.gamestatemanager.BufferedGameStateManager;
import ilusr.gamestatemanager.GameState;
import ilusr.gamestatemanager.IDataSource;
import ilusr.gamestatemanager.IGameState;


public class BufferedGameStateManagerUnitTest {

	@Test
	public void testCreate() {
		IDataSource source = new GameStateProvider(generateGameStates());
		IGameState gameState = new TestGameState(null);
		BufferedGameStateManager manager = new BufferedGameStateManager(5, source, "state1", gameState, null);
		assertNotNull(manager);
	}

	@Test
	public void testAddGameState() {
		IDataSource source = new GameStateProvider(generateGameStates());
		IGameState gameState = new TestGameState(null);
		boolean passed = true;
		BufferedGameStateManager manager = new BufferedGameStateManager(5, source, "state1", gameState, null);
		try {
			manager.addGameState(new TestGameState(null), "bb");
			manager.addGameState(new TestGameState(null), "cc");
			manager.addGameState(new TestGameState(null), "dd");
			manager.addGameState(new TestGameState(null), "ee");
			manager.addGameState(new TestGameState(null), "ff");
		} catch (BufferOverflowException e) {
			passed = false;
		}
		
		assertTrue(passed);
	}
	
	@Test
	public void testAddGameStatePastBufferSize() {
		IDataSource source = new GameStateProvider(generateGameStates());
		IGameState gameState = new TestGameState(null);
		int iter = 0;
		boolean passed = false;
		BufferedGameStateManager manager = new BufferedGameStateManager(5, source, "state1", gameState, null);
		try {
			manager.addGameState(new TestGameState(null), "bb");
			iter++;
			manager.addGameState(new TestGameState(null), "cc");
			iter++;
			manager.addGameState(new TestGameState(null), "dd");
			iter++;
			manager.addGameState(new TestGameState(null), "ee");
			iter++;
			manager.addGameState(new TestGameState(null), "ff");
			iter++;
			manager.addGameState(new TestGameState(null), "gg");
		} catch (BufferOverflowException e) {
			if (iter == 5) {
				passed = true;
			}
		}
		
		assertTrue(passed);
	}
	
	@Test
	public void testCompletion() {
		Map<Object, IGameState> retVal = new HashMap<Object, IGameState>();
		TestGameState missedGameState = new TestGameState(null);
		TestGameState gs1 = new TestGameState(null);
		TestGameState gs2 = new TestGameState(null);
		TestGameState gs3 = new TestGameState(null);
		TestGameState gs4 = new TestGameState(null);
		
		retVal.put("missedGameState", missedGameState);
		retVal.put("gs1", gs1);
		retVal.put("gs2", gs2);
		retVal.put("gs3", gs3);
		retVal.put("gs4", gs4);
		
		
		GameStateProvider source = new GameStateProvider(retVal);
		IGameState gameState = new TestGameState(null);
		BufferedGameStateManager manager = new BufferedGameStateManager(5, source, "state1", gameState, null);
		manager.addGameState("gs3", gs3);
		IGameState gameState2 = new TestGameState(null);
		IGameState gameState3 = new TestGameState(null);
		IGameState gameState4 = new TestGameState(null);
		IGameState gameState5 = new TestGameState(null);
		
		manager.addGameState("state2", gameState2);
		manager.addGameState("state3", gameState3);
		manager.addGameState("state4", gameState4);
		manager.addGameState("state5", gameState5);
		
		manager.start();
		manager.completed("gs3");
		assertEquals(false, source.called());
		assertEquals(true, gs3.ran());
	}
	
	@Test
	public void testCompletionWithMiss() {
		Map<Object, IGameState> retVal = new HashMap<Object, IGameState>();
		TestGameState missedGameState = new TestGameState(null);
		TestGameState gs1 = new TestGameState(null);
		TestGameState gs2 = new TestGameState(null);
		TestGameState gs3 = new TestGameState(null);
		TestGameState gs4 = new TestGameState(null);
		
		retVal.put("missedGameState", missedGameState);
		retVal.put("gs1", gs1);
		retVal.put("gs2", gs2);
		retVal.put("gs3", gs3);
		retVal.put("gs4", gs4);
		
		
		GameStateProvider source = new GameStateProvider(retVal);
		IGameState gameState = new TestGameState(null);
		BufferedGameStateManager manager = new BufferedGameStateManager(5, source, "state1", gameState, null);
		
		IGameState gameState2 = new TestGameState(null);
		IGameState gameState3 = new TestGameState(null);
		IGameState gameState4 = new TestGameState(null);
		IGameState gameState5 = new TestGameState(null);
		
		manager.addGameState("state2", gameState2);
		manager.addGameState("state3", gameState3);
		manager.addGameState("state4", gameState4);
		manager.addGameState("state5", gameState5);
		
		manager.start();
		manager.completed("missedGameState");
		assertEquals(true, source.called());
		assertEquals(true, missedGameState.ran());
	}
	
	private Map<Object, IGameState> generateGameStates() {
		Map<Object, IGameState> retVal = new HashMap<Object, IGameState>();
		TestGameState missedGameState = new TestGameState(null);
		retVal.put("missedGameState", missedGameState);
		return retVal;
	}
	
	private class GameStateProvider implements IDataSource {

		private Map<Object, IGameState> _origin;
		private boolean _called;
		
		public GameStateProvider(Map<Object, IGameState> origin) {
			_origin = origin;
		}
		
		@Override
		public Map<Object, IGameState> provideGameStates(int bufferSize,
				Object state) {
			_called = true;
			int iter = 0;
			Map<Object, IGameState> retVal = new HashMap<Object, IGameState>();
			
			for (Entry<Object, IGameState> entry : _origin.entrySet()) {
				iter++;
				if (iter > bufferSize) break;
				
				retVal.put(entry.getKey(), entry.getValue());
			}
			
			return retVal;
		}
		
		public boolean called() {
			return _called;
		}
	}
	
	private class TestGameState extends GameState {
		
		private boolean _ran;
		
		public <T>TestGameState(T content) {
			super(content);
		}
		
		@Override
		public <T> void run(T data) {
			_ran = true;
		}
		
		public boolean ran() {
			return _ran;
		}
		
		@Override
		public boolean equals(Object obj) {
			if (!(obj instanceof TestGameState)) return false;
			
			if (obj == this) return true;
			
			TestGameState comparer = (TestGameState)obj;
			
			return super.stateContent().equals(comparer.stateContent());
		}
	}
}
