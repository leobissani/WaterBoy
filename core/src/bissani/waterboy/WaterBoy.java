package bissani.waterboy;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.Random;

public class WaterBoy extends ApplicationAdapter {

	private int actualWidth;
	private int actualHeight;
	Random rand = new Random();

	private SpriteBatch batch;

	private Texture[] boy;
	private double variation = 0;

	private Texture mine;
    private Texture mine2;
    private Texture barrel;
	private Texture barrel2;

	private int mineHorizontalPosition;
	private int mineVerticalPosition;
	private int mine2HorizontalPosition;
	private int mine2VerticalPosition;
    private int barrelHorizontalPosition;
    private int barrelVerticalPosition;
	private int barrel2HorizontalPosition;
	private int barrel2VerticalPosition;

	private Texture[] background;
	private double backgroundVariation = 0;

	private double gravitySpeed = 0;
	private float verticalInitialPosition;

	private float deltaTime;
	private int gameState;
	private boolean screenTouched;

	@Override
	public void create () {
        batch = new SpriteBatch();

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

		actualWidth = Gdx.graphics.getWidth();
		actualHeight  = Gdx.graphics.getHeight();
		verticalInitialPosition = actualHeight / 2;
		mineHorizontalPosition = actualWidth + 100;
		mineVerticalPosition = actualHeight / 2;
		mine2HorizontalPosition = actualWidth + 300;
		mine2VerticalPosition = actualHeight / 4;
        barrelHorizontalPosition = actualWidth + 500;
        barrelVerticalPosition = actualHeight / 3;
		barrel2HorizontalPosition = actualWidth + 600;
		barrel2VerticalPosition = actualHeight / 5;

		mine = new Texture("mine.png");
		mine2 = new Texture("mine.png");
		barrel = new Texture("barrel.png");
		barrel2 = new Texture("barrel.png");
	}

	@Override
	public void render () {

	    screenTouched = Gdx.input.justTouched();
        deltaTime = Gdx.graphics.getDeltaTime();
        variation = variation + deltaTime * 5;

        if(variation > 5) variation = 0;

	    if(gameState == 0) {
            if(screenTouched) {
                gameState = 1;
            }
        }
        else {
            mineHorizontalPosition -= deltaTime * 400;
            mine2HorizontalPosition -= deltaTime * 500;
            barrelHorizontalPosition -= deltaTime * 300;
			barrel2HorizontalPosition -= deltaTime * 200;
            backgroundVariation = backgroundVariation + deltaTime * 5;
            gravitySpeed = gravitySpeed + 0.08;

            if(backgroundVariation > 4) backgroundVariation = 0;

            if(screenTouched) {
                gravitySpeed = -4;
            }

            if((verticalInitialPosition > 0 || gravitySpeed < 0) && (verticalInitialPosition < 780 || gravitySpeed > 0)) {
                verticalInitialPosition = verticalInitialPosition - (float)gravitySpeed;
            }

            if(mineHorizontalPosition < -mine.getWidth()) {
				mineHorizontalPosition = actualWidth;
				mineVerticalPosition = rand.nextInt(800);
			}

			if(mine2HorizontalPosition < -mine2.getWidth()) {
				mine2HorizontalPosition = actualWidth;
				mine2VerticalPosition = rand.nextInt(800);
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

	    batch.begin();
		batch.draw(background[(int)backgroundVariation], 0, 0, actualWidth, actualHeight);
        batch.draw(mine, mineHorizontalPosition, mineVerticalPosition);
		batch.draw(mine2, mine2HorizontalPosition, mine2VerticalPosition);
        batch.draw(barrel, barrelHorizontalPosition, barrelVerticalPosition);
		batch.draw(barrel2, barrel2HorizontalPosition, barrel2VerticalPosition);
		batch.draw(boy[(int)variation], 300, verticalInitialPosition);
        batch.end();
	}
}
