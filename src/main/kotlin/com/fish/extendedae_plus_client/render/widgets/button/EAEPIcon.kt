package com.fish.extendedae_plus_client.render.widgets.button

import appeng.client.gui.Icon
import appeng.client.gui.style.Blitter
import com.fish.extendedae_plus_client.ExtendedAEPlusClient
import net.minecraft.resources.ResourceLocation

enum class EAEPIcon(
    val x: Int, val y: Int, val width: Int = 16, val height: Int = 16,
    val texture: ResourceLocation = ExtendedAEPlusClient.getLocation("textures/gui/icons.png")
) : IButtonIcon {
    TOOLBAR_BUTTON_BACKGROUND(0, 0, 18, 20,
        ExtendedAEPlusClient.getLocation("textures/gui/ore_button.png")),
    TOOLBAR_BUTTON_BACKGROUND_HOVER(18, 0, 18, 20,
        ExtendedAEPlusClient.getLocation("textures/gui/ore_button.png")),
    TOOLBAR_BUTTON_BACKGROUND_FOCUS(36, 0, 18, 19,
        ExtendedAEPlusClient.getLocation("textures/gui/ore_button.png")),

    SAVE_CENTER(0, 0),
    SAVE_UP(16, 0),
    SAVE_DOWN(32, 0),

    ;

    override val blitter: Blitter
        get() = Blitter.texture(texture, TEXTURE_WIDTH, TEXTURE_HEIGHT)
            .src(x, y, width, height)

    override val aeIcon: Icon
        get() = Icon.INVALID

    @JvmRecord
    private data class AEIcon(override val aeIcon: Icon) : IButtonIcon {
        override val blitter: Blitter
            get() = aeIcon.blitter
    }

    companion object {
        const val TEXTURE_WIDTH: Int = 64
        const val TEXTURE_HEIGHT: Int = 64

        @JvmStatic
        fun fromAEIcon(aeIcon: Icon): IButtonIcon {
            return AEIcon(aeIcon)
        }
    }
}
