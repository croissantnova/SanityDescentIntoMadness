package croissantnova.sanitydim.config.custom;

import croissantnova.sanitydim.SanityMod;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public abstract class ConfigValueProcessor<T> {

    public List<T> processList(List<? extends String> raw) {
        return raw
                .stream()
                .map(this::parse)
                .flatMap(Optional::stream)
                .collect(Collectors.toList());
    }


    protected abstract int getParameterCount();

    protected abstract Optional<T> createObject(String entry, String[] parameters);


    protected Optional<T> parse(String entry) {
        String[] parameters = splitBySemicolon(entry);
        if (parameters.length != getParameterCount()) {
            reportParametersMismatch(entry);
            return Optional.empty();
        }
        return createObject(entry, parameters);
    }

    protected Optional<Float> convertToFloat(String entry, String parameter) {
        try {
            return Optional.of(Float.parseFloat(parameter));
        }
        catch (NullPointerException | NumberFormatException e) {
            reportTypeMismatch(entry, parameter, "float");
            return Optional.empty();
        }
    }

    protected Optional<Integer> convertToInteger(String entry, String parameter) {
        try {
            return Optional.of(Integer.parseInt(parameter));
        }
        catch (NumberFormatException e) {
            reportTypeMismatch(entry, parameter, "integer");
            return Optional.empty();
        }
    }

    protected String[] splitBySemicolon(String entry) {
        return entry.trim().split("\\s*;\\s*");
    }

    protected void reportParametersMismatch(String entry) {
        SanityMod.LOGGER.error("config format error in {} -> the number of parameters is not {}", entry, getParameterCount());
    }

    protected void reportTypeMismatch(String entry, String parameter, String type) {
        SanityMod.LOGGER.error("config format error in {} -> can't convert {} to {}", entry, parameter, type);
    }
}
