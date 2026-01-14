package com.fish.extendedae_plus_client.mixin.core.ae.registryObject;

import appeng.api.crafting.PatternDetailsHelper;
import appeng.api.stacks.AEItemKey;
import appeng.core.definitions.AEItems;
import appeng.crafting.pattern.EncodedPatternItem;
import com.fish.extendedae_plus_client.impl.ConstantCustomData;
import com.fish.extendedae_plus_client.impl.cache.CacheProvider;
import com.fish.extendedae_plus_client.util.UtilKeyBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(EncodedPatternItem.class)
public class MixinSelectedPatternTooltip {
    @Inject(method = "appendHoverText", at = @At("TAIL"))
    private void onTooltip(ItemStack stack, Level level, List<Component> lines, TooltipFlag advancedTooltips, CallbackInfo ci) {
        var hashGroup = CacheProvider.findProvider(
                PatternDetailsHelper.decodePattern(stack, Minecraft.getInstance().level));
        var record = CacheProvider.getProviderList().get(hashGroup);
        if (record != null) {
            lines.add(UtilKeyBuilder.of(UtilKeyBuilder.tooltip)
                    .item(AEItems.PROCESSING_PATTERN)
                    .addStr("selected_provider")
                    .args(record.getGroup().name().getString())
                    .build());
        }

        var pattern = PatternDetailsHelper.decodePattern(stack, Minecraft.getInstance().level);
        if (pattern == null) return;
        var data = pattern.getPrimaryOutput().what() instanceof AEItemKey itemKey
                ? itemKey.getReadOnlyStack().getTag() : null;
        if (data == null || !data.contains(ConstantCustomData.autoCompletable.get())) return;
        lines.add(UtilKeyBuilder.of(UtilKeyBuilder.tooltip)
                .item(AEItems.PROCESSING_PATTERN)
                .addStr("auto_completable")
                .build());
    }
}
