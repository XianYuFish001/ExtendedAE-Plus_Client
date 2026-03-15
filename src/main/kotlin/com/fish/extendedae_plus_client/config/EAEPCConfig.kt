package com.fish.extendedae_plus_client.config

import com.fish.fishlib.common.InitObject
import com.fish.fishlib.config.HelperConfig
import com.fish.fishlib.config.HelperConfig.Companion.bind
import com.fish.fishlib.config.spec
import net.neoforged.fml.ModContainer
import net.neoforged.fml.config.ModConfig
import net.neoforged.neoforge.client.gui.ConfigurationScreen
import net.neoforged.neoforge.client.gui.IConfigScreenFactory

object EAEPCConfig {
    private val Helper: HelperConfig = HelperConfig(::Spec)

    var AutoPlateRepeat: Int by Helper
    var ModeTransfer: ModeEncodingTransfer by Helper

    private val Spec by spec(ModConfig.Type.CLIENT) { spec ->
        spec.defineInRange("auto_plate_repeat", 1, 1, 64)
            .bind(Helper, ::AutoPlateRepeat)
        spec.defineEnum("mode_encoding_transfer", ModeEncodingTransfer.MergeAdjacency)
            .bind(Helper, ::ModeTransfer)
    }

    @InitObject
    private fun init(containerMod: ModContainer) {
        Helper.init(containerMod, "extendedae_plus")
        containerMod.registerExtensionPoint(IConfigScreenFactory::class.java,
            IConfigScreenFactory(::ConfigurationScreen))
    }
}
