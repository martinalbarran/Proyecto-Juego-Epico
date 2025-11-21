package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Rectangle;

public class ControlFlechas implements ControlStrategy {
    @Override
    public void mover(Jugador j) {
        Rectangle r = j.getAreaEntidad();
        float vel = j.getVelocidad() * Gdx.graphics.getDeltaTime();

        if (Gdx.input.isKeyPressed(Input.Keys.UP)) r.y += vel;
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) r.y -= vel;
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) r.x -= vel;
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) r.x += vel;
    }
}