package puppy.code;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;

public class JugadorMelee extends Jugador{
	
	   public JugadorMelee(Texture tex, Sound ss) {
		   setBucketImage(tex);
		   setSonidoHerido(ss);
	   }

	   @Override
	   public void ataque() {
		// TODO Auto-generated method stub
		
	   }

	      public void actualizarMovimiento() { 
	   	//movimiento desde teclado
	   	if(Gdx.input.isKeyPressed(Input.Keys.A)) getBucket().x -= getVelX() * Gdx.graphics.getDeltaTime();
	   	if(Gdx.input.isKeyPressed(Input.Keys.D)) getBucket().x += getVelX() * Gdx.graphics.getDeltaTime();
	   	if(Gdx.input.isKeyPressed(Input.Keys.W)) getBucket().y += getVelX() * Gdx.graphics.getDeltaTime();
	   	if(Gdx.input.isKeyPressed(Input.Keys.S)) getBucket().y -= getVelX() * Gdx.graphics.getDeltaTime();
	   	// que no se salga de los bordes izq y der
	   	if(getBucket().x < 0) getBucket().x = 0;
	   	if(getBucket().x > 800 - 64) getBucket().x = 800 - 64;		   
	   	if(getBucket().y > 350 - 64) getBucket().y = 350 - 64;
	   	if(getBucket().y < 0) getBucket().y = 0;
	   	
   	   }

}
