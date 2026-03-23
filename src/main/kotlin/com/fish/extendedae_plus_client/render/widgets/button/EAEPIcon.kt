package com.fish.extendedae_plus_client.render.widgets.button

import appeng.client.gui.style.Blitter
import appeng.util.Icon
import com.fish.extendedae_plus_client.ExtendedAEPlusClient

enum class EAEPIcon(
    val x: Int,
    val y: Int,
    val width: Int = 16,
    val height: Int = 16
) : IButtonIcon {
    SaveCenter(0, 0),
    SaveUp(16, 0),
    SaveDown(32, 0),

    ;

    override val blitter: Blitter
        get() = Blitter.texture(
            TEXTURE,
            TEXTURE_WIDTH,
            TEXTURE_HEIGHT
        ).src(x, y, width, height)

    override val aeIcon: Icon
        get() = Icon.INVALID

    @JvmRecord
    private data class AEIcon(override val aeIcon: Icon) : IButtonIcon {
        override val blitter: Blitter get() = Blitter.icon(this.aeIcon)
    }

    companion object {
        val TEXTURE = ExtendedAEPlusClient.getLocation("textures/gui/icons.png")
        const val TEXTURE_WIDTH = 64
        const val TEXTURE_HEIGHT = 64

        @JvmStatic
        fun fromAEIcon(aeIcon: Icon): IButtonIcon = AEIcon(aeIcon)
    }
}
