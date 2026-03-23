package com.fish.extendedae_plus_client.render.widgets.button

open class EAEPActionButton(override val action: EAEPActionItems, onPress: (EAEPActionItems?) -> Unit) : EAEPButton(
    { onPress(it.action) }
) {
    init {
        this.updateTooltip()
    }
}
