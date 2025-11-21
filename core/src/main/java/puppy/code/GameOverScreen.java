package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class GameOverScreen implements Screen {
    private final GameLluviaMenu game;
    private SpriteBatch batch;
    private BitmapFont font;
    private OrthographicCamera camera;
    private Music musicaGameOver; 

    public GameOverScreen(final GameLluviaMenu game) {
        this.game = game;
        this.batch = game.getBatch();
        this.font = game.getFont();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);

        musicaGameOver = Gdx.audio.newMusic(Gdx.files.internal("gameOver.mp3"));
        musicaGameOver.setLooping(true);
        musicaGameOver.setVolume(0.7f);   
        musicaGameOver.play();      
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0.2f, 1);
        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        font.getData().setScale(1.3f);
        font.draw(batch, "GAME OVER", 320, 250);
        font.getData().setScale(1f);
        font.draw(batch, "Toca en cualquier lado para reiniciar.", 220, 150);
        batch.end();

        if (Gdx.input.isTouched()) {
            musicaGameOver.stop();
            GameScreen.resetInstance();  
            game.setScreen(GameScreen.getInstance(game));
            dispose();
        }

    }

    @Override
    public void dispose() {
        if (musicaGameOver != null) musicaGameOver.dispose();
    }

    @Override
    public void show() {}
    @Override
    public void resize(int width, int height) {}
    @Override
    public void pause() {}
    @Override
    public void resume() {}
    @Override
    public void hide() {}
}
