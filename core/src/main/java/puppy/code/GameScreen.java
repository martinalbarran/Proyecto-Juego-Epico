package puppy.code;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class GameScreen implements Screen {

    private static GameScreen instance;  
	final GameLluviaMenu game;
    private OrthographicCamera camera;
	private SpriteBatch batch;	   
	private BitmapFont font;	
	private Jugador jugadorMelee;
	private Jugador jugadorRango;
	private Jefe jefe;
	public static Entidad tarroGlobal;
	private Music gameMusic;
	List<Jugador> jugadores;

    private GameScreen(final GameLluviaMenu game) {
        this.game = game;
        this.batch = game.getBatch();
        this.font = game.getFont();

        Texture texMelee = new Texture(Gdx.files.internal("jugadorMelee.png"));
        Texture texRango = new Texture(Gdx.files.internal("jugadorRango.png"));
        Texture texMeleeHurt = new Texture(Gdx.files.internal("jugadorMelee.png"));
        Texture texRangoHurt = new Texture(Gdx.files.internal("jugadorRango.png"));
        Sound hurtSound = Gdx.audio.newSound(Gdx.files.internal("hurt.ogg"));


        jugadorMelee = new Jugador(texMelee, texMeleeHurt, hurtSound, 3, 430, 280, 50, 64, 64, new ControlWASD(), new AtaqueMeleeStrategy());
        jugadorRango = new Jugador(texRango, texRangoHurt, hurtSound, 3, 430, 380, 50, 64, 64, new ControlFlechas(), new AtaqueRangoStrategy());
        jugadores = new ArrayList<>();
        jugadores.add(jugadorMelee);
        jugadores.add(jugadorRango);
        
        jefe = new Jefe(jugadores);         

        gameMusic = Gdx.audio.newMusic(Gdx.files.internal("escenario.mp3"));
        gameMusic.setVolume(0.3f);
        gameMusic.setLooping(true);
        gameMusic.play();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
    }

	public static GameScreen getInstance(GameLluviaMenu game) {
		if (instance == null) {
			instance = new GameScreen(game);
		}
		return instance;
	}
	
	public static void resetInstance() {
	    instance = null;
	}
	 
	 @Override
	    public void render(float delta) {
	        ScreenUtils.clear(0, 0, 0.2f, 1);
	        camera.update();
	        batch.setProjectionMatrix(camera.combined);

	        batch.begin();
	        font.draw(batch, "Vidas Melee: " + jugadorMelee.getVida(), 10, 470);
	        font.draw(batch, "Vidas Rango: " + jugadorRango.getVida(), 10, 450);
	        font.draw(batch, "Vida JEFE: " + jefe.getVida(), 10, 430);
	        

	        jugadorMelee.actualizarEntidad(delta, batch, jugadores);
	        jugadorRango.actualizarEntidad(delta, batch, jugadores);
	        jefe.actualizarEntidad(delta, batch, jugadores);
	        batch.end();
	        
	        if (Gdx.input.isKeyJustPressed(Input.Keys.F)) {
	            jugadorMelee.ejecutarAtaque();
	        }

	        if (Gdx.input.isKeyJustPressed(Input.Keys.SHIFT_RIGHT)) {
	            jugadorRango.ejecutarAtaque();
	        }
	        
	        jugadorMelee.verificarColisiones(jefe);
	        jugadorRango.verificarColisiones(jefe);

	        if ((jugadorMelee.getVida() <= 0 || jugadorRango.getVida() <= 0) || (jefe.getVida() <= 0)) {
	            game.setScreen(new GameOverScreen(game));
	            gameMusic.stop();
	            dispose();
	        }
	    }



	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void show() {
	}

	@Override
	public void hide() {

	}

	@Override
	public void pause() {
		game.setScreen(new PausaScreen(game, this)); 
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
      jugadorMelee.destruir();
	}
}
