package croissantnova.sanitydim.client;

import net.minecraft.network.chat.MutableComponent;

public record SanityHint(
        String translationKey,
        MutableComponent component,
        boolean swish
) {
}
