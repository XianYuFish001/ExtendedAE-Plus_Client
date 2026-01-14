package com.fish.extendedae_plus_client

import com.fish.extendedae_plus_client.ExtendedAEPlusClient.Companion.MODID
import com.fish.extendedae_plus_client.config.EAEPCConfig
import com.fish.extendedae_plus_client.integration.ContextModLoaded
import com.mojang.logging.LogUtils
import net.minecraft.resources.ResourceLocation
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn
import net.minecraftforge.fml.common.Mod

@OnlyIn(Dist.CLIENT)
@Mod(MODID)
class ExtendedAEPlusClient {
    init {
        EAEPCConfig.init()
        ContextModLoaded.init()
    }


    companion object {
        const val MODID = "extendedae_plus_client"

        internal fun getLocation(path : String) : ResourceLocation =
            ResourceLocation(MODID, path)
    }
}

@OnlyIn(Dist.DEDICATED_SERVER)
@Mod(value = MODID)
class ExtendedAEPlusServer {
    init {
        LogUtils.getLogger()
            .warn("This is a client-side mod and it won't work on servers. Please use ExtendedAE Plus (common) instead.")
    }
}