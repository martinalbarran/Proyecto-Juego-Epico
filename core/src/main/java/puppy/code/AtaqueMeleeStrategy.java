package puppy.code;

public class AtaqueMeleeStrategy implements AtaqueStrategy {

    @Override
    public Ataque crearAtaque(Entidad entidad) {
        return new AtaqueMeleeJugador(entidad.getAreaEntidad());
    }
}	