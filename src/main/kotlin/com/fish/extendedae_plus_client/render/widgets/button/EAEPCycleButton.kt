package com.fish.extendedae_plus_client.render.widgets.button

import appeng.client.gui.AEBaseScreen
import com.fish.fishlib.network.base.PacketGeneric.Companion.sendToServer
import net.minecraft.client.Minecraft
import net.minecraft.network.protocol.common.custom.CustomPacketPayload

open class EAEPCycleButton(
    protected val states: List<EAEPActionItems>,
    statedTask: (Int, EAEPActionItems) -> Unit,
    protected val iteratorState: IteratorState
) : EAEPButton({
    val screen = Minecraft.getInstance().screen
    if (it is EAEPCycleButton && screen is AEBaseScreen<*>)
        statedTask(it.iterateState(screen.isHandlingRightClick), it.action)
}) {
    protected var stateIndex: Int = 0

    override val action get() = this.states[this.stateIndex]

    fun setStateIndex(stateIndex: Int, triggerEvent: Boolean) {
        this.stateIndex = stateIndex
        if (triggerEvent) this.onPress.onPress(this)
        else this.updateTooltip()
    }

    /**
     * @return 被迭代过的 stateIndex
     */
    fun iterateState(reversed: Boolean): Int {
        this.iteratorState.let { this.stateIndex = it.iterate(this.stateIndex, reversed) }
        return this.stateIndex
    }

    class Builder {
        private val states: MutableList<EAEPActionItems> = ArrayList()
        private val tasks: MutableList<(EAEPActionItems) -> Unit> = ArrayList()
        private var task: ((EAEPActionItems) -> Unit)? = null
        private var iteratorState: IteratorState? = null

        fun addPart(action: EAEPActionItems, packet: CustomPacketPayload) =
            this.addPart(action) { _ -> packet.sendToServer() }

        fun addPart(action: EAEPActionItems, onPress: () -> Unit) =
            this.addPart(action) { _ -> onPress() }

        fun addPart(action: EAEPActionItems, onPress: (EAEPActionItems) -> Unit = {  }): Builder {
            this.states.add(action)
            this.tasks.add(onPress)
            return this
        }

        fun addGlobalTask(task: (EAEPActionItems) -> Unit): Builder {
            this.task = task
            return this
        }

        fun setIterator(iteratorState: IteratorState): Builder {
            this.iteratorState = iteratorState
            return this
        }

        fun build(): EAEPCycleButton {
            return EAEPCycleButton(
                this.states,
                { index: Int, action: EAEPActionItems ->
                    this.task?.invoke(action)
                    this.tasks[index](action)
                },
                this.iteratorState ?: IteratorState { prev, reversed ->
                    (prev + if (reversed) -1 else 1 + this.states.size) % this.states.size
                }
            )
        }
    }

    fun interface IteratorState {
        fun iterate(prev: Int, reversed: Boolean): Int
    }
}
