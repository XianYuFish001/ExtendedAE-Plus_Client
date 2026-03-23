package com.fish.extendedae_plus_client.integration.impl.recipeViewer

import appeng.api.stacks.GenericStack

object ViewerEmpty : IRecipeViewer {
    override fun getHoveredStacks(
        mouseX: Double,
        mouseY: Double
    ) = emptyList<GenericStack>()

    override fun getHoveredStacks() = emptyList<GenericStack>()

    override fun getFavorites() = emptyList<GenericStack>()

    override fun getPulled(mouseKey: Int): Pair<Boolean, Boolean>? = null

    override fun addFavorite(stack: GenericStack) = Unit

    override fun setSearch(text: String) = Unit
}
