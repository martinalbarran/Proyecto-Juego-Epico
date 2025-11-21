package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;

import java.util.List;

public class AtaqueZonaSegura implements Ataque {

    private float duracionPreparacion;
    private float duracionAtaque;

    private float tiempo = 0f;
    private boolean enPreparacion = true;
    private boolean terminado = false;

    private Circle zona;
    private Texture texturaZona;
    private List<Jugador> jugadoresEnCombate;

    // --- Rayo full-screen ---
    private Texture texturaRayo;
    private boolean mostrarRayo = false;
    private float tiempoRayo = 0f;
    private final float duracionRayo = 0.35f;

    private boolean danioAplicado = false;

    public AtaqueZonaSegura(float duracionPreparacion, float duracionAtaque, List<Jugador> jugadores) {
        this.duracionPreparacion = duracionPreparacion;
        this.duracionAtaque = duracionAtaque;
        this.jugadoresEnCombate = jugadores;

        float centerX = 400f;
        float centerY = 240f;
        float radius = 80f;

        zona = new Circle(centerX, centerY, radius);
        texturaZona = new Texture(Gdx.files.internal("zona.png"));

        texturaRayo = new Texture(Gdx.files.internal("rayo_ataque.png"));
    }

    @Override
    public void actualizar(float delta, Rectangle areaJefe) {
        if (terminado) return;

        tiempo += delta;

        if (enPreparacion && tiempo >= duracionPreparacion) {
            enPreparacion = false;
            tiempo = 0;

            mostrarRayo = true;
            tiempoRayo = 0f;

            if (!danioAplicado) {
                evaluarDaño();
                danioAplicado = true;
            }
        } else if (!enPreparacion && tiempo >= duracionAtaque) {
            terminado = true;
        }

        if (mostrarRayo) {
            tiempoRayo += delta;
            if (tiempoRayo >= duracionRayo) {
                mostrarRayo = false;
            }
        }
    }

    private void evaluarDaño() {
        int dentro = 0;

        for (Entidad jugador : jugadoresEnCombate) {
            if (estaDentro(jugador)) dentro++;
        }

        if (dentro == jugadoresEnCombate.size()) {
            return;
        }

        for (Entidad jugador : jugadoresEnCombate) {
            jugador.dañar();
        }
    }

    private boolean estaDentro(Entidad entidad) {
        Rectangle r = entidad.getAreaEntidad();
        float cx = r.x + r.width / 2f;
        float cy = r.y + r.height / 2f;

        return zona.contains(cx, cy);
    }

    @Override
    public void dibujar(SpriteBatch batch) {
        if (terminado) return;

        batch.setColor(
            enPreparacion ? new Color(1f, 0.5f, 0.1f, 0.4f)
                          : new Color(1f, 0.1f, 0.1f, 0.8f)
        );

        batch.draw(
            texturaZona,
            zona.x - zona.radius,
            zona.y - zona.radius,
            zona.radius * 2f,
            zona.radius * 2f
        );

        if (mostrarRayo) {
            float screenW = Gdx.graphics.getWidth();
            float screenH = Gdx.graphics.getHeight();

            batch.setColor(1f, 1f, 1f, 0.85f);
            batch.draw(texturaRayo, 0f, 0f, screenW, screenH);
        }

        batch.setColor(Color.WHITE);
    }

    @Override
    public void verificarColision(Entidad entidad) {
    }

    @Override
    public boolean haFinalizado() {
        return terminado;
    }

    @Override
    public void destruir() {
        if (texturaZona != null) texturaZona.dispose();
        if (texturaRayo != null) texturaRayo.dispose();
    }

    @Override
    public Float getPosicionObjetivoX(Rectangle areaJefe, Entidad entidad) {
        return null; 
    }
}


