package com.fish.extendedae_plus_client.integration.impl.recipeViewer.jei

import com.fish.extendedae_plus_client.ExtendedAEPlusClient
import mezz.jei.api.IModPlugin
import mezz.jei.api.JeiPlugin
import mezz.jei.api.runtime.IJeiRuntime

@JeiPlugin
class EAEPCJeiPlugin : IModPlugin {
    // TODO Refactor
    override fun getPluginUid() = null

    override fun onRuntimeAvailable(jeiRuntime: IJeiRuntime) {
        ViewerJei.runtime = jeiRuntime
    }

    companion object {
        private val UID = ExtendedAEPlusClient.getLocation("jei_plugin")
    }
}
