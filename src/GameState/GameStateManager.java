package GameState;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class GameStateManager {
	
	private GameState[] gameStates;
	private int currentState;
	
	public static final int NUMGAMESTATES = 6;
	public static final int MENUSTATE = 0;
	public static final int LEVEL1STATE = 1;
	public static final int CREDITS = 2;
	public static final int SCORESTATE = 4;
	
	public GameStateManager() {
		
		gameStates = new GameState[NUMGAMESTATES];
		
		currentState = MENUSTATE;
		loadState(currentState);
	}
	
	private void loadState(int state) {
		if(state == MENUSTATE)
			gameStates[state] = new MenuState(this);
		if(state == LEVEL1STATE)
			gameStates[state] = new Level1State(this);
		if(state == CREDITS)
			gameStates[state] = new CreditsState(this);
	}
	
	private void unloadState(int state) {
		gameStates[state] = null;
	}

	public void setScoreState(long time, int enemy, int hp, String data) {
		unloadState(currentState);
		currentState = SCORESTATE;
		gameStates[SCORESTATE] = new ScoreState(this, time/60, enemy, hp, data);
	}
	
	public void setState(int state) {
		unloadState(currentState);
		currentState = state;
		loadState(currentState);
		//gameStates[currentState].init();
	}
	
	public void update() {
		try {
			gameStates[currentState].update();
		} catch(Exception e) {}
	}
	
	public void draw(java.awt.Graphics2D g) {
		try {
			gameStates[currentState].draw(g);
		} catch(Exception e) {}
	}
	
	public void keyPressed(KeyEvent k) {
		try {
			gameStates[currentState].keyPressed(k);
		} catch (NullPointerException e) {
		}
	}
	
	public void keyReleased(int k) {
		try {
			gameStates[currentState].keyReleased(k);
		} catch (NullPointerException e) {
		}
	}
	
}









