package com.fish.extendedae_plus_client.integration.impl.bean

import com.fish.extendedae_plus_client.integration.impl.point.IntegrationJech
import com.fish.fishlib.integration.BeanIntegration
import me.towdium.jecharacters.utils.Match

@BeanIntegration("jecharacters")
object ImplJech : IntegrationJech {
    override fun contains(value: String, toMatch: CharSequence) = Match.contains(value, toMatch)
}