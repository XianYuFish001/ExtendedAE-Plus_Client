package com.fish.extendedae_plus_client.mixin.core.ae.menu;

import appeng.api.crafting.PatternDetailsHelper;
import appeng.api.storage.ITerminalHost;
import appeng.client.gui.me.items.PatternEncodingTermScreen;
import appeng.core.definitions.AEBlocks;
import appeng.menu.me.common.MEStorageMenu;
import appeng.menu.me.items.PatternEncodingTermMenu;
import appeng.menu.slot.RestrictedInputSlot;
import appeng.parts.encoding.EncodingMode;
import com.fish.extendedae_plus_client.impl.cache.CacheProvider;
import com.fish.extendedae_plus_client.integration.impl.point.IntegrationPatternizer;
import com.fish.extendedae_plus_client.mixin.impl.bridge.BridgePlanToEncode;
import com.fish.extendedae_plus_client.render.screen.ScreenProviderList;
import com.fish.extendedae_plus_client.util.UtilKeyBuilder;
import com.fish.fishlib.util.UtilJava;
import com.fish.fishlib.util.client.UtilKeyboard;
import com.fish.fishlib.util.keyBuilder.Patterns;
import com.glodblock.github.extendedae.common.EAESingletons;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(PatternEncodingTermMenu.class)
public abstract class MixinEncodingTerminal extends MEStorageMenu implements BridgePlanToEncode {
    @Shadow
    @Final
    private RestrictedInputSlot encodedPatternSlot;
    @Shadow
    public EncodingMode mode;

    @Shadow
    public abstract void encode();

    @Unique
    private boolean eaep$flagPatternSelection;
    @Unique
    private boolean eaep$encodingDelayed;

    public MixinEncodingTerminal(MenuType<?> menuType, int id, Inventory ip, ITerminalHost host) {
        super(menuType, id, ip, host);
    }

    @Inject(method = "encode", at = @At("HEAD"))
    private void onEncode(CallbackInfo ci) {
        if (this.isServerSide()) return;

        if (!UtilKeyboard.ctrl() && !IntegrationPatternizer.active()) return;
        this.eaep$flagPatternSelection = true;

        if (CacheProvider.getProviderList().isEmpty()) {
            this.getPlayer().displayClientMessage(
                    UtilKeyBuilder.INSTANCE.of(Patterns.Message)
                            .addStr("provider_list")
                            .addStr("empty_list")
                            .build(),
                    false
            );
            return;
        }

        if (!this.encodedPatternSlot.hasItem()) return;

        this.eaep$markPattern();
    }

    @Inject(method = "onSlotChange", at = @At("TAIL"))
    private void onSlotChange(Slot slot, CallbackInfo ci) {
        if (this.isServerSide()) return;

        if (!this.encodedPatternSlot.equals(slot)) {
            if (!this.eaep$encodingDelayed) return;
            this.eaep$encodingDelayed = false;

            this.encode();
            return;
        }

        if (!this.eaep$flagPatternSelection) return;
        this.eaep$flagPatternSelection = false;

        if (!this.encodedPatternSlot.hasItem()) return;

        this.eaep$markPattern();

        var player = Minecraft.getInstance().player;
        var gameMode = Minecraft.getInstance().gameMode;
        if (player == null || gameMode == null) return;
        gameMode.handleContainerInput(
                this.containerId,
                this.encodedPatternSlot.index,
                GLFW.GLFW_MOUSE_BUTTON_LEFT,
                ContainerInput.QUICK_MOVE,
                player
        );
    }

    @Unique
    private void eaep$markPattern() {
        if (CacheProvider.getProviderList().isEmpty()) return;

        var existingPattern = this.encodedPatternSlot.getItem();
        if (!PatternDetailsHelper.isEncodedPattern(existingPattern)) return;

        if (!EncodingMode.PROCESSING.equals(this.mode)) {
            for (var record : CacheProvider.getProviderList().values()) {
                var icon = record.getGroup().icon();
                if (icon == null
                        || !(icon.is(AEBlocks.MOLECULAR_ASSEMBLER)
                        || icon.is(EAESingletons.EX_ASSEMBLER)
                        || icon.is(EAESingletons.ASSEMBLER_MATRIX_PATTERN)))
                    continue;

                CacheProvider.markPattern(
                        Objects.requireNonNull(PatternDetailsHelper.decodePattern(
                                existingPattern,
                                this.getPlayer().level()
                        )),
                        record.getGroup().hashCode()
                );
            }
        } else {
            if (!(Minecraft.getInstance().screen instanceof PatternEncodingTermScreen<?> screen)) return;
            var screenProviderList = new ScreenProviderList<>(screen,
                    CacheProvider.getProviderList().values(),
                    UtilJava.consumerKotlin(hashGroup -> {
                        if (hashGroup == null) return;
                        CacheProvider.markPattern(
                                Objects.requireNonNull(PatternDetailsHelper.decodePattern(
                                        existingPattern,
                                        this.getPlayer().level()
                                )),
                                Math.toIntExact(hashGroup)
                        );
                    })
            );
            screen.switchToScreen(screenProviderList);
        }
    }

    @Override
    public void eaep$plan() {
        this.eaep$encodingDelayed = true;
    }
}
