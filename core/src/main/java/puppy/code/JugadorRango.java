package puppy.code;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;

public class JugadorRango extends Entidad{
	
	
	   public JugadorRango(Texture tex, Texture texAlt, Sound ss, int vida, int velocidad) {
		   super(tex, texAlt, ss, vida, velocidad);
	   }

	   @Override
	   public void ataque() {
		   if(Gdx.input.isKeyPressed(Input.Keys.F)) {
			   
		   }
		
	   }

	   public void actualizarMovimiento() { 
		    // movimiento desde teclado
		    if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) getAreaEntidad().x -= getVelocidad() * Gdx.graphics.getDeltaTime();
		    if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) getAreaEntidad().x += getVelocidad() * Gdx.graphics.getDeltaTime();
		    if (Gdx.input.isKeyPressed(Input.Keys.UP)) getAreaEntidad().y += getVelocidad() * Gdx.graphics.getDeltaTime();
		    if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) getAreaEntidad().y -= getVelocidad() * Gdx.graphics.getDeltaTime();
		    
		    // que no se salga de los bordes izq y der
		    if (getAreaEntidad().x < 0) getAreaEntidad().x = 0;
		    if (getAreaEntidad().x > 800 - 64) getAreaEntidad().x = 800 - 64;		   
		    if (getAreaEntidad().y > 350 - 64) getAreaEntidad().y = 350 - 64;
		    if (getAreaEntidad().y < 0) getAreaEntidad().y = 0;
		}
}
