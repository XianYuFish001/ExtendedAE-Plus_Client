package com.fish.extendedae_plus_client.integration.impl.recipeViewer

import appeng.api.stacks.GenericStack

object HelperRecipeViewer {
    // TODO Refactor
    private val activeViewer = ViewerEmpty /*by lazy {
        if (ContextModLoaded.emi.isLoaded) ViewerEmi
        else if (ContextModLoaded.jei.isLoaded) ViewerJei
        else ViewerEmpty
    }*/

    @JvmStatic
    fun hoveredStacks() = this.activeViewer.getHoveredStacks()

    @JvmStatic
    fun favorites() = this.activeViewer.getFavorites()

    /** @return Pair<Boolean: Stack, Boolean: ToInv> */
    @JvmStatic
    fun matchesKey(mouseKey: Int) = this.activeViewer.getPulled(mouseKey)

    @JvmStatic
    fun isCheatMode() = this.activeViewer.isCheatMode()

    @JvmStatic
    fun addFavorite(stack: GenericStack) = this.activeViewer.addFavorite(stack)

    @JvmStatic
    fun setSearchText(text: String) = this.activeViewer.setSearch(text)
}
