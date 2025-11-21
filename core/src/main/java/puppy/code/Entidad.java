package puppy.code;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

public abstract class Entidad {

    private Rectangle areaEntidad;
    private Texture textura;
    private Texture texturaHurt;
    private Sound sonidoHerido;
    private int vida;
    private int velocidad;
    private boolean herido = false;
    private int tiempoHeridoMax = 50;
    private int tiempoHerido; 
    private Ataque ataqueActual;
    private AtaqueStrategy ataqueStrategy;

    public Entidad(Texture image, Texture imageAlt, Sound ss, int vida, int velocidad, int coordenadasX, int coordenadasY, int ancho, int alto) {
        this.textura = image;
        this.texturaHurt = imageAlt;
        this.sonidoHerido = ss;
        this.vida = vida;
        this.velocidad = velocidad;
        crearHitbox(coordenadasX, coordenadasY, ancho, alto);
    }

    public final void actualizarEntidad(float delta, SpriteBatch batch, java.util.List<Jugador> jugadores) {
    	mover(delta);
    	
    	dibujar(batch);
    	
    	procesarAtaques(delta, jugadores);
    }
    
    public abstract void mover(float delta);

	public void aplicarControl(float delta) {
    }

    public void limitarPantalla() {
    }

    public abstract void procesarAtaques(float delta, java.util.List<Jugador> jugadores);


    public void crearHitbox(int coordenadasX, int coordenadasY, int ancho, int alto) {
        areaEntidad = new Rectangle(coordenadasX, coordenadasY, ancho, alto);
    }

    public void dañar() {
        vida--;
        herido = true;
        tiempoHerido = tiempoHeridoMax;
        sonidoHerido.play();
    }

    public void dibujar(SpriteBatch batch) {
        if (herido) {
            batch.draw(texturaHurt, areaEntidad.x, areaEntidad.y + MathUtils.random(-5, 5));
            tiempoHerido--;
            if (tiempoHerido <= 0) herido = false;
        } else {
            batch.draw(textura, areaEntidad.x, areaEntidad.y);
        }

        if (ataqueActual != null) {
            ataqueActual.dibujar(batch);
        }
    }

    public void destruir() {
        textura.dispose();
    }


    public AtaqueStrategy getAtaqueStrategy() { return ataqueStrategy; }
    public void setAtaqueStrategy(AtaqueStrategy ataqueStrategy) { this.ataqueStrategy = ataqueStrategy; }

    public Ataque getAtaqueActual() { return ataqueActual; }
    public void setAtaqueActual(Ataque ataqueActual) { this.ataqueActual = ataqueActual; }

    public Rectangle getAreaEntidad() { return areaEntidad; }
    public void setAreaEntidad(Rectangle areaEntidad) { this.areaEntidad = areaEntidad; }

    public Texture getTextura() { return textura; }
    public Texture getTexturaHurt() { return texturaHurt; }

    public int getVida() { return vida; }

    public int getVelocidad() { return velocidad; }

    public boolean estaHerido() { return herido; }
	public int getTiempoHerido() { return tiempoHerido; }
	public int setTiempoHerido(int tiempo) { return tiempoHerido = tiempo; }
	public void setHerido(boolean herido) { this.herido = herido; }
}

