import static org.junit.Assert.*;

import org.junit.Test;

import ilusr.gamestatemanager.GameState;
import ilusr.gamestatemanager.IGameState;

public class GameStateUnitTest {

	private final String _content = "Some Content";
 
	@Test
	public void testCreateGameState() {
		IGameState gameState = new GameState(_content);
		assertEquals(_content, gameState.stateContent());
	}
}