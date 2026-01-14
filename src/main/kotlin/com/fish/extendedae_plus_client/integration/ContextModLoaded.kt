package com.fish.extendedae_plus_client.integration

import net.minecraftforge.fml.ModList

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

    var loaded: Boolean = false
        private set

    companion object {
        private var initialized = false

        fun init() {
            check(!initialized) { "Contexts has already been initialized" }
            initialized = true

            for (context in entries) {
                context.loaded = ModList.get().isLoaded(context.modID)
            }
        }
    }
}
