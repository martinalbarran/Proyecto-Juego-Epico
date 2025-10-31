package puppy.code;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public interface Ataque {
    void actualizar(float delta, Rectangle areaJefe);
    void dibujar(SpriteBatch batch);
    void verificarColision(Entidad tarro);
    boolean haFinalizado();
    void destruir();
    default Float getPosicionObjetivoX(Rectangle areaJefe, Entidad entidad) {
        return null;
    }
}