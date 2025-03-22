package croissantnova.sanitydim.config.custom;

import croissantnova.sanitydim.config.ConfigManager;
import net.minecraft.resources.ResourceLocation;

import java.util.Optional;

public class PassiveSanityStatusEffectProcessor extends ConfigValueProcessor<PassiveSanityStatusEffect> {
    public static PassiveSanityStatusEffectProcessor INSTANCE = new PassiveSanityStatusEffectProcessor();
    private static final int INDEX_ID = 0;
    private static final int INDEX_SANITY = 1;
    private static final int INDEX_AMPLIFIER = 2;

    @Override
    protected int getParameterCount() {
        return 3;
    }

    @Override
    protected Optional<PassiveSanityStatusEffect> createObject(String entry, String[] parameters) {
        return convertToFloat(entry, parameters[INDEX_SANITY])
                .flatMap(sanity -> convertToInteger(entry, parameters[INDEX_AMPLIFIER])
                        .map(amplifier -> new PassiveSanityStatusEffect(
                                new ResourceLocation(parameters[INDEX_ID]),
                                ConfigManager.finalizePassive(sanity),
                                amplifier
                        ))
                );
    }
}
