package puppy.code;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;
import java.util.List;

public class Jugador extends Entidad {

    private ControlStrategy controlStrategy;  
    private AtaqueStrategy ataqueStrategy;     
    private List<Ataque> ataquesActivos;       

    public Jugador(Texture tex, Texture texAlt, Sound ss, int vida, int velocidad,int x, int y, int ancho, int alto, ControlStrategy control, AtaqueStrategy ataque) {
        super(tex, texAlt, ss, vida, velocidad, x, y, ancho, alto);

        this.controlStrategy = control;
        this.ataqueStrategy = ataque;
        this.ataquesActivos = new ArrayList<>();
    }
    
    @Override
    public void dibujar(SpriteBatch batch) {
        super.dibujar(batch);

        for (Ataque a : ataquesActivos) {
            a.dibujar(batch);
        }
    }
    
    @Override
    public void mover(float delta) {
        if (controlStrategy != null)
            controlStrategy.mover(this, delta);

        limitarPantalla();
    }
    
    public void procesarAtaques(float delta, List<Jugador> jugadores) {
        for (int i = 0; i < ataquesActivos.size(); i++) {
            Ataque a = ataquesActivos.get(i);
            a.actualizar(delta, null);

            if (a.haFinalizado()) {
                a.destruir();
                ataquesActivos.remove(i);
                i--;
            }
        }
    }

    public void ejecutarAtaque() {
        if (ataqueStrategy != null) {
            Ataque nuevo = ataqueStrategy.crearAtaque(this);
            if (nuevo != null) {
                ataquesActivos.add(nuevo);
                setAtaqueActual(nuevo);
            }
        }
    }

    public void verificarColisiones(Entidad jefe) {
        if (jefe == null) return;

        for (Ataque a : ataquesActivos) {
            a.verificarColision(jefe);
        }
    }

    @Override
    public void limitarPantalla() {
        Rectangle r = getAreaEntidad();

        if (r.x < 0) r.x = 0;
        if (r.x > 800 - r.width) r.x = 800 - r.width;
        if (r.y < 0) r.y = 0;
        if (r.y > 350 - r.height) r.y = 350 - r.height;
    }

}
