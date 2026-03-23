package com.fish.extendedae_plus_client.impl.event

import appeng.client.gui.implementations.QuartzKnifeScreen
import appeng.client.gui.me.common.MEStorageScreen
import appeng.core.network.serverbound.SwitchGuisPacket
import appeng.menu.me.crafting.CraftingStatusMenu
import com.fish.extendedae_plus_client.ExtendedAEPlusClient
import com.fish.extendedae_plus_client.impl.cache.CacheCrafting
import com.fish.extendedae_plus_client.impl.cache.CacheCuttingKnife
import com.fish.fishlib.network.base.PacketGeneric.Companion.sendToServer
import net.minecraft.client.Minecraft
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.client.event.ScreenEvent

@EventBusSubscriber(modid = ExtendedAEPlusClient.MODID)
object EventScreenSwitching {
    @SubscribeEvent
    private fun onGuiOpening(event: ScreenEvent.Opening) {
        when (event.newScreen) {
            is MEStorageScreen<*> -> handleMEStorageScreen(event)
            is QuartzKnifeScreen -> handleCuttingKnifeScreen(event)
        }
    }

    private fun handleMEStorageScreen(event: ScreenEvent.Opening) {
        if (event.currentScreen != null) return
        if (CacheCrafting.isEmpty) return
        CacheCrafting.isOpening = true
        SwitchGuisPacket.openSubMenu(CraftingStatusMenu.TYPE).sendToServer()
    }

    private fun handleCuttingKnifeScreen(event: ScreenEvent.Opening) {
        if (!CacheCuttingKnife.isHandlingBlockCopies) return
        CacheCuttingKnife.isHandlingBlockCopies = false
        event.isCanceled = true
        Minecraft.getInstance().player?.closeContainer()
    }
}
