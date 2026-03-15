package com.fish.extendedae_plus_client.integration.impl.recipeViewer

import appeng.api.stacks.GenericStack
import com.fish.extendedae_plus_client.integration.ContextModLoaded
import com.fish.extendedae_plus_client.integration.impl.recipeViewer.emi.ViewerEmi
import com.fish.extendedae_plus_client.integration.impl.recipeViewer.jei.ViewerJei

object HelperRecipeViewer {
    private val activeViewer by lazy {
        if (ContextModLoaded.emi.isLoaded) ViewerEmi
        else if (ContextModLoaded.jei.isLoaded) ViewerJei
        else ViewerEmpty
    }

    @JvmStatic
    fun hoveredStacks() = this.activeViewer.hoveredStacks

    @JvmStatic
    fun favorites() = this.activeViewer.favorites

    /** @return Pair<Boolean: Stack, Boolean: ToInv> */
    @JvmStatic
    fun matchesKey(mouseKey: Int) = this.activeViewer.matchesKey(mouseKey)

    @JvmStatic
    fun isCheatMode() = this.activeViewer.isCheatMode

    @JvmStatic
    fun addFavorite(stack: GenericStack) = this.activeViewer.addFavorite(stack)

    @JvmStatic
    fun setSearchText(text: String) = this.activeViewer.setSearch(text)
}
