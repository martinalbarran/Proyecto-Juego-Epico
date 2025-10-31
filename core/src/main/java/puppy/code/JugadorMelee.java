package puppy.code;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

public class JugadorMelee extends Entidad{
	
	   public JugadorMelee(Texture tex, Texture texAlt, Sound ss, int vida, int velocidad, int coordenadasX, int coordenadasY, int ancho, int alto) {
		   super(tex, texAlt, ss, vida, velocidad, coordenadasX, coordenadasY, ancho, alto);
	   }

	   public void ataque() {
		    if (Gdx.input.isKeyJustPressed(Input.Keys.F)) {
		        setAtaqueActual(new AtaqueMeleeJugador(getAreaEntidad())) ;
		    }
		}
	   
	   public void chequearAtaque(float delta, Rectangle areaJefe) {
		    if (getAtaqueActual() != null) {
		        getAtaqueActual().actualizar(delta, areaJefe);
		        if (getAtaqueActual().haFinalizado()) {
		            getAtaqueActual().destruir();
		            setAtaqueActual(null);
		        }
		    }
		}
	   
	   public void actualizar() { 
		    if (Gdx.input.isKeyPressed(Input.Keys.A)) getAreaEntidad().x -= getVelocidad() * Gdx.graphics.getDeltaTime();
		    if (Gdx.input.isKeyPressed(Input.Keys.D)) getAreaEntidad().x += getVelocidad() * Gdx.graphics.getDeltaTime();
		    if (Gdx.input.isKeyPressed(Input.Keys.W)) getAreaEntidad().y += getVelocidad() * Gdx.graphics.getDeltaTime();
		    if (Gdx.input.isKeyPressed(Input.Keys.S)) getAreaEntidad().y -= getVelocidad() * Gdx.graphics.getDeltaTime();
		    
		    //esto es para delimitar los bordes
		    if (getAreaEntidad().x < 0) getAreaEntidad().x = 0;
		    if (getAreaEntidad().x > 800 - 64) getAreaEntidad().x = 800 - 64;		   
		    if (getAreaEntidad().y > 350 - 64) getAreaEntidad().y = 350 - 64;
		    if (getAreaEntidad().y < 0) getAreaEntidad().y = 0;
		}


}
