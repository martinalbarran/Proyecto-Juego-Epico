package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Rectangle;

public class ControlWASD implements ControlStrategy {
    @Override
    public void mover(Jugador j) {
        Rectangle r = j.getAreaEntidad();
        float vel = j.getVelocidad();

        if (Gdx.input.isKeyPressed(Input.Keys.W)) r.y += vel * Gdx.graphics.getDeltaTime();
        if (Gdx.input.isKeyPressed(Input.Keys.S)) r.y -= vel * Gdx.graphics.getDeltaTime();
        if (Gdx.input.isKeyPressed(Input.Keys.A)) r.x -= vel * Gdx.graphics.getDeltaTime();
        if (Gdx.input.isKeyPressed(Input.Keys.D)) r.x += vel * Gdx.graphics.getDeltaTime();
    }
}
