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
	private Entidad jugadorMelee;
	private Entidad jugadorRango;
	private Jefe jefe;
	public static Entidad tarroGlobal;
	private Texture player1Tex;
	private Texture player2Tex;
	private Texture player1TexHurt;
	private Texture player2TexHurt;

	public GameScreen(final GameLluviaMenu game) {
		this.game = game;
        this.batch = game.getBatch();
        this.font = game.getFont();
        player1Tex = new Texture(Gdx.files.internal("bucket.png"));
        player2Tex = new Texture(Gdx.files.internal("bucket.png"));
        player1TexHurt = new Texture(Gdx.files.internal("bucket.png"));
        player2TexHurt = new Texture(Gdx.files.internal("bucket.png"));
		Sound hurtSound = Gdx.audio.newSound(Gdx.files.internal("hurt.ogg"));
		
		jugadorMelee = new JugadorMelee(player1Tex, player1TexHurt,hurtSound, 10, 400);
		jugadorRango = new JugadorRango(player2Tex, player2TexHurt,hurtSound, 10, 400);
		  
		jefe = new Jefe();         
        
	    Music rainMusic = Gdx.audio.newMusic(Gdx.files.internal("escenario.mp3"));
	    rainMusic.setVolume(0.3f);
        rainMusic.play();
        rainMusic.setLooping(true);
        tarroGlobal = jugadorMelee;
	      
	      // camera
	    camera = new OrthographicCamera();
	    camera.setToOrtho(false, 800, 480);
	    batch = new SpriteBatch();

	    jugadorMelee.crear();
	    jugadorRango.crear();

	}

	@Override
	public void render(float delta) {
	    ScreenUtils.clear(0, 0, 0.2f, 1);
	    camera.update();
	    batch.setProjectionMatrix(camera.combined);

	    batch.begin();
	    jefe.dibujarJefe(batch);
	    jugadorMelee.dibujar(batch);
	    jugadorRango.dibujar(batch);
	    font.draw(batch, "Vidas Melee: " + jugadorMelee.getVida(), 10, 470);
	    font.draw(batch, "Vidas Rango: " + jugadorRango.getVida(), 10, 450);
	    batch.end();


	    jugadorMelee.actualizarMovimiento();
	    jugadorRango.actualizarMovimiento();


	    List<Entidad> jugadores = new ArrayList<>();
	    jugadores.add(jugadorMelee);
	    jugadores.add(jugadorRango);
	    jefe.actualizar(delta, jugadores);


	    if (jugadorMelee.getVida() <= 0 || jugadorRango.getVida() <= 0) {
	        game.setScreen(new GameOverScreen(game));
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
