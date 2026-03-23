package com.fish.extendedae_plus_client.lang

import com.fish.extendedae_plus_client.ExtendedAEPlusClient
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.data.event.GatherDataEvent

@EventBusSubscriber(modid = ExtendedAEPlusClient.MODID)
object InitializerProvider {
    @SubscribeEvent
    private fun register(event: GatherDataEvent.Client) {
        event.createProvider(::LangEN)
        event.createProvider(::LangZH)
    }
}
