package com.fish.extendedae_plus_client.integration.impl.point

import appeng.api.stacks.AEKey
import com.fish.fishlib.integration.BeanEmpty

interface IntegrationAppliedMek {
    fun isMekKey(key: AEKey): Boolean

    fun getStack(key: AEKey): Any?

    val TypeChemicalJei: () -> Class<out Any>

    @BeanEmpty
    object Empty : IntegrationAppliedMek {
        override fun isMekKey(key: AEKey) = false

        override fun getStack(key: AEKey) = null

        override val TypeChemicalJei: () -> Class<out Any>
            get() = TODO("Not yet implemented")
    }
}