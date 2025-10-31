package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

import java.util.HashSet;
import java.util.Set;


public class AtaqueMitadPantalla implements Ataque {

    private boolean ladoIzquierdo;
    private float duracionPreparacion;
    private float duracionAtaque;
    private float tiempo;

    private Texture texturaPreparacion;
    private Texture texturaAtaque;
    private Rectangle areaAtaque;

    private boolean enPreparacion = true;
    private boolean terminado = false;

    
    private Set<Entidad> jugadoresHeridos;

    public AtaqueMitadPantalla(boolean ladoIzquierdo, float duracionPreparacion, float duracionAtaque) {
        this.ladoIzquierdo = ladoIzquierdo;
        this.duracionPreparacion = duracionPreparacion;
        this.duracionAtaque = duracionAtaque;
        this.tiempo = 0;

        texturaPreparacion = new Texture(Gdx.files.internal("rayo_preparacion.png"));
        texturaAtaque = new Texture(Gdx.files.internal("rayo_ataque.png"));
        crearArea();

        jugadoresHeridos = null; 
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
            jugadoresHeridos = new HashSet<Entidad>();
        } else if (!enPreparacion && tiempo >= duracionAtaque) {
            terminado = true;
            jugadoresHeridos = null;
        }
    }
    
    public Float getPosicionObjetivoX(Rectangle areaJefe, Entidad entidad) {
        float destinoX = areaAtaque.x + areaAtaque.width / 2f - areaJefe.width / 2f;
        return destinoX;
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
    public void verificarColision(Entidad jugador) {        
        if (enPreparacion || terminado) return;

        if (jugadoresHeridos != null && jugadoresHeridos.contains(jugador)) return;

        if (areaAtaque.overlaps(jugador.getAreaEntidad())) {
            jugador.dañar();
            if (jugadoresHeridos != null) {
                jugadoresHeridos.add(jugador);
            }
        }
    }

    @Override
    public boolean haFinalizado() {
        return terminado;
    }

    @Override
    public void destruir() {
        if (texturaPreparacion != null) texturaPreparacion.dispose();
        if (texturaAtaque != null) texturaAtaque.dispose();
        jugadoresHeridos = null;
    }

    public Rectangle getAreaAtaque() {
        return areaAtaque;
    }

    public boolean estaActivo() {
        return !enPreparacion && !terminado;
    }
}
