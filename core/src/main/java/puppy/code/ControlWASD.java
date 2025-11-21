package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Rectangle;

public class ControlWASD implements ControlStrategy {
    @Override
    public void mover(Jugador j, float delta) {
        Rectangle r = j.getAreaEntidad();
        float vel = j.getVelocidad() * delta;

        if (Gdx.input.isKeyPressed(Input.Keys.W)) r.y += vel;
        if (Gdx.input.isKeyPressed(Input.Keys.S)) r.y -= vel;
        if (Gdx.input.isKeyPressed(Input.Keys.A)) r.x -= vel;
        if (Gdx.input.isKeyPressed(Input.Keys.D)) r.x += vel;
    }
}
