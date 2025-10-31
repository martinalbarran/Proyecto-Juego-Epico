package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class Jefe extends Entidad {
    private Rectangle area;       
    private Rectangle hitbox;     

    private List<Supplier<Ataque>> ataquesDisponibles;
    private int indiceAtaqueActual = 0; 
    private Ataque ataqueActual;
    private Supplier<Ataque> ataquePendienteFactory;
    private Ataque ataquePendienteInstanciaPreview;
    private float tiempoEnfriamiento = 0f;
    private float temporizadorEnfriamiento = 0f;
    private boolean enCooldown = false;
    private Entidad objetivoSeleccionado;
    
    public Jefe() {
    	super(new Texture(Gdx.files.internal("jefe.png")),new Texture(Gdx.files.internal("bucket.png")),Gdx.audio.newSound(Gdx.files.internal("hurt.ogg")), 20, 550);
        area = new Rectangle(800 / 2f - 64, 480 - 128, 128, 128);
        hitbox = new Rectangle(area.x, area.y, area.width, area.height);
        ataquesDisponibles = new ArrayList<>();
        ataque();
    }

    @Override
    public void ataque() {
        ataquesDisponibles.add(() -> new AtaqueMitadPantalla(true, 1.5f, 3f));
        ataquesDisponibles.add(() -> new AtaqueMitadPantalla(false, 1.5f, 3f));
        ataquesDisponibles.add(AtaqueGotas::new);
    }

    public void registrarAtaque(Supplier<Ataque> nuevoAtaque) {
        ataquesDisponibles.add(nuevoAtaque);
    }

    public void actualizar(float delta, List<Entidad> jugadores) {
        if (jugadores == null || jugadores.isEmpty()) return;


        if (objetivoSeleccionado == null || (ataqueActual == null && ataquePendienteFactory == null && !enCooldown)) {
            int index = MathUtils.random(jugadores.size() - 1);
            objetivoSeleccionado = jugadores.get(index);
        }

        actualizarHitbox();

        if (ataqueActual != null) {
            moverSegunAtaque(delta, objetivoSeleccionado);
            ataqueActual.actualizar(delta, area);

            for (Entidad jugador : jugadores) {
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

            Float objetivoX = ataquePendienteInstanciaPreview.getPosicionObjetivoX(area, objetivoSeleccionado);
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
        
        if(ataqueActual != null) {
	        if (ataqueActual.haFinalizado()) {
	            ataqueActual.destruir();
	            ataqueActual = null;
	            enCooldown = true;
	            temporizadorEnfriamiento = tiempoEnfriamiento;
	        }
        }

        seleccionarAtaquePendienteSecuencial();
    }


    private void seleccionarAtaquePendienteSecuencial() {
        if (ataquesDisponibles.isEmpty()) return;

        ataquePendienteFactory = ataquesDisponibles.get(indiceAtaqueActual);
        ataquePendienteInstanciaPreview = null;

        indiceAtaqueActual++;
        if (indiceAtaqueActual >= ataquesDisponibles.size()) {
            indiceAtaqueActual = 0; 
        }
    }

    private void moverHaciaX(float destinoX, float delta) {
        float diferencia = destinoX - area.x;
        area.x += diferencia * 5f * delta;
        actualizarHitbox();
    }
    
    private void moverSegunAtaque(float delta, Entidad objetivoSeleccionado) {
        if (ataqueActual == null) return;
        Float destinoX = ataqueActual.getPosicionObjetivoX(area, objetivoSeleccionado);
        if (destinoX != null) moverHaciaX(destinoX, delta);
    }

    private void actualizarHitbox() {
        hitbox.setPosition(area.x, area.y);
    }

    public Rectangle getHitbox() {
        return hitbox;
    }

    public void dibujarJefe(SpriteBatch batch) {
    	if(isHerido()) {
    		batch.draw(getTexturaHurt(), area.x+MathUtils.random(-5,5), area.y+MathUtils.random(-5,5), area.width+MathUtils.random(-5,5), area.height+MathUtils.random(-5,5));
			setTiempoHerido(getTiempoHerido()-1);
			if (getTiempoHerido()<=0) setHerido(false);
    	} else {
    		batch.draw(getTextura(), area.x, area.y, area.width, area.height);
    	}
    	if (ataqueActual != null) ataqueActual.dibujar(batch);
    }
        
	  
    public void destruir() {
        getTextura().dispose();
        if (ataqueActual != null) ataqueActual.destruir();
        if (ataquePendienteInstanciaPreview != null) ataquePendienteInstanciaPreview.destruir();
    }

	@Override
	public void actualizarMovimiento() {
	}

}