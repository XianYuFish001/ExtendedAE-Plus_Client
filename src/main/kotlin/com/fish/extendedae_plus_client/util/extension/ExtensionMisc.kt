package com.fish.extendedae_plus_client.util.extension

import org.jetbrains.annotations.Contract

private typealias PairDFU<A, B> = com.mojang.datafixers.util.Pair<A, B>

@Contract("_ -> this")
inline fun <T> Collection<T>.ifNotEmpty(action: Collection<T>.() -> Unit): Collection<T> {
    if (this.isNotEmpty()) this.action()
    return this
}

fun <A, B> PairDFU<A, B>.toKotlin(): Pair<A, B> = this.first to this.second

fun <A, B> Pair<A, B>.toDfu(): PairDFU<A, B> = PairDFU(first, second)