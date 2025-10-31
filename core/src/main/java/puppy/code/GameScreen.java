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
	private Music gameMusic;
	
	public GameScreen(final GameLluviaMenu game) {
		this.game = game;
        this.batch = game.getBatch();
        this.font = game.getFont();
        player1Tex = new Texture(Gdx.files.internal("bucket.png"));
        player2Tex = new Texture(Gdx.files.internal("bucket.png"));
        player1TexHurt = new Texture(Gdx.files.internal("bucket.png"));
        player2TexHurt = new Texture(Gdx.files.internal("bucket.png"));
		Sound hurtSound = Gdx.audio.newSound(Gdx.files.internal("hurt.ogg"));
		
		jugadorMelee = new JugadorMelee(player1Tex, player1TexHurt,hurtSound, 10, 400, 300, 20, 64, 64);
		jugadorRango = new JugadorRango(player2Tex, player2TexHurt,hurtSound, 10, 400, 350, 20, 64, 64);
		jefe = new Jefe();         

        gameMusic = Gdx.audio.newMusic(Gdx.files.internal("escenario.mp3"));
	    gameMusic.setVolume(0.3f);
        gameMusic.play();
        gameMusic.setLooping(true);
        tarroGlobal = jugadorMelee;
	      
	      // camera
	    camera = new OrthographicCamera();
	    camera.setToOrtho(false, 800, 480);
	    batch = new SpriteBatch();
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
	    font.draw(batch, "Vida JEFE: " + jefe.getVida(), 10, 430);
	    batch.end();


	    jugadorMelee.actualizar();
	    jugadorRango.actualizar();
	    jugadorMelee.ataque();
	    jugadorMelee.chequearAtaque(delta, jefe.getAreaEntidad());
	    
	    List<Entidad> jugadores = new ArrayList<>();
	    jugadores.add(jugadorMelee);
	    jugadores.add(jugadorRango);
	    jefe.actualizar(delta, jugadores);

	    if (jugadorMelee.getAtaqueActual() != null) {
	        jugadorMelee.getAtaqueActual().verificarColision(jefe);
	        jugadorMelee.getAtaqueActual().actualizar(delta, jefe.getAreaEntidad());
	    }
	    
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
