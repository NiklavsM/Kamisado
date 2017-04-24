package player;

import java.util.HashMap;
import java.util.Random;

import model.Move;
import model.MyObservable;
import model.Position;
import model.State;

public class HardAIPlayer extends Player implements MyObservable {

	private static final long serialVersionUID = 1L;
	private int timesVisited = 0;
	private HashMap<Position, Position> bestOpeningMoves;
	private Position firstMove;
	private State workingState;

	public HardAIPlayer(String playerTeam, String playerName, boolean goingFist) {
		super(playerTeam, playerName, goingFist, true);
		bestOpeningMoves = new HashMap<>();
		initialiseBestMoves();
	}

	private void initialiseBestMoves() {
		Random rnd = new Random();
		int num = rnd.nextInt(2) + 3;
		firstMove = new Position(num, getHomeRow());
		bestOpeningMoves.put(firstMove, new Position(num, 4));
	}

	@Override
	public void getMove(State state) {
		workingState = state;
	}

	@Override
	public int fillHomeRow() {
		Random rnd = new Random();
		int num = rnd.nextInt(2);
		return num;
	}

	public void setToFirstMove(boolean isGoingFirst) {
		initialiseBestMoves();
		timesVisited = 0;
		this.setGoingFirst(isGoingFirst);
	}

	@Override
	public void run() {
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if (getisFirst()) {
			if (timesVisited == 0) {
				timesVisited++;
				tellAll(firstMove);
				return;
			} else if (timesVisited == 1) {
				timesVisited++;
				tellAll(new Position(bestOpeningMoves.get(firstMove)));
				return;
			}
		}
		if (this.getHomeRow() == 7) {
			TreeNode moveTree = new TreeNode(4, workingState, 1); 
			Move move = moveTree.getBestOrWorstsChild(false);
			tellAll(move.getEndPos());
		} else {
			TreeNode moveTree = new TreeNode(4, workingState, 0);
			Move move = moveTree.getBestOrWorstsChild(true);
			tellAll(move.getEndPos());
		}
	}

	@Override
	public void update(MyObservable o, Object arg) {

	}
}
