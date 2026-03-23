package com.fish.extendedae_plus_client.integration.impl.recipeViewer

import appeng.api.stacks.GenericStack

interface IRecipeViewer {
    fun getHoveredStacks(mouseX: Double, mouseY: Double): List<GenericStack>

    fun getHoveredStacks(): List<GenericStack>

    fun getFavorites(): List<GenericStack>

    /**
     * @return First: Stack; Second: ToInv
     */
    fun getPulled(mouseKey: Int): Pair<Boolean, Boolean>?

    fun isCheatMode() = true

    fun addFavorite(stack: GenericStack)

    fun setSearch(text: String)
}
