package com.pearl.game.screen;

import java.util.Iterator;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.pearl.game.assets.Assets;
import com.pearl.game.entities.Direction;
import com.pearl.game.entities.Score;
import com.pearl.game.entities.Cell;
import com.pearl.game.screen.group.GameLoseGroup;
import com.pearl.game.screen.group.GameWinGroup;
import com.pearl.game.utils.Constants;
import com.pearl.game.utils.DrawCell;
import com.pearl.game.utils.Prefs;

public class GameScreen implements Screen {

	private enum GameState {
		WIN, LOSE, PLAYING, PAUSE, EXIT
	}

	private GameState state;

	private Game game;
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private Stage stage;

	private Cell[][] board;
	private Array<Cell> shouldRemoveSquareActors;
	private Array<Cell> shouldDoubleSquareActors;

	private int emptyCellCount;
	private int maxValue;

	private boolean touchInBoard;
	private boolean isMove;

	private Vector2 drag;
	private Direction direction;

	private Group gameWinGroup;
	private Group gameLoseGroup;
	int call = 0;

	public GameScreen(Game pearl) {
		// TODO Auto-generated constructor stub
		this.game = pearl;
		init();
	}

	private void init() {
		camera = new OrthographicCamera(Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT);
		camera.position.set(Constants.VIEWPORT_WIDTH / 2, Constants.VIEWPORT_HEIGHT / 2, 0);
		camera.update();
		batch = new SpriteBatch();
		stage = new Stage(new FitViewport(Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT, camera), batch);
		Gdx.input.setInputProcessor(stage);

		initBoard();

		shouldRemoveSquareActors = new Array<Cell>();
		shouldDoubleSquareActors = new Array<Cell>();

		addEvent();
		addGroupButton();

		drag = new Vector2();
	}

	private void addGroupButton() {
		/*
		 * Add new and Exit Button; texture of it will be draw at render
		 *  Don't ask for why it has this coordinate =))
		 */
		Group group = new Group();
		{
			Actor actor = new Actor();
			actor.setBounds(30, 465, Assets.instance.newL.getRegionWidth(), Assets.instance.newL.getRegionWidth());
			actor.addListener(new ClickListener() {
				@Override
				public void clicked(InputEvent event, float x, float y) {
					// TODO Auto-generated method stub
					restartGame();
					super.clicked(event, x, y);
				}
			});
			group.addActor(actor);
		}
		{

			Actor actor = new Actor();
			actor.setBounds(110, 465, Assets.instance.newL.getRegionWidth(), Assets.instance.newL.getRegionWidth());
			actor.addListener(new ClickListener() {
				@Override
				public void clicked(InputEvent event, float x, float y) {
					// TODO Auto-generated method stub
					Gdx.app.exit();
					super.clicked(event, x, y);
				}
			});
			group.addActor(actor);

		}
		stage.addActor(group);
	}

	private void initBoard() {
		emptyCellCount = 16;

		board = new Cell[4][4];
		if (Prefs.instance.isLaunchNewGame)
			initDataForBoard();
		else
			loadDataFromPrefs();
		
	}

	private void initDataForBoard() {

		for (int i = 0; i < 2; i++)
			addCell();

		isMove = false;
		state = GameState.PLAYING;
		Score.instance.setScore(0);
		maxValue = 0;

	}

	
	public void loadDataFromPrefs() {

		String[] tmp = Prefs.instance.board.split(" ");
		maxValue = 0;
		for (int i = 0; i < 4; i++)
			for (int j = 0; j < 4; j++) {

				if (!tmp[i * 4 + j].equals("0")) {
					addCell(Integer.parseInt(tmp[i * 4 + j]), i, j);
					if (Integer.parseInt(tmp[i * 4 + j]) > maxValue)
						maxValue = Integer.parseInt(tmp[i * 4 + j]);
					emptyCellCount--;
				}
			}

		isMove = false;
		state = GameState.PLAYING;

	}

	public void restartGame() {
		// clear cell in stage
		earseCellActorInStage();
		initDataForBoard();
	}

	private void earseCellActorInStage() {
		for (int i = 0; i < 4; i++)
			for (int j = 0; j < 4; j++)
				if (board[i][j] != null) {

					stage.getRoot().removeActor(board[i][j]);
					board[i][j] = null;
				}
		emptyCellCount = 16;
	}

	private void addCell() {
		if (emptyCellCount > 0) {
			
			/*
			 * choose random pos at null cell of board, after,we find pos in board, and random value for it
			 * call addCell(value, posX, posY) to add to board.
			 */
			int pos = MathUtils.random(emptyCellCount - 1);
			boolean findEmpty = false;
			int i = 0;
			int j = 0;
			int d = 0;
			for (i = 0; i < 4 && !findEmpty; i++)
				for (j = 0; j < 4 && !findEmpty; j++) {
					if (board[i][j] == null) {
						if (d == pos) {
							findEmpty = true;
						}
						d++;
					}
				}
			i--;
			j--;
			int value;
			if (MathUtils.randomBoolean(0.9f)) {
				value = 2;
			} else {
				value = 4;
			}

			addCell(value, i, j);
			emptyCellCount--;
		}
	}

	private void addCell(int value, int boardX, int boardY) {
		Cell actor = new Cell(value, boardX, boardY);
		board[boardX][boardY] = actor;
		stage.addActor(actor);
		printBoard();
	}

	public void addEvent() {
		stage.addListener(new InputListener() {

			@Override
			public boolean keyDown(InputEvent event, int keycode) {
				// TODO Auto-generated method stub
				/*
				 * Event is add for keyboard/desktop Game;
				 */
				if (state == GameState.PLAYING) {
					if (keycode == Keys.UP) {
						direction = Direction.UP;
						if (isCanMoveWithDirection()) {
							touchDragToUp();
							isMove = true;
						}
					} else if (keycode == Keys.DOWN) {
						direction = Direction.DOWN;
						if (isCanMoveWithDirection()) {
							touchDragToDown();
							isMove = true;
						}

					} else if (keycode == Keys.RIGHT) {
						direction = Direction.RIGHT;
						if (isCanMoveWithDirection()) {
							touchDragToRight();
							isMove = true;
						}
					} else if (keycode == Keys.LEFT) {
						direction = Direction.LEFT;
						if (isCanMoveWithDirection()) {
							touchDragToLeft();
							isMove = true;
						}
					}
				}
				return super.keyDown(event, keycode);
			}

			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				// TODO Auto-generated method stub
				if (state == GameState.PLAYING)
					// check x,y in board
					if (Constants.BOARD_POS_BOT <= x && x <= Constants.BOARD_POS_TOP && Constants.BOARD_POS_BOT <= y && y <= Constants.BOARD_POS_TOP) {
						drag.set(x, y);
						touchInBoard = true;
						return true;
					}
				return super.touchDown(event, x, y, pointer, button);
			}

			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				// TODO Auto-generated method stub
				if ((x != drag.x || y != drag.y) && touchInBoard) {
					drag.sub(x, y);
					drag.scl(-1);
					checkDirection();
					if (isCanMoveWithDirection()) {
						if (direction == Direction.LEFT)
							touchDragToLeft();
						else if (direction == Direction.RIGHT)
							touchDragToRight();
						else if (direction == Direction.DOWN)
							touchDragToDown();
						else if (direction == Direction.UP)
							touchDragToUp();
						isMove = true;
					}
				}
				touchInBoard = false;
				super.touchUp(event, x, y, pointer, button);
			}
		});
	}

	private void checkDirection() {
		// check touch direction
		if (drag.angle() > 45 && drag.angle() < 135) 
			direction = Direction.UP;
		else if (drag.angle() > 135 && drag.angle() < 225)
			direction = Direction.LEFT;
		else if (drag.angle() > 225 && drag.angle() < 305)
			direction = Direction.DOWN;
		else if (drag.angle() > 305 || drag.angle() < 45)
			direction = Direction.RIGHT;

	}

	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		if (state != GameState.EXIT) {
			batch.setProjectionMatrix(camera.combined);
			update(delta);
			batch.begin();
			// draw background
			batch.draw(Assets.instance.backgroundL, 0, 0);
			// draw newLabel and exitLabel
			// Don't ask for why it has this coordinate =))
			batch.draw(Assets.instance.newL, 30, 465);
			batch.draw(Assets.instance.exitL, 110 - 0.29f * Assets.instance.exitL.getRegionWidth() / 2, 465 - Assets.instance.exitL.getRegionHeight() * 0.29f,
					Assets.instance.exitL.getRegionWidth() / 2, Assets.instance.exitL.getRegionHeight(), Assets.instance.exitL.getRegionWidth(),
					Assets.instance.exitL.getRegionHeight(), 0.71f, 0.71f, 0);

			// draw Score and best Score
			Assets.instance.font.setColor(Color.WHITE);
			Assets.instance.font.setScale(0.5f);
			Assets.instance.font.draw(batch, Integer.toString(Score.instance.getScore()),
					291 - Assets.instance.font.getBounds(Integer.toString(Score.instance.getScore())).width / 2,
					592 + Assets.instance.font.getBounds(Integer.toString(Score.instance.getScore())).height);
			Assets.instance.font.draw(batch, Integer.toString(Score.instance.getBest()),
					403 - Assets.instance.font.getBounds(Integer.toString(Score.instance.getBest())).width / 2,
					592 + Assets.instance.font.getBounds(Integer.toString(Score.instance.getBest())).height);

			batch.end();
			stage.draw();
		}

	}

	private void update(float delta) {
		stage.act(delta);
		if (state == GameState.PLAYING) {

			{
				if (isMove) {
					// check cell is move to exaclty position
					boolean tmpIsMoved = true;
					for (int i = 0; i < 4; i++)
						for (int j = 0; j < 4; j++)
							if (board[i][j] != null)
								if (board[i][j].hasMoved() == false)
									tmpIsMoved = false;
					{
						Iterator<Cell> iters = shouldRemoveSquareActors.iterator();
						while (iters.hasNext()) {
							if (iters.next().hasMoved() == false)
								tmpIsMoved = false;
						}
					}
					if (tmpIsMoved) {
						{
							// remove cell and double value
							printBoard();
							Iterator<Cell> iters = shouldRemoveSquareActors.iterator();
							while (iters.hasNext()) {
								Cell actor = iters.next();
								stage.getRoot().removeActor(actor);
								iters.remove();
							}
						}
						{
							Iterator<Cell> iters = shouldDoubleSquareActors.iterator();
							while (iters.hasNext()) {
								Cell actor = iters.next();
								actor.doubleValue();
								maxValue = actor.getValue() > maxValue ? actor.getValue() : maxValue;
								iters.remove();
							}
						}
						// check win and prepare for new turn
						addCell();
						isMove = false;
						printBoard();
						if (maxValue == 2048) {
							gameWin();
						} else {
							if (!isBoardCanMove()) {
								gameLose();
							}
						}
					}
				}

			}
		}
	}

	private void gameWin() {

		state = GameState.WIN;
		gameWinGroup = new GameWinGroup(this);
		stage.addActor(gameWinGroup);

	}

	private void gameLose() {
		state = GameState.LOSE;
		gameLoseGroup = new GameLoseGroup(this);
		stage.addActor(gameLoseGroup);
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		// it is apply for libgd 1.0.0; it miss it,input event game can not be
		// catch
		stage.getViewport().update(width, height);
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		// it is call when game exit
		if (state == GameState.PLAYING) {
			String convertBoardToString = "";
			for (int i = 0; i < 4; i++)
				for (int j = 0; j < 4; j++) {
					if (board[i][j] != null)
						convertBoardToString += (Integer.toString(board[i][j].getValue())) + " ";
					else
						convertBoardToString += ("0 ");
				}
			Prefs.instance.saveBoard(convertBoardToString, false);
		}

		else {
			Prefs.instance.saveBoard("", true);
		}

		state = GameState.EXIT;

		batch.dispose();
		stage.dispose();

		Assets.instance.dispose();
		DrawCell.instance.dispose();
		Prefs.instance.saveScore();

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	private void printBoard() {
		System.out.println("\n========================================");
		for (int i = 0; i < 4; i++) {
			System.out.println();
			for (int j = 0; j < 4; j++)
				if (board[i][j] == null)
					System.out.print(" 0 ");
				else
					System.out.print(" " + board[i][j].getValue() + " ");
		}
		System.out.println("\n" + stage.getActors().size);
		System.out.println("========================================\n");
	}

	
	private void touchDragToLeft() {
		for (int i = 0; i < 4; i++) {
			Cell lastActor = null;
			int adjustPosition = 0;
			for (int j = 0; j < 4; j++) {
				if (board[i][j] == null)
					continue;
				else {
					if (lastActor == null) {

						if (adjustPosition != j) {
							board[i][adjustPosition] = board[i][j];
							board[i][j] = null;
							board[i][adjustPosition].moveTo(i, adjustPosition, j - adjustPosition);
						}
						lastActor = board[i][adjustPosition];

						adjustPosition++;
					} else {
						if (lastActor.getValue() != board[i][j].getValue()) {
							if (adjustPosition != j) {
								board[i][adjustPosition] = board[i][j];
								board[i][j] = null;
								board[i][adjustPosition].moveTo(i, adjustPosition, j - adjustPosition);
							}
							lastActor = board[i][adjustPosition];

							adjustPosition++;

						} else {
							board[i][j].moveTo(lastActor.getBoardX(), lastActor.getBoardY(), j - lastActor.getBoardY());
							shouldDoubleSquareActors.add(lastActor);
							Score.instance.addScore(board[i][j].getValue());
							shouldRemoveSquareActors.add(board[i][j]);
							emptyCellCount++;

							lastActor = null;
							board[i][j] = null;

						}
					}

				}
			}
		}

	}

	private void touchDragToRight() {
		for (int i = 0; i < 4; i++) {
			Cell lastActor = null;
			int adjustPosition = 3;
			for (int j = 3; j >= 0; j--) {
				if (board[i][j] == null)
					continue;
				else {
					if (lastActor == null) {

						if (adjustPosition != j) {
							board[i][adjustPosition] = board[i][j];
							board[i][j] = null;
							board[i][adjustPosition].moveTo(i, adjustPosition, adjustPosition - j);
						}
						lastActor = board[i][adjustPosition];

						adjustPosition--;
					} else {
						if (lastActor.getValue() != board[i][j].getValue()) {
							if (adjustPosition != j) {
								board[i][adjustPosition] = board[i][j];
								board[i][j] = null;
								board[i][adjustPosition].moveTo(i, adjustPosition, adjustPosition - j);
							}
							lastActor = board[i][adjustPosition];
							adjustPosition--;

						} else {
							board[i][j].moveTo(lastActor.getBoardX(), lastActor.getBoardY(), lastActor.getBoardY() - j);

							shouldRemoveSquareActors.add(board[i][j]);
							Score.instance.addScore(board[i][j].getValue());
							shouldDoubleSquareActors.add(lastActor);
							emptyCellCount++;
							lastActor = null;
							board[i][j] = null;
							printBoard();
						}
					}

				}
			}
		}
	}

	private void touchDragToDown() {
		for (int j = 0; j < 4; j++) {
			Cell lastActor = null;
			int adjustPosition = 3;
			for (int i = 3; i >= 0; i--) {
				if (board[i][j] == null)
					continue;
				else {
					if (lastActor == null) {
						if (adjustPosition != i) {
							board[adjustPosition][j] = board[i][j];
							board[i][j] = null;
							board[adjustPosition][j].moveTo(adjustPosition, j, adjustPosition - i);
						}
						lastActor = board[adjustPosition][j];
						adjustPosition--;
					} else {
						if (lastActor.getValue() != board[i][j].getValue()) {

							if (adjustPosition != i) {
								
								board[adjustPosition][j] = board[i][j];
								board[i][j] = null;
								board[adjustPosition][j].moveTo(adjustPosition, j, adjustPosition - i);
							}
							lastActor = board[adjustPosition][j];
							adjustPosition--;

						} else {
							
							board[i][j].moveTo(lastActor.getBoardX(), lastActor.getBoardY(), lastActor.getBoardX() - i);
							shouldRemoveSquareActors.add(board[i][j]);
							Score.instance.addScore(board[i][j].getValue());
							shouldDoubleSquareActors.add(lastActor);
							emptyCellCount++;
							lastActor = null;
							board[i][j] = null;

						}
					}

				}
			}
		}
	}

	private void touchDragToUp() {
		for (int j = 0; j < 4; j++) {
			Cell lastActor = null;
			int adjustPosition = 0;
			for (int i = 0; i < 4; i++) {
				if (board[i][j] == null)
					continue;
				else {
					if (lastActor == null) {
						if (adjustPosition != i) {
							board[adjustPosition][j] = board[i][j];
							board[i][j] = null;
							board[adjustPosition][j].moveTo(adjustPosition, j, i - adjustPosition);
						}
						lastActor = board[adjustPosition][j];
						adjustPosition++;
					} else {
						if (lastActor.getValue() != board[i][j].getValue()) {
							if (adjustPosition != i) {
								board[adjustPosition][j] = board[i][j];
								board[i][j] = null;
								board[adjustPosition][j].moveTo(adjustPosition, j, i - adjustPosition);
							}
							lastActor = board[adjustPosition][j];
							adjustPosition++;
						} else {
							board[i][j].moveTo(lastActor.getBoardX(), lastActor.getBoardY(), i - lastActor.getBoardX());
							Score.instance.addScore(board[i][j].getValue());
							shouldRemoveSquareActors.add(board[i][j]);
							shouldDoubleSquareActors.add(lastActor);
							emptyCellCount++;
							lastActor = null;
							board[i][j] = null;

						}
					}

				}
			}
		}
	}

	private Vector2 findFarthestCell(Cell actor, Direction dir) {
		int i = actor.getBoardX() + dir.getX();
		int j = actor.getBoardY() + dir.getY();
		while (isInBoard(i, j)) {
			if (board[i][j] == null) {
				i += dir.getX();
				j += dir.getY();
			} else {
				return new Vector2(i, j);
			}
		}
		return null;
	}

	private boolean isBoardCanMove() {

		return emptyCellCount > 0 || isTileMatchesAvaiable();
	}

	private boolean isTileMatchesAvaiable() {
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				if (board[i][j] != null) {
					for (Direction d : Direction.values()) {
						if (isInBoard(i - d.getX(), j - d.getY()))
							if (board[i - d.getX()][j - d.getY()] != null) {
								if (board[i - d.getX()][j - d.getY()].getValue() == board[i][j].getValue())
									return true;
							}
					}

				}

			}
		}

		return false;
	}

	private boolean isCanMoveWithDirection() {
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				if (board[i][j] != null) {
					if (findFarthestCell(board[i][j], direction) != null) {

						if (board[(int) findFarthestCell(board[i][j], direction).x][(int) findFarthestCell(board[i][j], direction).y].getValue() == board[i][j]
								.getValue()) {
							return true;
						} else {
							if (Math.abs(findFarthestCell(board[i][j], direction).x - i) > 1)
								return true;
							if (Math.abs(findFarthestCell(board[i][j], direction).y - j) > 1)
								return true;
						}
					} else {
						if (isInBoard(i + direction.getX(), j + direction.getY())) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	private boolean isInBoard(int i, int j) {
		return i < 4 && i >= 0 && j < 4 && j >= 0;
	}

	public Stage getStage() {
		return stage;
	}

	public Group getGameWinGroup() {
		return gameWinGroup;
	}

	public Group getGameLoseGroup() {
		return gameLoseGroup;
	}

	public Game getGame() {
		return game;
	}

}
