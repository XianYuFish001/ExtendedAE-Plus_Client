package com.fish.extendedae_plus_client.config

import com.fish.extendedae_plus_client.ExtendedAEPlusClient
import com.fish.fishlib.config.HelperKeyMapping
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent
import net.neoforged.neoforge.client.settings.KeyConflictContext
import org.lwjgl.glfw.GLFW

@EventBusSubscriber(modid = ExtendedAEPlusClient.MODID)
object EAEPCKeyMapping : HelperKeyMapping(ExtendedAEPlusClient.MODID) {
    val FillToSearch by this.register(
        "fill_to_search_field",
        GLFW.GLFW_KEY_F,
        KeyConflictContext.GUI
    )

    @SubscribeEvent
    override fun init(event: RegisterKeyMappingsEvent) {
        super.init(event)
    }
}
