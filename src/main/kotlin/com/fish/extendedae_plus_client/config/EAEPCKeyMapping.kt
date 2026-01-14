package com.fish.extendedae_plus_client.config

import com.fish.extendedae_plus_client.ExtendedAEPlusClient
import com.fish.extendedae_plus_client.util.UtilKeyBuilder
import com.mojang.blaze3d.platform.InputConstants
import net.minecraft.client.KeyMapping
import net.minecraftforge.client.event.RegisterKeyMappingsEvent
import net.minecraftforge.client.settings.IKeyConflictContext
import net.minecraftforge.client.settings.KeyConflictContext
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod
import org.lwjgl.glfw.GLFW

@Mod.EventBusSubscriber(modid = ExtendedAEPlusClient.MODID)
object EAEPCKeyMapping {
    private val mappings = HashSet<Lazy<KeyMapping>>()

    private val CATEGORY = UtilKeyBuilder.of(UtilKeyBuilder.keyCategory).buildRaw()

    @JvmField
    val fillToSearchField = this.register(
        "fill_to_search_field",
        KeyConflictContext.GUI,
        GLFW.GLFW_KEY_F
    )

    private fun register(
        name: String,
        keyConflictContext: IKeyConflictContext,
        inputType: InputConstants.Type,
        keyCode: Int,
        category: String
    ): Lazy<KeyMapping> {
        val mapping = lazy {
            KeyMapping(
                UtilKeyBuilder.of(UtilKeyBuilder.key)
                    .addStr(name)
                    .buildRaw(),
                keyConflictContext,
                inputType,
                keyCode,
                category
            )
        }
        mappings.add(mapping)
        return mapping
    }

    private fun register(
        name: String,
        keyConflictContext: IKeyConflictContext,
        keyCode: Int
    ): Lazy<KeyMapping> {
        return register(name, keyConflictContext, InputConstants.Type.KEYSYM, keyCode, CATEGORY)
    }

    @SubscribeEvent
    fun onKeyMappingReg(event: RegisterKeyMappingsEvent) {
        this.mappings.stream()
            .map(Lazy<KeyMapping>::value)
            .forEach(event::register)
    }
}
