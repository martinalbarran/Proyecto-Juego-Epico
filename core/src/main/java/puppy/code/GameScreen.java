package puppy.code;

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
	   
	//boolean activo = true;

	public GameScreen(final GameLluviaMenu game) {
		this.game = game;
        this.batch = game.getBatch();
        this.font = game.getFont();
		  // load the images for the droplet and the bucket, 64x64 pixels each 	     
          //TODO: Cambiar las texturas para cada jugador
		  Sound hurtSound = Gdx.audio.newSound(Gdx.files.internal("hurt.ogg"));
		  jugadorMelee = new JugadorMelee(new Texture(Gdx.files.internal("bucket.png")),hurtSound);
		  jugadorRango = new JugadorRango(new Texture(Gdx.files.internal("bucket.png")),hurtSound);
		  
		  jefe = new Jefe();
         
	      // load the drop sound effect and the rain background "music" 
         Texture gota = new Texture(Gdx.files.internal("drop.png"));
         Texture gotaMala = new Texture(Gdx.files.internal("dropBad.png"));
         
         Sound dropSound = Gdx.audio.newSound(Gdx.files.internal("drop.wav"));
        
	     Music rainMusic = Gdx.audio.newMusic(Gdx.files.internal("ecenario.mp3"));
         lluvia = new Lluvia(gota, gotaMala, dropSound, rainMusic);
         
         tarroGlobal = jugadorMelee;
	      
	      // camera
	      camera = new OrthographicCamera();
	      camera.setToOrtho(false, 800, 480);
	      batch = new SpriteBatch();
	      // creacion del tarro
	      jugadorMelee.crear();
	      jugadorRango.crear();
	      // creacion de la lluvia
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
	    font.draw(batch, "Vidas: " + jugadorMelee.getVidas(), 10, 470);
	    font.draw(batch, "Vidas: " + jugadorRango.getVidas(), 10, 450);
	    batch.end();

	    jefe.actualizar(delta, jugadorMelee);
	    jefe.actualizar(delta, jugadorRango);
	    jugadorMelee.actualizarMovimiento();
	    jugadorRango.actualizarMovimiento();
	    
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
