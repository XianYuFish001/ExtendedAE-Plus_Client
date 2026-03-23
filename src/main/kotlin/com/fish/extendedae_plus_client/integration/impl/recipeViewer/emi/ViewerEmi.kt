package com.fish.extendedae_plus_client.integration.impl.recipeViewer.emi

import appeng.api.stacks.GenericStack
import com.fish.extendedae_plus_client.integration.impl.recipeViewer.IRecipeViewer
import dev.emi.emi.api.EmiApi
import dev.emi.emi.api.stack.EmiStack
import dev.emi.emi.config.EmiConfig
import dev.emi.emi.runtime.EmiFavorite
import dev.emi.emi.runtime.EmiFavorites

object ViewerEmi : IRecipeViewer {
    override fun getHoveredStacks(mouseX: Double, mouseY: Double) =
        EmiApi.getHoveredStack(mouseX.toInt(), mouseY.toInt(), false)
            .stack.emiStacks
            .mapNotNull(EmiStackHelper::toGenericStack)

    override fun getHoveredStacks() =
        EmiApi.getHoveredStack(false)
            .stack.emiStacks
            .mapNotNull(EmiStackHelper::toGenericStack)

    override fun getFavorites() =
        EmiFavorites.favorites
            .flatMap(EmiFavorite::getEmiStacks)
            .mapNotNull(EmiStackHelper::toGenericStack)

    override fun getPulled(mouseKey: Int): Pair<Boolean, Boolean>? = when {
        EmiConfig.cheatOneToCursor.matchesMouse(mouseKey) -> Pair(false, false)
        EmiConfig.cheatOneToInventory.matchesMouse(mouseKey) -> Pair(false, true)
        EmiConfig.cheatStackToCursor.matchesMouse(mouseKey) -> Pair(true, false)
        EmiConfig.cheatStackToInventory.matchesMouse(mouseKey) -> Pair(true, true)
        else -> null
    }

    override fun isCheatMode() = EmiApi.isCheatMode()

    override fun addFavorite(stack: GenericStack) =
        EmiFavorites.addFavorite(EmiStackHelper.toEmiStack(stack))

    override fun setSearch(text: String) = EmiApi.setSearchText(text)
}

// TODO Refactor
private object EmiStackHelper {
    fun toGenericStack(stack: EmiStack) = null

    fun toEmiStack(stack: GenericStack) = null
}
