package io.github.matheusfsantos.drop;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.renderers.BatchTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;
import com.sun.org.apache.xerces.internal.impl.xs.util.XSInputSource;

import java.util.Iterator;

public class DropGame extends ApplicationAdapter {
	private OrthographicCamera camera;
	private SpriteBatch batch;

	/* Active */
	private Texture dropImage;
	private Texture bucketImage;
	private Sound dropSound;
	private Music rainMusic;

	/* Renderers */
	private Rectangle bucket;
	private Array<Rectangle> rainDrops;
	private long lastDropRainTime;

	/* Points */
	private int points;
	private BitmapFont font;

	@Override
	public void create () {
		this.font = new BitmapFont();
		this.batch = new SpriteBatch();
		this.camera = new OrthographicCamera();
		this.rainDrops = new Array<Rectangle>();
		this.spawnNewRainDrop();

		this.camera.setToOrtho(false, 800, 480);

		this.instanceAssets();
		this.musicController();

		this.bucket = new Rectangle();

		/* align on center */
		this.bucket.x = (800 /2) - (64 /2);
		this.bucket.y = 20;
		this.bucket.width = 64;
		this.bucket.height = 64;
	}

	private void instanceAssets() {
		this.dropImage = new Texture(Gdx.files.internal("drop.png"));
		this.bucketImage = new Texture(Gdx.files.internal("bucket.png"));

		this.dropSound = Gdx.audio.newSound(Gdx.files.internal("waterdrop.wav"));
		this.rainMusic = Gdx.audio.newMusic(Gdx.files.internal("rain.mp3"));
	}

	private void musicController() {
		this.rainMusic.setLooping(true);
		this.rainMusic.setVolume(0.2f);
		this.rainMusic.play();
	}

	private void spawnNewRainDrop() {
		Rectangle raindrop = new Rectangle();

		raindrop.x = MathUtils.random(0, (800 - 64));
		raindrop.y = 480;

		raindrop.width = 64;
		raindrop.height = 64;

		this.rainDrops.add(raindrop);
		this.lastDropRainTime = TimeUtils.nanoTime();
	}

	@Override
	public void render () {
		ScreenUtils.clear(0, 0, 0.2f, 1);
		this.camera.update();

		this.batch.setProjectionMatrix(this.camera.combined);
		this.batch.begin();

		this.batch.draw(this.bucketImage, this.bucket.x, this.bucket.y);
		this.font.draw(batch, ("Total de pontos: " + this.points), 20, 460);
		for(Rectangle raindrop : this.rainDrops) {
			batch.draw(dropImage, raindrop.x, raindrop.y);
		}

		this.batch.end();

		if(Gdx.input.isTouched()) {
			Vector3 touchPosition = new Vector3();

			touchPosition.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			this.camera.unproject(touchPosition);
			this.bucket.x = touchPosition.x - (64 /2);
		}

		if(Gdx.input.isKeyPressed(Input.Keys.LEFT))
			bucket.x -= 500 * Gdx.graphics.getDeltaTime();

		if(Gdx.input.isKeyPressed(Input.Keys.RIGHT))
			bucket.x += 500 * Gdx.graphics.getDeltaTime();

		if(this.bucket.x < 0)
			this.bucket.x = 0;

		if(this.bucket.x > 800 - 64)
			this.bucket.x = 800 - 64;

		/* -------------------- RainDrops -------------------- */
		if((TimeUtils.nanoTime() - this.lastDropRainTime) > 1000000000)
			this.spawnNewRainDrop();

		for(Iterator<Rectangle> iter = this.rainDrops.iterator(); iter.hasNext(); ) {
			Rectangle raindrop = iter.next();

			raindrop.y -= 200 * Gdx.graphics.getDeltaTime();
			if(raindrop.y + 64 < 0) iter.remove();

			if(raindrop.overlaps(bucket)) {
				this.dropSound.play();
				this.points += 1;
				iter.remove();
			}
		}


	}
	
	@Override
	public void dispose () {
		this.batch.dispose();
		this.dropSound.dispose();
		this.rainMusic.dispose();
		this.rainMusic.dispose();
		this.bucketImage.dispose();
	}

}
