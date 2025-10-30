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
    private float velocidadMovimiento = 150f;
    private int direccion = 1;

    private List<Supplier<AtaqueJefe>> ataquesDisponibles; 
    private AtaqueJefe ataqueActual;
    private float tiempoEnfriamiento = 3f;
    private float temporizadorEnfriamiento = 0f;
    private boolean enCooldown = false;

    public Jefe() {
        textura = new Texture(Gdx.files.internal("jefe.png"));
        area = new Rectangle(800 / 2f - 64, 480 - 128, 128, 128);

        ataquesDisponibles = new ArrayList<>();
        registrarAtaquesBase();
    }

      //registranlos ataques base del jefe
     //se puede agregar más en cualquier momento desde fuera
    
    private void registrarAtaquesBase() {
        ataquesDisponibles.add(() -> new AtaqueMitadPantalla(MathUtils.randomBoolean(), 2f, 2.5f));
        ataquesDisponibles.add(() -> new AtaqueMitadPantalla(true, 1.5f, 3f));
        ataquesDisponibles.add(() -> new AtaqueMitadPantalla(false, 1.5f, 3f));
        ataquesDisponibles.add(AtaqueGotas::new);
    }
  
    public void registrarAtaque(Supplier<AtaqueJefe> nuevoAtaque) {
        ataquesDisponibles.add(nuevoAtaque);
    }

    public void actualizar(float delta, Jugador tarro) {
        moverHorizontalmente(delta);

        if (ataqueActual != null) {
            ataqueActual.actualizar(delta, area);
            ataqueActual.verificarColision(tarro);

            if (ataqueActual.haFinalizado()) {
                ataqueActual.destruir();
                ataqueActual = null;
                enCooldown = true;
                temporizadorEnfriamiento = tiempoEnfriamiento;
            }
        } else if (enCooldown) {
            temporizadorEnfriamiento -= delta;
            if (temporizadorEnfriamiento <= 0) enCooldown = false;
        } else {
            seleccionarAtaqueAleatorio();
        }
    }

    private void moverHorizontalmente(float delta) {
        area.x += velocidadMovimiento * delta * direccion;

        if (area.x <= 0) {
            area.x = 0;
            direccion = 1;
        } else if (area.x + area.width >= 800) {
            area.x = 800 - area.width;
            direccion = -1;
        }
    }

    private void seleccionarAtaqueAleatorio() {
        int index = MathUtils.random(ataquesDisponibles.size() - 1);
        ataqueActual = ataquesDisponibles.get(index).get();
    }

    public void dibujar(SpriteBatch batch) {
        batch.draw(textura, area.x, area.y, area.width, area.height);
        if (ataqueActual != null)
            ataqueActual.dibujar(batch);
    }

    public void destruir() {
        textura.dispose();
        if (ataqueActual != null)
            ataqueActual.destruir();
    }
}