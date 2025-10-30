package puppy.code;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public interface AtaqueJefe {
    void actualizar(float delta, Rectangle areaJefe);
    void dibujar(SpriteBatch batch);
    void verificarColision(Jugador tarro);
    boolean haFinalizado();
    void destruir();
}