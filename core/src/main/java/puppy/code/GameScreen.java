package puppy.code;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class GameScreen implements Screen {
	final GameLluviaMenu game;
    private OrthographicCamera camera;
	private SpriteBatch batch;	   
	private BitmapFont font;	
	private Jugador jugadorMelee;
	private Jugador jugadorRango;
	private Lluvia lluvia;
	private Jefe jefe;
	public static Jugador tarroGlobal;
	   


	public GameScreen(final GameLluviaMenu game) {
		this.game = game;
        this.batch = game.getBatch();
        this.font = game.getFont();
		  Sound hurtSound = Gdx.audio.newSound(Gdx.files.internal("hurt.ogg"));
		  jugadorMelee = new JugadorMelee(new Texture(Gdx.files.internal("bucket.png")),hurtSound);
		  jugadorRango = new JugadorRango(new Texture(Gdx.files.internal("bucket.png")),hurtSound);
		  
		  jefe = new Jefe();
         

         Texture gota = new Texture(Gdx.files.internal("drop.png"));
         Texture gotaMala = new Texture(Gdx.files.internal("dropBad.png"));
         
         Sound dropSound = Gdx.audio.newSound(Gdx.files.internal("drop.wav"));
        
	     Music rainMusic = Gdx.audio.newMusic(Gdx.files.internal("ecenario.mp3"));
	     rainMusic.setVolume(0.3f);
         lluvia = new Lluvia(gota, gotaMala, dropSound, rainMusic);
         
         tarroGlobal = jugadorMelee;
	      
	      // camera
	      camera = new OrthographicCamera();
	      camera.setToOrtho(false, 800, 480);
	      batch = new SpriteBatch();

	      jugadorMelee.crear();
	      jugadorRango.crear();

	      lluvia.crear();
	}

	@Override
	public void render(float delta) {
	    ScreenUtils.clear(0, 0, 0.2f, 1);
	    camera.update();
	    batch.setProjectionMatrix(camera.combined);

	    batch.begin();
	    jefe.dibujar(batch);
	    jugadorMelee.dibujar(batch);
	    jugadorRango.dibujar(batch);
	    font.draw(batch, "Vidas Melee: " + jugadorMelee.getVidas(), 10, 470);
	    font.draw(batch, "Vidas Rango: " + jugadorRango.getVidas(), 10, 450);
	    batch.end();


	    jugadorMelee.actualizarMovimiento();
	    jugadorRango.actualizarMovimiento();


	    List<Jugador> jugadores = new ArrayList<>();
	    jugadores.add(jugadorMelee);
	    jugadores.add(jugadorRango);
	    jefe.actualizar(delta, jugadores);


	    if (jugadorMelee.getVidas() <= 0 || jugadorRango.getVidas() <= 0) {
	        game.setScreen(new GameOverScreen(game));
	        dispose();
	    }
	}


	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void show() {
	  // continuar con sonido de lluvia
	  lluvia.continuar();
	}

	@Override
	public void hide() {

	}

	@Override
	public void pause() {
		lluvia.pausar();
		game.setScreen(new PausaScreen(game, this)); 
	}

	@Override
	public void resume() {

	}

	@Override
	public void dispose() {
      jugadorMelee.destruir();
      lluvia.destruir();
	}
}
