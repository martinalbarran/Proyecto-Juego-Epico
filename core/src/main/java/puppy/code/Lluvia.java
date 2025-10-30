package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

public class Lluvia {
    private Array<Rectangle> rainDropsPos;
    private Array<Integer> rainDropsType;
    private long lastDropTime;
    private Texture gotaBuena;
    private Texture gotaMala;
    private Sound dropSound;
    private Music rainMusic;
    
    // Zona de daño en la parte superior
    private Rectangle zonaDano;
    private ShapeRenderer shapeRenderer;
    private float tiempoEnZonaDano;
    private static final float TIEMPO_DANO = 1.0f; // 1 segundo para recibir daño
    private static final float ALTURA_ZONA_DANO = 150f; // Altura de la zona peligrosa
    
    public Lluvia(Texture gotaBuena, Texture gotaMala, Sound ss, Music mm) {
        rainMusic = mm;
        dropSound = ss;
        this.gotaBuena = gotaBuena;
        this.gotaMala = gotaMala;
        shapeRenderer = new ShapeRenderer();
        tiempoEnZonaDano = 0;
    }
    
    public void crear() {
        rainDropsPos = new Array<Rectangle>();
        rainDropsType = new Array<Integer>();
        crearGotaDeLluvia();
        
        // Crear zona de daño en la parte superior de la pantalla
        zonaDano = new Rectangle(0, 480 - ALTURA_ZONA_DANO, 800, ALTURA_ZONA_DANO);
        
        // start the playback of the background music immediately
        rainMusic.setLooping(true);
        rainMusic.play();
    }
    
    private void crearGotaDeLluvia() {
        Rectangle raindrop = new Rectangle();
        raindrop.x = MathUtils.random(0, 800-64);
        raindrop.y = 480;
        raindrop.width = 64;
        raindrop.height = 64;
        rainDropsPos.add(raindrop);
        // ver el tipo de gota
        if (MathUtils.random(1,10)<5)          
            rainDropsType.add(1);
        else 
            rainDropsType.add(2);
        lastDropTime = TimeUtils.nanoTime();
    }
    
    public boolean actualizarMovimiento(Jugador tarro) { 
        // generar gotas de lluvia 
        if(TimeUtils.nanoTime() - lastDropTime > 100000000) crearGotaDeLluvia();
        
        // Verificar si el tarro está en la zona de daño
        if (zonaDano.overlaps(tarro.getArea())) {
            tiempoEnZonaDano += Gdx.graphics.getDeltaTime();
            
            // Si pasa suficiente tiempo en la zona, causar daño
            if (tiempoEnZonaDano >= TIEMPO_DANO) {
                tarro.dañar();
                tiempoEnZonaDano = 0; // Resetear el contador
                
                if (tarro.getVidas() <= 0)
                    return false; // Game over
            }
        } else {
            // Resetear el contador si sale de la zona
            tiempoEnZonaDano = 0;
        }
        
        // revisar si las gotas cayeron al suelo o chocaron con el tarro
        for (int i=0; i < rainDropsPos.size; i++ ) {
            Rectangle raindrop = rainDropsPos.get(i);
            raindrop.y -= 300 * Gdx.graphics.getDeltaTime();
            
            //cae al suelo y se elimina
            if(raindrop.y + 64 < 0) {
                rainDropsPos.removeIndex(i); 
                rainDropsType.removeIndex(i);
            }
            
            if(raindrop.overlaps(tarro.getArea())) { //la gota choca con el tarro
                if(rainDropsType.get(i)==1) { // gota dañina
                    tarro.dañar();
                    if (tarro.getVidas()<=0)
                        return false; // si se queda sin vidas retorna falso /game over
                    rainDropsPos.removeIndex(i);
                    rainDropsType.removeIndex(i);
                } else { // gota a recolectar
                    tarro.sumarPuntos(10);
                    dropSound.play();
                    rainDropsPos.removeIndex(i);
                    rainDropsType.removeIndex(i);
                }
            }
        } 
        return true; 
    }
    
    public void dibujarZonaDano(ShapeRenderer shapeRenderer) {
        // Relleno semi-transparente con efecto pulsante
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        float alpha = 0.2f + (MathUtils.sin(TimeUtils.millis() / 200f) * 0.1f);
        shapeRenderer.setColor(1, 0, 0, alpha);
        shapeRenderer.rect(zonaDano.x, zonaDano.y, zonaDano.width, zonaDano.height);
        shapeRenderer.end();
        
        // Borde de la zona
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        Gdx.gl.glLineWidth(3);
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.rect(zonaDano.x, zonaDano.y, zonaDano.width, zonaDano.height);
        shapeRenderer.end();
        Gdx.gl.glLineWidth(1);
    }
    
    public void actualizarDibujoLluvia(SpriteBatch batch) { 
        for (int i=0; i < rainDropsPos.size; i++ ) {
            Rectangle raindrop = rainDropsPos.get(i);
            if(rainDropsType.get(i)==1) // gota dañina
                batch.draw(gotaMala, raindrop.x, raindrop.y); 
            else
                batch.draw(gotaBuena, raindrop.x, raindrop.y); 
        }
    }
    
    public float getTiempoEnZonaDano() {
        return tiempoEnZonaDano;
    }
    
    public float getTiempoDanoMaximo() {
        return TIEMPO_DANO;
    }
    
    public void destruir() {
        dropSound.dispose();
        rainMusic.dispose();
        shapeRenderer.dispose();
    }
    
    public void pausar() {
        rainMusic.stop();
    }
    
    public void continuar() {
        rainMusic.play();
    }
}