package com.fish.extendedae_plus_client.integration.impl.point

import com.fish.extendedae_plus_client.integration.ManagerIntegration

interface IntegrationPatternizer {
    val active: Boolean

    companion object {
        @JvmStatic
        fun active() = ManagerIntegration<IntegrationPatternizer>()?.active ?: false
    }
}