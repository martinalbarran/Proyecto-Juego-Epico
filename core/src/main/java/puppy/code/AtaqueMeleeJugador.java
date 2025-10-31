package puppy.code;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class AtaqueMeleeJugador implements Ataque {

    private Rectangle areaGolpe;
    private Texture texturaAtaque;
    private boolean activo;
    private float duracion;      
    private float tiempoTranscurrido; 
    
    public AtaqueMeleeJugador(Rectangle posicionJugador) {
        this.areaGolpe = new Rectangle(posicionJugador.x, posicionJugador.y + posicionJugador.height,  100, 50); // tamaño del golpe
        this.texturaAtaque = new Texture(Gdx.files.internal("ataqueMelee.jpg"));
        this.activo = true;
        this.duracion = 0.2f;
        this.tiempoTranscurrido = 0;
    }

    @Override
    public void actualizar(float delta, Rectangle areaJefe) {
        if (!activo) return;

        tiempoTranscurrido += delta;
        if (tiempoTranscurrido > duracion) {
            activo = false;
        }
    }

    @Override
    public void dibujar(SpriteBatch batch) {
        if (activo) {
            batch.draw(texturaAtaque, areaGolpe.x, areaGolpe.y, areaGolpe.width, areaGolpe.height);
        }
    }

    @Override
    public void verificarColision(Entidad enemigo) {
        if (!activo) return;
        if (enemigo == null || enemigo.getAreaEntidad() == null) return; // evita null

        if (areaGolpe.overlaps(enemigo.getAreaEntidad())) {
            enemigo.dañar();
            activo = false;
        }
    }


    @Override
    public boolean haFinalizado() {
        return !activo;
    }

    @Override
    public void destruir() {
        texturaAtaque.dispose();
    }
}


