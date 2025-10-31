package puppy.code;
import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

public class JugadorRango extends Entidad {

    private List<AtaqueRangoJugador> disparos;

    public JugadorRango(Texture tex, Texture texAlt, Sound ss, int vida, int velocidad, int coordenadasX, int coordenadasY, int ancho, int alto) {
        super(tex, texAlt, ss, vida, velocidad, coordenadasX, coordenadasY, ancho, alto);
        disparos = new ArrayList<>();
    }


    public void ataque() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.B)) {
            AtaqueRangoJugador nuevoDisparo = new AtaqueRangoJugador(getAreaEntidad());
            setAtaqueActual(nuevoDisparo); 
            disparos.add(nuevoDisparo);
        }
    }

    public void chequearAtaque(float delta, Rectangle areaJefe) {
        if (getAtaqueActual() != null) {
            getAtaqueActual().actualizar(delta, areaJefe);
            if (getAtaqueActual().haFinalizado()) {
                getAtaqueActual().destruir();
                setAtaqueActual(null);
            }
        }
        for (int i = 0; i < disparos.size(); i++) {
            AtaqueRangoJugador d = disparos.get(i);
            d.actualizar(delta, areaJefe);
            if (d.haFinalizado()) {
                d.destruir();
                disparos.remove(i);
                i--;
            }
        }
    }

    // Movimiento del jugador
    public void actualizar() {
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT))
            getAreaEntidad().x -= getVelocidad() * Gdx.graphics.getDeltaTime();
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT))
            getAreaEntidad().x += getVelocidad() * Gdx.graphics.getDeltaTime();
        if (Gdx.input.isKeyPressed(Input.Keys.UP))
            getAreaEntidad().y += getVelocidad() * Gdx.graphics.getDeltaTime();
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN))
            getAreaEntidad().y -= getVelocidad() * Gdx.graphics.getDeltaTime();

        // límites del área de movimiento
        if (getAreaEntidad().x < 0) getAreaEntidad().x = 0;
        if (getAreaEntidad().x > 800 - 64) getAreaEntidad().x = 800 - 64;
        if (getAreaEntidad().y > 350 - 64) getAreaEntidad().y = 350 - 64;
        if (getAreaEntidad().y < 0) getAreaEntidad().y = 0;
    }

    public List<AtaqueRangoJugador> getDisparos() {
        return disparos;
    }
}

