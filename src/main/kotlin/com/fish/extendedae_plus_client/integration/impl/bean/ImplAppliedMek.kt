package com.fish.extendedae_plus_client.integration.impl.bean

import appeng.api.stacks.AEKey
import com.fish.extendedae_plus_client.integration.impl.point.IntegrationAppliedMek
import com.fish.fishlib.integration.BeanIntegration
import me.ramidzkh.mekae2.ae2.MekanismKey
import mekanism.api.chemical.ChemicalStack

@BeanIntegration("appmek")
object ImplAppliedMek : IntegrationAppliedMek {
    override fun isMekKey(key: AEKey) = key is MekanismKey

    override fun getStack(key: AEKey) = (key as? MekanismKey)?.stack

    override val TypeChemicalJei = { ChemicalStack::class.java }
}