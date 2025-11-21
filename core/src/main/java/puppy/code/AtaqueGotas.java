package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;


public class AtaqueGotas implements Ataque {

    private Texture gota;
    private Array<Rectangle> gotas;
    private float tiempoDesdeUltima = 0;
    private float tiempoTranscurrido = 0;
    private final float duracion = 5f;
    private final float cooldown = 3f;
    private boolean enCooldown = false;
    private boolean terminado = false;
    public AtaqueGotas() {
        gota = new Texture(Gdx.files.internal("dropBad.png"));
        gotas = new Array<>();
    }

    @Override
    public void actualizar(float delta, Rectangle areaJefe) {
        if (terminado) return;

        tiempoTranscurrido += delta;

        if (!enCooldown) {
            if (tiempoTranscurrido < duracion) {
                tiempoDesdeUltima += delta;
                if (tiempoDesdeUltima > 0.5f) {
                    Rectangle g = new Rectangle(areaJefe.x + areaJefe.width / 2f - 16,areaJefe.y,32,32);
                    gotas.add(g);
                    tiempoDesdeUltima = 0;
                }
            } else {
                enCooldown = true;
                tiempoDesdeUltima = 0;
            }
        } else {
            if (tiempoTranscurrido > duracion + cooldown && gotas.size == 0) {
                terminado = true;
            }
        }
        
        for (int i = 0; i < gotas.size; i++) {
            Rectangle r = gotas.get(i);
            r.y -= 400 * delta;
            if (r.y < 0) {
                gotas.removeIndex(i);
                i--;
            }
        }
    }

    public Float getPosicionObjetivoX(Rectangle areaJefe, Entidad entidad) {
        Rectangle areaJugador = entidad.getAreaEntidad();
        float centroJugadorX = areaJugador.x + areaJugador.width / 2f;
        float destinoX = centroJugadorX - areaJefe.width / 2f;

        return destinoX;
    }

    @Override
    public void verificarColision(Entidad tarro) {
        for (int i = 0; i < gotas.size; i++) {
            Rectangle r = gotas.get(i);
            if (r.overlaps(tarro.getAreaEntidad())) {
                tarro.dañar();
                gotas.removeIndex(i);
                i--;
            }
        }
    }

    @Override
    public void dibujar(SpriteBatch batch) {
        for (Rectangle r : gotas) {
            batch.draw(gota, r.x, r.y);
        }
    }

    @Override
    public boolean haFinalizado() {
        return terminado;
    }

    @Override
    public void destruir() {
        if (gota != null) gota.dispose();
    }
}