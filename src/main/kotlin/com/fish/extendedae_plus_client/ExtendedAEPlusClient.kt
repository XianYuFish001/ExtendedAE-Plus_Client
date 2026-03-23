package com.fish.extendedae_plus_client

import com.fish.extendedae_plus_client.integration.ManagerIntegration
import com.fish.fishlib.common.InitializerObject
import com.mojang.logging.LogUtils
import net.minecraft.resources.Identifier
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.IEventBus
import net.neoforged.fml.ModContainer
import net.neoforged.fml.common.Mod

@Mod(ExtendedAEPlusClient.MODID, dist = [Dist.CLIENT])
class ExtendedAEPlusClient(eventBus : IEventBus, containerMod: ModContainer) {
    init {
        InitializerObject(eventBus, containerMod)
        ManagerIntegration.init()
    }

    @Mod(value = MODID, dist = [Dist.DEDICATED_SERVER])
    class ExtendedAEPlusServer(eventBus: IEventBus, modContainer: ModContainer) {
        init {
            LogUtils.getLogger()
                .warn("This is a client-side mod and it won't work on servers. Please use ExtendedAE Plus (common) instead.")
        }
    }

    companion object {
        const val MODID = "extendedae_plus_client"

        fun getLocation(path : String): Identifier =
            Identifier.fromNamespaceAndPath(MODID, path)
    }
}