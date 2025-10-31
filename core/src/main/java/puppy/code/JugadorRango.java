package puppy.code;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

public class JugadorRango extends Entidad{
	
	
	   public JugadorRango(Texture tex, Texture texAlt, Sound ss, int vida, int velocidad, int coordenadasX, int coordenadasY, int ancho, int alto) {
		   super(tex, texAlt, ss, vida, velocidad, coordenadasX, coordenadasY, ancho, alto);
	   }

	   @Override
	   public void ataque() {
		   if(Gdx.input.isKeyPressed(Input.Keys.F)) {
			   
		   }
		
	   }

	   public void actualizar() { 
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

	   @Override
	   protected void chequearAtaque(float delta, Rectangle areaEntidad2) {
		// TODO Auto-generated method stub
		
	   }
}
