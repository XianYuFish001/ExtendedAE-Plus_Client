package com.fish.extendedae_plus_client.impl.event

import appeng.client.gui.implementations.QuartzKnifeScreen
import appeng.client.gui.me.common.MEStorageScreen
import appeng.core.sync.network.NetworkHandler
import appeng.core.sync.packets.SwitchGuisPacket
import appeng.menu.me.crafting.CraftingStatusMenu
import com.fish.extendedae_plus_client.ExtendedAEPlusClient
import com.fish.extendedae_plus_client.impl.cache.CacheCrafting
import com.fish.extendedae_plus_client.impl.cache.CacheCuttingKnife
import net.minecraft.client.Minecraft
import net.minecraft.network.protocol.game.ServerboundContainerClosePacket
import net.minecraftforge.client.event.ScreenEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod

@Mod.EventBusSubscriber(modid = ExtendedAEPlusClient.MODID)
object EventScreenSwitching {
    @SubscribeEvent
    fun onGuiOpening(event: ScreenEvent.Opening) {
        val newScreen = event.newScreen;
        if (newScreen is MEStorageScreen<*>)
            handleMEStorageScreen(event)
        else if (newScreen is QuartzKnifeScreen)
            handleCuttingKnifeScreen(newScreen, event)
    }

    private fun handleMEStorageScreen(event: ScreenEvent.Opening) {
        if (event.currentScreen != null) return
        if (CacheCrafting.isEmpty) return
        CacheCrafting.isOpening = true
        NetworkHandler.instance().sendToServer(SwitchGuisPacket.openSubMenu(CraftingStatusMenu.TYPE))
    }

    private fun handleCuttingKnifeScreen(screen: QuartzKnifeScreen, event: ScreenEvent.Opening) {
        if (!CacheCuttingKnife.isHandlingBlockCopies) return
        event.setCanceled(true)

        val connection = Minecraft.getInstance().connection ?: return
        connection.send(ServerboundContainerClosePacket(screen.getMenu().containerId))
    }
}
