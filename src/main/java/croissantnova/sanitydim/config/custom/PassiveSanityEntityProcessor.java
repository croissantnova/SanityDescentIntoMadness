package croissantnova.sanitydim.config.custom;

import croissantnova.sanitydim.SanityMod;
import croissantnova.sanitydim.config.ConfigManager;
import net.minecraft.resources.ResourceLocation;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class PassiveSanityEntityProcessor {
    private static final int PARAMETER_COUNT = 3;
    private static final int INDEX_ID = 0;
    private static final int INDEX_SANITY = 1;
    private static final int INDEX_RADIUS = 2;

    public static List<PassiveSanityEntity> processList(List<? extends String> raw) {
        return raw
                .stream()
                .map(PassiveSanityEntityProcessor::parse)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private static PassiveSanityEntity parse(String entry) {
        String[] parameters = splitBySemicolon(entry);
        if (parameters.length != PARAMETER_COUNT) {
            SanityMod.LOGGER.error("config format error in {} -> the number of parameters is not {}", entry, PARAMETER_COUNT);
            return null;
        }

        Float sanity = convertToFloat(entry, parameters[INDEX_SANITY]);
        Float radius = convertToFloat(entry, parameters[INDEX_RADIUS]);
        if (radius == null || sanity == null) {
            return null;
        }

        return new PassiveSanityEntity(
                new ResourceLocation(parameters[INDEX_ID]),
                ConfigManager.finalizePassive(sanity),
                radius
        );
    }

    private static String[] splitBySemicolon(String entry) {
        return entry.trim().split("\\s*;\\s*", PARAMETER_COUNT);
    }

    private static Float convertToFloat(String entry, String parameter) {
        try {
            return Float.parseFloat(parameter);
        }
        catch (NumberFormatException e) {
            SanityMod.LOGGER.error("config format error in {} -> can't convert {} to float", entry, parameter);
            return null;
        }
    }
}
