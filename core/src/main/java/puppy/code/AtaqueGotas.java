package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

public class AtaqueGotas implements AtaqueJefe {
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
            // fase activa del ataque
            if (tiempoTranscurrido < duracion) {
                tiempoDesdeUltima += delta;
                if (tiempoDesdeUltima > 0.5f) {
                    Rectangle g = new Rectangle(
                        areaJefe.x + areaJefe.width / 2 - 16,
                        areaJefe.y,
                        32,
                        32
                    );
                    gotas.add(g);
                    tiempoDesdeUltima = 0;
                }
            } else {
                // Entra en enfriamiento, pero NO borramos las gotas
                enCooldown = true;
                tiempoDesdeUltima = 0;
            }
        } else {
            // Esperamos a que pase el cooldown
            if (tiempoTranscurrido > duracion + cooldown) {
                // Finaliza el ataque cuando ya pasó todo el tiempo
                // y no quedan gotas en pantalla
                if (gotas.size == 0) {
                    terminado = true;
                }
            }
        }

        // Actualizar movimiento de gotas
        for (int i = 0; i < gotas.size; i++) {
            Rectangle r = gotas.get(i);
            r.y -= 200 * delta;
            if (r.y < 0) {
                gotas.removeIndex(i);
                i--;
            }
        }
    }

    @Override
    public void verificarColision(Jugador tarro) {
        for (int i = 0; i < gotas.size; i++) {
            Rectangle r = gotas.get(i);
            if (r.overlaps(tarro.getArea())) {
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
        gota.dispose();
    }
}
