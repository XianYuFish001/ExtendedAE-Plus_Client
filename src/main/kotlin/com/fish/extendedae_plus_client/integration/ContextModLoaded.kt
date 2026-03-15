package com.fish.extendedae_plus_client.integration

import com.fish.fishlib.common.InitObject
import net.neoforged.fml.ModList

@Suppress("EnumEntryName")
enum class ContextModLoaded(private val modID: String) {
    emi("emi"),
    jei("jei"),
    jech("jecharacters"),
    curios("curios"),
    ae2wtlib("ae2wtlib"),
    advancedAE("advanced_ae"),
    appliedFlux("appflux"),
    mekanism("mekanism"),
    appliedMekanistics("appmek"),
    gtceuModern("gtceu"),
    ftbLibrary("ftblibrary"),
    ;

    var isLoaded: Boolean
        private set

    init {
        this.isLoaded = false
    }

    companion object {
        @InitObject
        private fun init() {
            for (context in entries) {
                context.isLoaded = ModList.get().isLoaded(context.modID)
            }
        }
    }
}
