package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class AtaqueMitadPantalla implements AtaqueJefe {

    private boolean ladoIzquierdo;
    private float duracionPreparacion;
    private float duracionAtaque;
    private float tiempo;

    private Texture texturaPreparacion;
    private Texture texturaAtaque;
    private Rectangle areaAtaque;

    private boolean enPreparacion = true;
    private boolean terminado = false;
    private boolean dañoAplicado = false; 

    public AtaqueMitadPantalla(boolean ladoIzquierdo, float duracionPreparacion, float duracionAtaque) {
        this.ladoIzquierdo = ladoIzquierdo;
        this.duracionPreparacion = duracionPreparacion;
        this.duracionAtaque = duracionAtaque;
        this.tiempo = 0;

        texturaPreparacion = new Texture(Gdx.files.internal("rayo_preparacion.png"));
        texturaAtaque = new Texture(Gdx.files.internal("rayo_ataque.png"));
        crearArea();
    }

    private void crearArea() {
        areaAtaque = new Rectangle();
        areaAtaque.x = ladoIzquierdo ? 0 : 400;
        areaAtaque.y = 0;
        areaAtaque.width = 400;
        areaAtaque.height = 480;
    }

    @Override
    public void actualizar(float delta, Rectangle areaJefe) {
        if (terminado) return;
        tiempo += delta;

        if (enPreparacion && tiempo >= duracionPreparacion) {
            enPreparacion = false;
            tiempo = 0;
        } else if (!enPreparacion && tiempo >= duracionAtaque) {
            terminado = true;
        }
    }

    @Override
    public void dibujar(SpriteBatch batch) {
        if (terminado) return;

        if (enPreparacion) {
            float alpha = 0.3f + 0.2f * (float) Math.sin(System.currentTimeMillis() * 0.01f);
            batch.setColor(1, 0, 0, alpha);
            batch.draw(texturaPreparacion, areaAtaque.x, areaAtaque.y, areaAtaque.width, areaAtaque.height);
            batch.setColor(Color.WHITE);
        } else {
            batch.setColor(1, 1, 1, 1);
            batch.draw(texturaAtaque, areaAtaque.x, areaAtaque.y, areaAtaque.width, areaAtaque.height);
        }
    }

    @Override
    public void verificarColision(Jugador tarro) {
        if (!enPreparacion && !terminado && !dañoAplicado) {
            if (areaAtaque.overlaps(tarro.getArea())) {
                tarro.dañar();
                dañoAplicado = true; 
            }
        }
    }

    @Override
    public boolean haFinalizado() {
        return terminado;
    }

    @Override
    public void destruir() {
        texturaPreparacion.dispose();
        texturaAtaque.dispose();
    }

    public Rectangle getAreaAtaque() {
        return areaAtaque;
    }

    public boolean estaActivo() {
        return !enPreparacion && !terminado;
    }
}
