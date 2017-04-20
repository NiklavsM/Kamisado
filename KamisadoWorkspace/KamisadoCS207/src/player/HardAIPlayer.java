package player;

import java.util.HashMap;
import java.util.Random;

import com.sun.glass.ui.Cursor;

import model.Move;
import model.MyObservable;
import model.Piece;
import model.Position;
import model.State;

public class HardAIPlayer extends Player implements MyObservable {

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
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
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
		}if(this.getHomeRow() == 0){
			TreeNode moveTree = new TreeNode(5, workingState, 1);
			Move move = moveTree.getBestOrWorstsChild(false);
			//System.out.println(move);
			tellAll(move.getEndPos());
		}else {
			TreeNode moveTree = new TreeNode(5, workingState, 0);
			Move move = moveTree.getBestOrWorstsChild(true);
			//System.out.println(move);
			tellAll(move.getEndPos());
		}
	}

	@Override
	public void update(MyObservable o, Object arg) {
		// TODO Auto-generated method stub
		
	}
}
