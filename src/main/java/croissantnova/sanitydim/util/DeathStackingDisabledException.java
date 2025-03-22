package croissantnova.sanitydim.util;

public class DeathStackingDisabledException extends RuntimeException {

    public DeathStackingDisabledException() {
        super("Death stacking is not enabled.");
    }
}
