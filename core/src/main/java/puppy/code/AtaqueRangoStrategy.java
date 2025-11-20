package puppy.code;

public class AtaqueRangoStrategy implements AtaqueStrategy {

    @Override
    public Ataque crearAtaque(Entidad entidad) {
        return new AtaqueRangoJugador(entidad.getAreaEntidad());
    }
}