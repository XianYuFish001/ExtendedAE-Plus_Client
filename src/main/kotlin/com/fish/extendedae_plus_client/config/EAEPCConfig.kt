package com.fish.extendedae_plus_client.config

import com.fish.extendedae_plus_client.ExtendedAEPlusClient
import dev.toma.configuration.Configuration
import dev.toma.configuration.config.Config
import dev.toma.configuration.config.Configurable
import dev.toma.configuration.config.format.ConfigFormats

@Config(id = ExtendedAEPlusClient.MODID, filename = "extendedae_plus/client")
class EAEPCConfig {
    companion object {
        @JvmStatic
        var instance: EAEPCConfig? = null
            private set

        fun init() {
            if (instance == null) {
                instance = Configuration.registerConfig(
                    EAEPCConfig::class.java, ConfigFormats.YAML)
                    .configInstance
            }
        }
    }

    @Configurable
    @Configurable.Range(min = 1, max = 64)
    var autoPlateRepeat: Int = 1

    @Configurable
    var modeEncodingTransfer: ModeEncodingTransfer = ModeEncodingTransfer.MERGE_ADJACENCY
}
