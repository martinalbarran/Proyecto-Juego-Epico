package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.List;

public class AtaqueEmbestidaDirigida implements Ataque {

    private float duracionPreparacion = 1.0f;
    private float duracionEmbestida = 0.35f;

    private float tiempo = 0f;
    private boolean enPreparacion = true;
    private boolean enEmbestida = false;
    private boolean terminado = false;

    private Texture texturaAdvertencia;
    private Rectangle areaAdvertencia;

    private Entidad objetivo;
    private List<Jugador> todosLosJugadores;

    private Vector2 posicionObjetivoInicial;
    private Vector2 posicionJefeInicial;

    private Rectangle areaJefeActual;

    private float velocidadEmbestida = 1200f;
    private boolean dañoAplicado = false;

    private float radioProtector = 80f;

    public AtaqueEmbestidaDirigida(Entidad objetivo, List<Jugador> jugadores) {
        this.objetivo = objetivo;
        this.todosLosJugadores = jugadores;

        texturaAdvertencia = new Texture(Gdx.files.internal("advertencia_circulo.png"));

        posicionObjetivoInicial = new Vector2(
                objetivo.getAreaEntidad().x + objetivo.getAreaEntidad().width / 2f,
                objetivo.getAreaEntidad().y + objetivo.getAreaEntidad().height / 2f
        );

        areaAdvertencia = new Rectangle(
                posicionObjetivoInicial.x - 48,
                posicionObjetivoInicial.y - 48,
                96,
                96
        );
    }

    @Override
    public void actualizar(float delta, Rectangle areaJefe) {

        if (terminado) return;

        if (posicionJefeInicial == null) {
            posicionJefeInicial = new Vector2(areaJefe.x, areaJefe.y);
        }

        if (areaJefeActual == null) areaJefeActual = new Rectangle();
        areaJefeActual.set(areaJefe);

        tiempo += delta;

        if (enPreparacion) {
            if (tiempo >= duracionPreparacion) {
                enPreparacion = false;
                enEmbestida = true;
                tiempo = 0f;
            }
            return;
        }

        if (enEmbestida) {

            Vector2 dir = new Vector2(
                    posicionObjetivoInicial.x - (areaJefe.x + areaJefe.width / 2f),
                    posicionObjetivoInicial.y - (areaJefe.y + areaJefe.height / 2f)
            );

            if (dir.len() > 5f) {
                dir.nor().scl(velocidadEmbestida * delta);
                areaJefe.x += dir.x;
                areaJefe.y += dir.y;
            }

            if (tiempo >= duracionEmbestida) {
                enEmbestida = false;
                tiempo = 0f;
            }
            return;
        }

        if (!enEmbestida && !enPreparacion && !terminado) {

            Vector2 dir = new Vector2(
                    posicionJefeInicial.x - areaJefe.x,
                    posicionJefeInicial.y - areaJefe.y
            );

            if (dir.len() > 5f) {
                dir.nor().scl(800f * delta);
                areaJefe.x += dir.x;
                areaJefe.y += dir.y;
            } else {
                terminado = true;
            }
        }
    }

    @Override
    public void dibujar(SpriteBatch batch) {
        if (terminado) return;

        if (enPreparacion) {
            float alpha = 0.4f + 0.3f * (float)Math.sin(System.currentTimeMillis() * 0.02f);

            batch.setColor(1, 0, 0, alpha);
            batch.draw(texturaAdvertencia,
                    areaAdvertencia.x,
                    areaAdvertencia.y,
                    areaAdvertencia.width,
                    areaAdvertencia.height
            );
            batch.setColor(Color.WHITE);
        }
    }

    @Override
    public void verificarColision(Entidad jugador) {
        if (!enEmbestida || terminado || dañoAplicado) return;
        if (jugador != objetivo) return;

        if (areaJefeActual == null) return;

        if (!areaJefeActual.overlaps(jugador.getAreaEntidad())) return;

        float cxObj = jugador.getAreaEntidad().x + jugador.getAreaEntidad().width / 2f;
        float cyObj = jugador.getAreaEntidad().y + jugador.getAreaEntidad().height / 2f;

        boolean protegido = false;
        if (todosLosJugadores != null) {
            for (Jugador otro : todosLosJugadores) {
                if (otro == jugador) continue;
                float cxOtro = otro.getAreaEntidad().x + otro.getAreaEntidad().width / 2f;
                float cyOtro = otro.getAreaEntidad().y + otro.getAreaEntidad().height / 2f;
                float dx = cxOtro - cxObj;
                float dy = cyOtro - cyObj;
                if (dx*dx + dy*dy <= radioProtector * radioProtector) {
                    protegido = true;
                    break;
                }
            }
        }

        if (!protegido) {
            jugador.dañar();
        }
        dañoAplicado = true;
    }

    @Override
    public boolean haFinalizado() {
        return terminado;
    }

    @Override
    public void destruir() {
        if (texturaAdvertencia != null)
            texturaAdvertencia.dispose();
    }

    @Override
    public Float getPosicionObjetivoX(Rectangle areaJefe, Entidad entidad) {
        return null;
    }
}

