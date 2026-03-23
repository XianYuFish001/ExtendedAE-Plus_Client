package com.fish.extendedae_plus_client.mixin.core.ae.registryObject;

import appeng.api.crafting.PatternDetailsHelper;
import appeng.core.definitions.AEItems;
import appeng.crafting.pattern.EncodedPatternItem;
import com.fish.extendedae_plus_client.impl.ConstantCustomData;
import com.fish.extendedae_plus_client.impl.cache.CacheProvider;
import com.fish.extendedae_plus_client.util.UtilKeyBuilder;
import com.fish.fishlib.util.keyBuilder.Patterns;
import net.minecraft.client.Minecraft;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Mixin(EncodedPatternItem.class)
public class MixinSelectedPatternTooltip {
    @Inject(method = "appendHoverText", at = @At("TAIL"))
    private void onTooltip(ItemStack stack,
                           Item.TooltipContext context,
                           TooltipDisplay tooltipDisplay,
                           Consumer<Component> lines,
                           TooltipFlag flags,
                           CallbackInfo ci) {
        var hashGroup = CacheProvider.findProvider(
                PatternDetailsHelper.decodePattern(stack, Minecraft.getInstance().level));
        var record = CacheProvider.getProviderList().get(hashGroup);
        if (record != null) {
            lines.accept(UtilKeyBuilder.INSTANCE.of(Patterns.Tooltip)
                    .item(AEItems.PROCESSING_PATTERN)
                    .addStr("selected_provider")
                    .args(record.getGroup().name().getString())
                    .build());
        }

        var pattern = PatternDetailsHelper.decodePattern(stack, Minecraft.getInstance().level);
        if (pattern == null) return;
        var data = pattern.getPrimaryOutput().what().get(DataComponents.CUSTOM_DATA);
        if (data == null || !data.contains(ConstantCustomData.autoCompletable.get())) return;
        lines.accept(UtilKeyBuilder.INSTANCE.of(Patterns.Tooltip)
                .item(AEItems.PROCESSING_PATTERN)
                .addStr("auto_completable")
                .build());
    }
}
