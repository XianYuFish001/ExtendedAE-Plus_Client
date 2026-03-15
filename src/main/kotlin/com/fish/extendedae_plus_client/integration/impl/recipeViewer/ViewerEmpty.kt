package com.fish.extendedae_plus_client.integration.impl.recipeViewer

import appeng.api.stacks.GenericStack

object ViewerEmpty : IRecipeViewer {
    override fun getHoveredStacks(mouseX: Double, mouseY: Double): MutableList<GenericStack?> = mutableListOf()

    override val hoveredStacks: MutableList<GenericStack?> = mutableListOf()

    override val favorites: MutableList<GenericStack?> = mutableListOf()

    override fun matchesKey(mouseKey: Int): Pair<Boolean, Boolean>? = null

    override fun addFavorite(stack: GenericStack) = Unit

    override fun setSearch(text: String) = Unit
}
