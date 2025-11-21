package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

public class AtaqueRayos implements Ataque {
	
    private Texture texturaRayo;
    private Array<Rectangle> rayos;
    private float tiempoTranscurrido;
    private float tiempoEntreRayos = 0.4f;
    private float tiempoUltimoRayo = 0f;
    private int cantidadRayos = 10;
    private int rayosLanzados = 0;
    private boolean terminado = false;
    
    private boolean desdeIzquierda;

    public AtaqueRayos(boolean desdeIzquierda) {
        this.desdeIzquierda = desdeIzquierda;
        texturaRayo = new Texture(Gdx.files.internal("rayo.png"));
        rayos = new Array<>();
    }

    @Override
    public void actualizar(float delta, Rectangle areaJefe) {
        if (terminado) return;

        tiempoTranscurrido += delta;

    
        if (rayosLanzados < cantidadRayos && tiempoTranscurrido - tiempoUltimoRayo >= tiempoEntreRayos) {
            float x;
            if (desdeIzquierda) {
                x = (rayosLanzados / (float)cantidadRayos) * 800;
            } else {
                x = 800 - ((rayosLanzados / (float)cantidadRayos) * 800);
            }

            Rectangle nuevoRayo = new Rectangle(x - 16, 400, 32, 100);
            rayos.add(nuevoRayo);
            rayosLanzados++;
            tiempoUltimoRayo = tiempoTranscurrido;
        }


        for (int i = 0; i < rayos.size; i++) {
            Rectangle r = rayos.get(i);
            r.y -= 600 * delta;
            if (r.y + r.height < 0) {
                rayos.removeIndex(i);
                i--;
            }
        }


        if (rayosLanzados >= cantidadRayos && rayos.size == 0) {
            terminado = true;
        }
    }

    @Override
    public void verificarColision(Entidad entidad) {
        for (int i = 0; i < rayos.size; i++) {
            if (rayos.get(i).overlaps(entidad.getAreaEntidad())) {
                entidad.dañar();

                rayos.removeIndex(i);
                i--;
            }
        }
    }

    @Override
    public void dibujar(SpriteBatch batch) {
        for (Rectangle r : rayos) {
            batch.draw(texturaRayo, r.x, r.y, r.width, r.height);
        }
    }

    @Override
    public boolean haFinalizado() {
        return terminado;
    }

    @Override
    public void destruir() {
        texturaRayo.dispose();
    }

    @Override
    public Float getPosicionObjetivoX(Rectangle areaJefe, Entidad entidad) {
        return null;
    }

}