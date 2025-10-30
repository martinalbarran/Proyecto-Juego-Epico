package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class Jefe {

    private Texture textura;
    private Rectangle area;
    private float velocidadMovimiento = 350f;

    private List<Supplier<AtaqueJefe>> ataquesDisponibles;
    private AtaqueJefe ataqueActual;
    private Supplier<AtaqueJefe> ataquePendienteFactory;
    private AtaqueJefe ataquePendienteInstanciaPreview;
    private float tiempoEnfriamiento = 3f;
    private float temporizadorEnfriamiento = 0f;
    private boolean enCooldown = false;

    public Jefe() {
        textura = new Texture(Gdx.files.internal("jefe.png"));
        area = new Rectangle(800 / 2f - 64, 480 - 128, 128, 128);
        ataquesDisponibles = new ArrayList<>();
        registrarAtaquesBase();
    }

    private void registrarAtaquesBase() {
        ataquesDisponibles.add(() -> new AtaqueMitadPantalla(true, 1.5f, 3f));
        ataquesDisponibles.add(() -> new AtaqueMitadPantalla(false, 1.5f, 3f));
        ataquesDisponibles.add(AtaqueGotas::new);
    }

    public void registrarAtaque(Supplier<AtaqueJefe> nuevoAtaque) {
        ataquesDisponibles.add(nuevoAtaque);
    }

    public void actualizar(float delta, List<Jugador> jugadores) {
        if (ataqueActual != null) {
            moverSegunAtaque(delta, jugadores);
            ataqueActual.actualizar(delta, area);

            for (Jugador jugador : jugadores) {
                ataqueActual.verificarColision(jugador);
            }

            if (ataqueActual.haFinalizado()) {
                ataqueActual.destruir();
                ataqueActual = null;
                enCooldown = true;
                temporizadorEnfriamiento = tiempoEnfriamiento;
            }
            return;
        }

        if (ataquePendienteFactory != null) {
            if (ataquePendienteInstanciaPreview == null)
                ataquePendienteInstanciaPreview = ataquePendienteFactory.get();

            Float objetivoX = ataquePendienteInstanciaPreview.getPosicionObjetivoX(area, jugadores);
            if (objetivoX == null) {
                ataqueActual = ataquePendienteInstanciaPreview;
                ataquePendienteFactory = null;
                ataquePendienteInstanciaPreview = null;
            } else {
                moverHaciaX(objetivoX, delta);
                if (Math.abs(area.x - objetivoX) <= 4f) {
                    ataqueActual = ataquePendienteInstanciaPreview;
                    ataquePendienteFactory = null;
                    ataquePendienteInstanciaPreview = null;
                }
            }
            return;
        }

        if (enCooldown) {
            temporizadorEnfriamiento -= delta;
            if (temporizadorEnfriamiento <= 0) enCooldown = false;
            return;
        }

        seleccionarAtaquePendienteAleatorio();
    }

    private void seleccionarAtaquePendienteAleatorio() {
        int index = MathUtils.random(ataquesDisponibles.size() - 1);
        ataquePendienteFactory = ataquesDisponibles.get(index);
        ataquePendienteInstanciaPreview = null;
    }

    private void moverHaciaX(float destinoX, float delta) {
        float diferencia = destinoX - area.x;
        if (Math.abs(diferencia) > 1f) {
            area.x += Math.signum(diferencia) * velocidadMovimiento * delta;
        }
    }

    private void moverSegunAtaque(float delta, List<Jugador> jugadores) {
        if (ataqueActual == null) return;
        Float destinoX = ataqueActual.getPosicionObjetivoX(area, jugadores);
        if (destinoX != null) moverHaciaX(destinoX, delta);
    }

    public void dibujar(SpriteBatch batch) {
        batch.draw(textura, area.x, area.y, area.width, area.height);
        if (ataqueActual != null) ataqueActual.dibujar(batch);
    }

    public void destruir() {
        textura.dispose();
        if (ataqueActual != null) ataqueActual.destruir();
        if (ataquePendienteInstanciaPreview != null) ataquePendienteInstanciaPreview.destruir();
    }
}
