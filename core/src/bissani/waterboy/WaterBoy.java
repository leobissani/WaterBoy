package bissani.waterboy;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.CircleShape;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class WaterBoy extends ApplicationAdapter {

    private Preferences prefs;

	private double variation = 0;
	private double backgroundVariation = 0;
	private double gravitySpeed = 0;

	private BitmapFont font;
	private Random rand = new Random();
	private Timer T;
	private SpriteBatch batch;

	private Rectangle bottomOfSea;
	private Rectangle waterBoy;
	private Circle mine1Shape;
	private Circle mine2Shape;
	private Rectangle barrel1Shape;
	private Rectangle barrel2Shape;
	private ShapeRenderer shape;

	private Texture[] boy;
	private Texture[] background;

	private Texture mine;
    private Texture mine2;
    private Texture barrel;
	private Texture barrel2;
	private Texture menuStart;
	private Texture startGame;
	private Texture gameOver;
	private Texture restartGame;
	private Texture bottomOfSeaMessage;

	private int actualWidth;
	private int actualHeight;
	private int mineHorizontalPosition;
	private int mineVerticalPosition;
	private int mine2HorizontalPosition;
	private int mine2VerticalPosition;
    private int barrelHorizontalPosition;
    private int barrelVerticalPosition;
	private int barrel2HorizontalPosition;
	private int barrel2VerticalPosition;
	private int score = 0;
	private int highestScore = 0;
	private int bottom = 0;
	private int gameState;

    private String scoreText = "Score: ";
    private String highestScoreText = "Highest Score: ";

	private float verticalInitialPosition;
	private float deltaTime;

	private boolean screenTouched;
	private boolean scored;

	@Override
	public void create () {

        batch = new SpriteBatch();

        bottomOfSea = new Rectangle();
        waterBoy = new Rectangle();
        mine1Shape = new Circle();
		mine2Shape = new Circle();
		barrel1Shape = new Rectangle();
		barrel2Shape = new Rectangle();
		shape = new ShapeRenderer();

        font = new BitmapFont();
        font.setColor(Color.GOLD);
        font.getData().setScale(5);

        boy = new Texture[6];
        boy[0] = new Texture("legs1.png");
		boy[1] = new Texture("legs2.png");
		boy[2] = new Texture("legs3.png");
		boy[3] = new Texture("legs4.png");
		boy[4] = new Texture("legs3.png");
		boy[5] = new Texture("legs2.png");

		background = new Texture[4];
		background[0] = new Texture("background.png");
		background[1] = new Texture("background1.png");
		background[2] = new Texture("background2.png");
		background[3] = new Texture("background4.png");

		startGame = new Texture("waterboy.png");
		menuStart = new Texture("startgame.png");
		gameOver = new Texture("gameover.png");
		restartGame = new Texture("restartgame.png");
		bottomOfSeaMessage = new Texture("bottomofsea.png");

		actualWidth = Gdx.graphics.getWidth();
		actualHeight  = Gdx.graphics.getHeight();
		verticalInitialPosition = (actualHeight / 2) - 250;

		mineHorizontalPosition = actualWidth + rand.nextInt(200);
		mineVerticalPosition = actualHeight / 2;
		mine2HorizontalPosition = actualWidth + rand.nextInt(300);
		mine2VerticalPosition = actualHeight / 4;
        barrelHorizontalPosition = actualWidth + rand.nextInt(500);
        barrelVerticalPosition = actualHeight / 3;
		barrel2HorizontalPosition = actualWidth + rand.nextInt(600);
		barrel2VerticalPosition = actualHeight / 5;

		mine = new Texture("mine.png");
		mine2 = new Texture("mine.png");
		barrel = new Texture("barrel.png");
		barrel2 = new Texture("barrel.png");
	}

	@Override
	public void render () {

        Preferences prefs = Gdx.app.getPreferences("My Preferences");
		highestScore = prefs.getInteger("score");

	    screenTouched = Gdx.input.justTouched();
        deltaTime = Gdx.graphics.getDeltaTime();

		variation = variation + deltaTime * 5;
		if(variation > 5) variation = 0;

		backgroundVariation = backgroundVariation + deltaTime * 5;
		if(backgroundVariation > 4) backgroundVariation = 0;

	    if(gameState == 0) {
            if(screenTouched) {
				T = new Timer();
				T.scheduleAtFixedRate(new TimerTask() {
					@Override
					public void run() {
						score++;
					}
				}, 1000, 1000);
                gameState = 1;
            }
        }
        else {

			gravitySpeed = gravitySpeed + 0.08;

			if((verticalInitialPosition > 0 || gravitySpeed < 0) && (verticalInitialPosition < 780 || gravitySpeed > 0)) {
				verticalInitialPosition = verticalInitialPosition - (float)gravitySpeed;
			}

	    	if(gameState == 1) {
				mineHorizontalPosition -= deltaTime * 400;
				mine2HorizontalPosition -= deltaTime * 500;
				barrelHorizontalPosition -= deltaTime * 300;
				barrel2HorizontalPosition -= deltaTime * 200;

				if(screenTouched) {
					gravitySpeed = -4;
				}

				if(mineHorizontalPosition < -mine.getWidth()) {
					mineHorizontalPosition = actualWidth;
					mineVerticalPosition = rand.nextInt(800);
					scored = false;
				}

				if(mine2HorizontalPosition < -mine2.getWidth()) {
					mine2HorizontalPosition = actualWidth;
					mine2VerticalPosition = rand.nextInt(800);
					scored = false;
				}

				if(barrelHorizontalPosition < -barrel.getWidth()) {
					barrelHorizontalPosition = actualWidth;
					barrelVerticalPosition = rand.nextInt(800);
				}

				if(barrel2HorizontalPosition < -barrel.getWidth()) {
					barrel2HorizontalPosition = actualWidth;
					barrel2VerticalPosition = rand.nextInt(800);
				}
			}
			else {
				T.cancel();
				if(score > highestScore) {
					highestScore = score;
					prefs.putInteger("score", highestScore);
				}
				prefs.flush();

				if(screenTouched) {
					bottom = 0;
					gameState = 0;
					variation = 0;
					gravitySpeed = 0;
					verticalInitialPosition = actualHeight / 2;
					mineHorizontalPosition = actualWidth + 100;
					mineVerticalPosition = actualHeight / 2;
					mine2HorizontalPosition = actualWidth + 300;
					mine2VerticalPosition = actualHeight / 4;
					barrelHorizontalPosition = actualWidth + 500;
					barrelVerticalPosition = actualHeight / 3;
					barrel2HorizontalPosition = actualWidth + 600;
					barrel2VerticalPosition = actualHeight / 5;
					score = 0;
				}
			}
        }

	    batch.begin();

		batch.draw(background[(int)backgroundVariation], 0, 0, actualWidth, actualHeight);
        batch.draw(mine, mineHorizontalPosition, mineVerticalPosition);
		batch.draw(mine2, mine2HorizontalPosition, mine2VerticalPosition);
        batch.draw(barrel, barrelHorizontalPosition, barrelVerticalPosition);
		batch.draw(barrel2, barrel2HorizontalPosition, barrel2VerticalPosition);
		batch.draw(boy[(int)variation], 150, verticalInitialPosition);
        font.draw(batch, scoreText + String.valueOf(score), actualWidth - 1750, actualHeight - 25);
        font.draw(batch, highestScoreText + String.valueOf(highestScore),actualWidth - 1300, actualHeight - 25);

        if(gameState == 0) {
			batch.draw(startGame, actualWidth / 2 - gameOver.getHeight() - 180, actualHeight / 2);
			batch.draw(menuStart, actualWidth / 2 - gameOver.getHeight(), actualHeight / 4);
		}

        if(gameState == 2) {
			batch.draw(gameOver, actualWidth / 2 - gameOver.getHeight(), actualHeight / 2);
			batch.draw(restartGame, actualWidth / 2 - gameOver.getHeight() - 140, actualHeight / 4);
		}

		if(gameState == 3) {
			batch.draw(gameOver, actualWidth / 2 - gameOver.getHeight(), actualHeight / 2);
			batch.draw(restartGame, actualWidth / 2 - gameOver.getHeight() - 140, actualHeight / 4);
			batch.draw(bottomOfSeaMessage, 570, 20);
		}

        batch.end();

        waterBoy.set(150, verticalInitialPosition + 60, boy[0].getWidth() - 50, boy[0].getHeight() - 80);
		mine1Shape.set(mineHorizontalPosition + 75, mineVerticalPosition + 75, mine.getHeight()/3);
		mine2Shape.set(mine2HorizontalPosition + 75, mine2VerticalPosition + 75, mine2.getHeight()/3);
		barrel1Shape.set(barrelHorizontalPosition, barrelVerticalPosition, barrel.getWidth(), barrel.getHeight());
		barrel2Shape.set(barrel2HorizontalPosition, barrel2VerticalPosition, barrel.getWidth(), barrel2.getHeight());
		bottomOfSea.set(0, 40, actualWidth, 20);

		if( Intersector.overlaps(waterBoy, barrel1Shape) || Intersector.overlaps(waterBoy, barrel2Shape) || Intersector.overlaps(mine1Shape, waterBoy) ||  Intersector.overlaps(mine2Shape, waterBoy)) {
			gameState = 2;
		}

		if(Intersector.overlaps(waterBoy, bottomOfSea)) {
			bottom++;
			if(bottom >= 300)
				gameState = 3;
		}

		if(!(Intersector.overlaps(waterBoy, bottomOfSea))) {
			bottom = 0;
		}
		/*
        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.circle(mine1Shape.x, mine1Shape.y, mine1Shape.radius);
		shape.circle(mine2Shape.x, mine2Shape.y, mine2Shape.radius);
        shape.rect(waterBoy.x, waterBoy.y, waterBoy.width, waterBoy.height);
		shape.rect(barrel1Shape.x, barrel1Shape.y, barrel1Shape.width, barrel1Shape.height);
		shape.rect(barrel2Shape.x, barrel2Shape.y, barrel2Shape.width, barrel2Shape.height);
		shape.rect(bottomOfSea.x, bottomOfSea.y, bottomOfSea.width, bottomOfSea.height);
        shape.end();
        */
	}
}
