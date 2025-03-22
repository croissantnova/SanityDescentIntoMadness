package croissantnova.sanitydim.config;

import org.jetbrains.annotations.NotNull;

import java.util.Map;

@Deprecated
public enum ConfigEntryOld {
    INNER_ENTITY_DESPAWN_MOBS_DISTANCE(
            "sanity.entity.despawn_nearby_mobs_distance",
            new ConfigManager.ProxyValueEntry<>(() -> ConfigManager.getConfigValues().entities_despawnMobsDistance.get(), ConfigManager::noFinalize)
    ),
    ROTTING_STALKER_KILL_RATIO(
            "sanity.active.rotting_stalker_kill_ratio",
            new ConfigManager.ProxyValueEntry<>(() -> ConfigManager.getConfigValues().m_rottingStalkerKillRatio.get(), ConfigManager::finalizeActive)
    ),
    SNEAKING_TERROR_KILL_RATIO(
            "sanity.active.sneaking_terror_kill_ratio",
            new ConfigManager.ProxyValueEntry<>(() -> ConfigManager.getConfigValues().m_sneakingTerrorKillRatio.get(), ConfigManager::finalizeActive)
    ),
    PASSIVE_BODY_PART_SLIGHTLY_WOUNDED(
            "sanity.passive.body_part_slightly_wounded",
            new ConfigManager.ProxyValueEntry<>(() -> ConfigManager.getConfigValues().passive_lsoCompat.bodyPartSlightlyWounded.get(), ConfigManager::finalizePassive)
    )
    ;

    private final String proxyKey;
    private final ConfigManager.ProxyValueEntry<?> proxyValueEntry;

    ConfigEntryOld(String proxyKey, ConfigManager.ProxyValueEntry<?> proxyValueEntry) {
        this.proxyKey = proxyKey;
        this.proxyValueEntry = proxyValueEntry;
    }

    public String getProxyKey() {
        return proxyKey;
    }


    public void putInProxies(@NotNull Map<String, ConfigManager.ProxyValueEntry<?>> proxies) {
        proxies.put(proxyKey, proxyValueEntry);
    }
}
