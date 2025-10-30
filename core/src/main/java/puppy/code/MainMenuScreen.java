package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;

public class MainMenuScreen implements Screen {

    final GameLluviaMenu game;
    private SpriteBatch batch;
    private BitmapFont font;
    private OrthographicCamera camera;
    private ShapeRenderer shapeRenderer;
    
    // Definición del botón
    private Rectangle startButton;
    private boolean isButtonHovered;

    public MainMenuScreen(final GameLluviaMenu game) {
        this.game = game;
        this.batch = game.getBatch();
        this.font = game.getFont();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
        
        shapeRenderer = new ShapeRenderer();
        
        // Crear el rectángulo del botón (x, y, ancho, alto)
        startButton = new Rectangle(300, 180, 200, 60);
        isButtonHovered = false;
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0.2f, 1);
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        shapeRenderer.setProjectionMatrix(camera.combined);

        // Verificar si el mouse está sobre el botón
        Vector3 mousePos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        camera.unproject(mousePos);
        isButtonHovered = startButton.contains(mousePos.x, mousePos.y);

        // Dibujar el botón
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        if (isButtonHovered) {
            shapeRenderer.setColor(0.3f, 0.6f, 0.9f, 1); // Azul claro cuando hover
        } else {
            shapeRenderer.setColor(0.2f, 0.4f, 0.7f, 1); // Azul oscuro normal
        }
        shapeRenderer.rect(startButton.x, startButton.y, startButton.width, startButton.height);
        shapeRenderer.end();

        // Dibujar borde del botón
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.WHITE);
        shapeRenderer.rect(startButton.x, startButton.y, startButton.width, startButton.height);
        shapeRenderer.end();

        // Dibujar textos
        batch.begin();
        font.getData().setScale(2, 2);
        font.draw(batch, "Bienvenido a Recolecta Gotas!!!", 80, 350);
        
        font.getData().setScale(1.5f, 1.5f);
        font.draw(batch, "INICIAR JUEGO", startButton.x + 20, startButton.y + 38);
        
        font.getData().setScale(1, 1);
        batch.end();

        // Detectar click en el botón
        if (Gdx.input.justTouched() && isButtonHovered) {
            game.setScreen(new GameScreen(game));
            dispose();
        }
    }

    @Override
    public void show() {
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        shapeRenderer.dispose();
    }
}
