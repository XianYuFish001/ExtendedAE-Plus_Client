package com.fish.extendedae_plus_client.render.widgets.button

import appeng.client.gui.style.Blitter
import appeng.client.gui.widgets.IconButton
import appeng.util.Icon
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.input.InputWithModifiers
import net.minecraft.network.chat.Component
import net.minecraft.world.item.ItemStack
import org.jetbrains.annotations.MustBeInvokedByOverriders
import java.util.regex.Pattern
import kotlin.math.max

abstract class EAEPButton(onPress: (EAEPButton) -> Unit) : IconButton({
    if (it is EAEPButton) onPress(it)
}) {
    override fun onPress(input: InputWithModifiers) {
        super.onPress(input)
        this.updateTooltip()
    }

    @MustBeInvokedByOverriders
    protected fun updateTooltip() {
        if (this.nonnullAction.text.string.isNotEmpty()) this.message = this.buildMessage(
            this.nonnullAction.text,
            this.nonnullAction.tooltip
        )
    }

    abstract val action: EAEPActionItems?

    private val nonnullAction get() = this.action ?: EAEPActionItems.BackingOut

    override fun getIcon() = this.nonnullAction.aeIcon

    protected val iconBlitter get() = this.nonnullAction.iconBlitter

    protected fun buildMessage(i18nName: Component, i18nTooltip: Component?) = if (i18nTooltip == null) {
        Component.literal(i18nName.string)
    } else {
        var value = i18nTooltip.string
        value = PATTERN_NEW_LINE.matcher(value).replaceAll("\n")
        val sb = StringBuilder(value)
        var i = max(sb.lastIndexOf("\n"), 0)

        while (i + 30 < sb.length && (sb.lastIndexOf(" ", i + 30).also { i = it }) != -1) {
            sb.replace(i, i + 1, "\n")
        }

        Component.literal(i18nName.string + "\n" + sb)
    }

    override fun renderContents(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, partial: Float) {
        if (this.visible) {
            val blitter = this.iconBlitter
            val item = this.itemOverlay

            if (this.isHalfSize) {
                this.width = 8
                this.height = 8
            }

            val yOffset = if (isHovered()) 1 else 0

            if (this.isHalfSize) {
                if (!isDisableBackground) {
                    Blitter.icon(Icon.TOOLBAR_BUTTON_BACKGROUND).dest(x, y).blit(guiGraphics)
                }
                if (item != null) {
                    guiGraphics.renderItem(ItemStack(item), x, y)
                } else {
                    if (!this.active) blitter.opacity(0.5f)
                    blitter.dest(x, y).blit(guiGraphics)
                }
            } else {
                if (!isDisableBackground) {
                    val bgIcon = if (isHovered())
                        Icon.TOOLBAR_BUTTON_BACKGROUND_HOVER
                    else
                        if (isFocused) Icon.TOOLBAR_BUTTON_BACKGROUND_FOCUS else Icon.TOOLBAR_BUTTON_BACKGROUND

                    Blitter.icon(bgIcon)
                        .dest(x - 1, y + yOffset, 18, 20)
                        .blit(guiGraphics)
                }
                if (item != null) guiGraphics.renderItem(ItemStack(item), x, y + 1 + yOffset)
                else blitter.dest(x, y + 1 + yOffset).blit(guiGraphics)
            }
        }
    }

    companion object {
        protected val PATTERN_NEW_LINE = "\\n".toPattern(Pattern.LITERAL)
    }
}
