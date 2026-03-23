package com.fish.extendedae_plus_client.render.widgets.button

import appeng.util.Icon
import com.fish.extendedae_plus_client.util.UtilKeyBuilder
import com.fish.fishlib.util.keyBuilder.Patterns
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent

enum class EAEPActionItems(builder: (Builder) -> Builder) {
    BackingOut({ it.icon(Icon.TOOLBAR_BUTTON_BACKGROUND).toggleName() }),

    AliasAdd({ it.icon(EAEPIcon.SaveUp).group("recipe_alias").tooltip("add") }),
    AliasRemove({ it.icon(EAEPIcon.SaveDown).group("recipe_alias").tooltip("remove") });

    val icon: IButtonIcon
    @JvmField
    val text: Component
    @JvmField
    val tooltip: Component?
    val group: String

    init {
        val info = builder(Builder())
        this.icon = info.icon
        this.group = info.actionGroup
        this.text = if (info.nameVisible) info.name else Component.empty()
        this.tooltip = if (info.tooltipVisible) info.tooltip else null
    }

    val iconBlitter get() = icon.blitter

    val aeIcon: Icon get() = icon.aeIcon

    companion object {
        @JvmField
        val actions = HashMap<String, MutableList<EAEPActionItems>>()

        init {
            for (action in entries) {
                if (!action.group.isEmpty()) actions.computeIfAbsent(
                    action.group
                ) { ArrayList() } += action
            }
        }
    }

    private class Builder {
        var icon = EAEPIcon.fromAEIcon(Icon.TOOLBAR_BUTTON_BACKGROUND)
        var actionGroup = ""
        var name: Component = Component.empty()
        var nameVisible = true
        var tooltip: MutableComponent? = null
        var tooltipVisible = true

        fun icon(icon: IButtonIcon) = also {
            this.icon = icon
        }

        fun icon(icon: Icon) = this.icon(EAEPIcon.fromAEIcon(icon))

        fun group(group: String) = also {
            this.actionGroup = group
        }

        fun name(name: Component) = also {
            this.name = name
        }

        fun name(vararg name: String) = also {
            val builder = UtilKeyBuilder.of(Patterns.ScreenTooltip)
            if (name.isNotEmpty()) name.forEach(builder::addStr)
            else builder.addStr(this.actionGroup)
            this.name(builder.build())
        }

        fun tooltip(tooltip: MutableComponent) = also {
            if (this.name.string.isEmpty()) this.name()
            this.tooltip = tooltip
        }

        fun tooltip(vararg tooltip: String) = also {
            if (this.name.string.isEmpty()) this.name()
            val builder = UtilKeyBuilder.of(Patterns.ScreenTooltip)
                .addStr(this.actionGroup)
            tooltip.forEach(builder::addStr)
            if (this.tooltip != null) this.tooltip!!.append(builder.build())
            else this.tooltip = builder.build()
        }

        fun toggleName() = also {
            this.nameVisible = !this.nameVisible
            if (!this.nameVisible) this.tooltipVisible = false
        }

        fun toggleTooltip() = also {
            this.tooltipVisible = this.nameVisible && !this.tooltipVisible
        }
    }
}
