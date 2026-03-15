package com.fish.extendedae_plus_client.integration.impl.bean

import com.fish.extendedae_plus_client.integration.impl.point.IntegrationPatternizer
import com.fish.fishlib.integration.BeanIntegration
import io.github.linkfgfgui.emi_patternizer.Patternize

@BeanIntegration("emi_patternizer")
object ImplPatternizer : IntegrationPatternizer {
    override val active get() = Patternize.operating
}