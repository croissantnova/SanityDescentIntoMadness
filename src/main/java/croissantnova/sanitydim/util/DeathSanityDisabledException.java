package croissantnova.sanitydim.util;

public class DeathSanityDisabledException extends RuntimeException {

    public DeathSanityDisabledException() {
        super("Death sanity is not enabled.");
    }
}
