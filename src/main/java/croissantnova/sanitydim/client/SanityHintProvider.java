package croissantnova.sanitydim.client;

import croissantnova.sanitydim.SanityMod;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;

public enum SanityHintProvider {

    PARANOID(new ArrayList<>()),
    INSANE(new ArrayList<>()),
    INSOMNIA(new ArrayList<>());

    private final List<SanityHint> hints;

    SanityHintProvider(List<SanityHint> hints) {
        this.hints = hints;
    }

    private void load() {
        for (int i = 0; ; i++) {
            String baseKey = String.format("gui.%s.hint.%s.%s", SanityMod.MOD_ID, this.toString().toLowerCase(), i);
            String swishKey = baseKey + ".swish";

            if (translationKeyExists(baseKey)) {
                hints.add(new SanityHint(baseKey, Component.translatable(baseKey), false));
            }
            else if (translationKeyExists(swishKey)) {
                hints.add(new SanityHint(swishKey, Component.translatable(swishKey), true));
            }
            else {
                break;
            }
        }
    }

    private boolean translationKeyExists(String key) {
        return Language.getInstance().has(key);
    }

    public List<SanityHint> getHints() {
        if (hints.isEmpty()) {
            load();
        }
        return hints;
    }

    public SanityHint getRandomHint() {
        if (hints.isEmpty()) {
            load();
        }
        int i = (int) (Math.random() * hints.size());
        return hints.get(i);
    }
}

