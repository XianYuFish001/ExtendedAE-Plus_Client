package com.fish.extendedae_plus_client.integration.impl.point

import com.fish.fishlib.integration.BeanEmpty

interface IntegrationJech {
    fun contains(value: String, toMatch: CharSequence): Boolean

    @BeanEmpty
    object Empty : IntegrationJech {
        override fun contains(value: String, toMatch: CharSequence) = false
    }
}