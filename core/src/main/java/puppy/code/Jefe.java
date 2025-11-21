package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class Jefe extends Entidad {
    private List<Supplier<Ataque>> ataquesDisponibles;
    private int indiceAtaqueActual = 0; 
    private Supplier<Ataque> ataquePendienteFactory;
    private Ataque ataquePendienteInstanciaPreview;
    private float tiempoEnfriamiento = 0f;
    private float temporizadorEnfriamiento = 0f;
    private boolean enCooldown = false;
    private Entidad objetivoSeleccionado;
    
    public Jefe() {
    	super(new Texture(Gdx.files.internal("jefe.png")),new Texture(Gdx.files.internal("jefeHurt.png")),Gdx.audio.newSound(Gdx.files.internal("good.mp3")), 100, 550, 336 , 352, 128, 128);
        ataquesDisponibles = new ArrayList<>();
        ataque();
    }

    public void ataque() {
    	ataquesDisponibles.add(() -> new AtaqueRayos(true)); 
    	ataquesDisponibles.add(() -> new AtaqueMitadPantalla(false, 1f, 1f));    	
    	ataquesDisponibles.add(() -> new AtaqueRayos(false));
        ataquesDisponibles.add(() -> new AtaqueMitadPantalla(true, 1f, 1f));
        ataquesDisponibles.add(AtaqueGotas::new);
    }

    public void registrarAtaque(Supplier<Ataque> nuevoAtaque) {
        ataquesDisponibles.add(nuevoAtaque);
    }


    public void procesarAtaques(float delta, List<Jugador> jugadores) {
        if (jugadores == null || jugadores.isEmpty()) return;


        if (objetivoSeleccionado == null || (getAtaqueActual() == null && ataquePendienteFactory == null && !enCooldown)) {
            int index = MathUtils.random(jugadores.size() - 1);
            objetivoSeleccionado = jugadores.get(index);
        }

        if (getAtaqueActual() != null) {
            moverSegunAtaque(delta, objetivoSeleccionado);
            getAtaqueActual().actualizar(delta, getAreaEntidad());

            for (Entidad jugador : jugadores) {
            	getAtaqueActual().verificarColision(jugador);
            }

            if (getAtaqueActual().haFinalizado()) {
            	getAtaqueActual().destruir();
                setAtaqueActual(null);
                enCooldown = true;
                temporizadorEnfriamiento = tiempoEnfriamiento;
            }
            return;
        }

        if (ataquePendienteFactory != null) {
            if (ataquePendienteInstanciaPreview == null)
                ataquePendienteInstanciaPreview = ataquePendienteFactory.get();

            Float objetivoX = ataquePendienteInstanciaPreview.getPosicionObjetivoX(getAreaEntidad(), objetivoSeleccionado);
            if (objetivoX == null) {
                setAtaqueActual(ataquePendienteInstanciaPreview);
                ataquePendienteFactory = null;
                ataquePendienteInstanciaPreview = null;
            } else {
                moverHaciaX(objetivoX, delta);
                if (Math.abs(getAreaEntidad().x - objetivoX) <= 4f) {
                	setAtaqueActual(ataquePendienteInstanciaPreview);
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
        
        if(getAtaqueActual() != null) {
	        if (getAtaqueActual().haFinalizado()) {
	        	getAtaqueActual().destruir();
	            setAtaqueActual(null);
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
        float diferencia = destinoX - getAreaEntidad().x;
        getAreaEntidad().x += diferencia * 5f * delta;
    }
    
    private void moverSegunAtaque(float delta, Entidad objetivoSeleccionado) {
        if (getAtaqueActual() == null) return;
        Float destinoX = getAtaqueActual().getPosicionObjetivoX(getAreaEntidad(), objetivoSeleccionado);
        if (destinoX != null) moverHaciaX(destinoX, delta);
    }


    @Override
    public void dibujar(SpriteBatch batch) {
    	if(estaHerido()) {
    		batch.draw(getTexturaHurt(), getAreaEntidad().x+MathUtils.random(-5,5), getAreaEntidad().y+MathUtils.random(-5,5), getAreaEntidad().width+MathUtils.random(-5,5), getAreaEntidad().height+MathUtils.random(-5,5));
			setTiempoHerido(getTiempoHerido()-1);
			if (getTiempoHerido()<=0) setHerido(false);
    	} else {
    		batch.draw(getTextura(), getAreaEntidad().x, getAreaEntidad().y, getAreaEntidad().width, getAreaEntidad().height);
    	}
    	if (getAtaqueActual() != null) getAtaqueActual().dibujar(batch);
    }
        
	  
    public void destruir() {
        getTextura().dispose();
        if (getAtaqueActual() != null) getAtaqueActual().destruir();
        if (ataquePendienteInstanciaPreview != null) ataquePendienteInstanciaPreview.destruir();
    }

    
    public void mover(float delta) {
        if (getAtaqueActual() != null) {
            moverSegunAtaque(delta, objetivoSeleccionado);
            return;
        }

        if (ataquePendienteInstanciaPreview != null) {
            Float x = ataquePendienteInstanciaPreview.getPosicionObjetivoX(getAreaEntidad(), objetivoSeleccionado);
            if (x != null) moverHaciaX(x, delta);
        }
    }

}