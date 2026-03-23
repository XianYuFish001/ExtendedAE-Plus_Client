package com.fish.extendedae_plus_client.mixin.core.ae.screen;

import appeng.client.gui.me.common.MEStorageScreen;
import appeng.client.gui.me.items.PatternEncodingTermScreen;
import appeng.client.gui.style.ScreenStyle;
import appeng.core.network.serverbound.InventoryActionPacket;
import appeng.helpers.InventoryAction;
import appeng.menu.me.items.PatternEncodingTermMenu;
import com.fish.extendedae_plus_client.render.screen.ScreenStacksReproperties;
import com.fish.fishlib.network.base.PacketGeneric;
import com.fish.fishlib.util.UtilJava;
import com.fish.fishlib.util.client.UtilKeyboard;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PatternEncodingTermScreen.class)
public class MixinEncodingTerminalReproperties<TMenu extends PatternEncodingTermMenu>
        extends MEStorageScreen<TMenu> {
    public MixinEncodingTerminalReproperties(TMenu menu, Inventory playerInventory, Component title, ScreenStyle style) {
        super(menu, playerInventory, title, style);
    }

    @Inject(method = "mouseClicked", at = @At("HEAD"), cancellable = true)
    private void onMouseClick(MouseButtonEvent event, boolean doubleClick, CallbackInfoReturnable<Boolean> cir) {
        if (this.minecraft == null) return;

        if (!this.menu.canModifyAmountForSlot(this.hoveredSlot)) return;

        if (!this.minecraft.options.keyPickItem.matchesMouse(event)) return;
        if (!UtilKeyboard.ctrl()) return;

        var stack = this.hoveredSlot.getItem();
        var screen = new ScreenStacksReproperties<>(
                this,
                stack,
                UtilJava.consumerKotlin(newStack -> {
                    var packetUpdateStack = new InventoryActionPacket(
                            InventoryAction.SET_FILTER, this.hoveredSlot.index, newStack);
                    PacketGeneric.Companion.sendToServer(packetUpdateStack);
                }),
                this.hoveredSlot == this.menu.getProcessingOutputSlots()[0]
        );
        this.switchToScreen(screen);
        cir.setReturnValue(true);
    }
}
