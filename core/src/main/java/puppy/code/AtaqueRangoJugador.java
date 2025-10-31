package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class AtaqueRangoJugador implements Ataque {

    private Rectangle areaDisparo;
    private Texture texturaAtaque;
    private boolean finalizado;
    private float velocidad;

    
    public AtaqueRangoJugador(Rectangle posicionJugador ) {
    	this.areaDisparo = new Rectangle(posicionJugador.x , posicionJugador.y , 32, 32);
        this.texturaAtaque = new Texture(Gdx.files.internal("bala.png")); 
        this.finalizado = false;
        this.velocidad = 600; 
    }
    

    @Override
    public void actualizar(float delta, Rectangle areaEntidad) {
        areaDisparo.y += velocidad * delta;
        
        if (areaDisparo.y > 450) { // sale de pantalla
            finalizado = true;
        }
    }

    @Override
    public void dibujar(SpriteBatch batch) {
    	if(!finalizado) {
        batch.draw(texturaAtaque, areaDisparo.x, areaDisparo.y, areaDisparo.width, areaDisparo.height);
        }
    }

    @Override
    public void verificarColision(Entidad enemigo) {
    	if(finalizado) {
    		return;
    	}    	
    	if (enemigo == null || enemigo.getAreaEntidad() == null) return;    	
        if (areaDisparo.overlaps(enemigo.getAreaEntidad())) {
            enemigo.dañar();
            finalizado = true;
        }
    }

    @Override
    public boolean haFinalizado() {
        return finalizado;
    }

    @Override
    public void destruir() {
        if (texturaAtaque != null) {  
            texturaAtaque.dispose();
            texturaAtaque = null;
        }
    }

    public Rectangle getAreaDisparo() {
        return areaDisparo;
    }

    public void setVelocidad(float velocidad) {
        this.velocidad = velocidad;
    }

    public float getVelocidad() {
        return velocidad;
    }
}