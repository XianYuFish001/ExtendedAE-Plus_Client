package com.fish.extendedae_plus_client.util.extension

import net.minecraft.world.item.ItemStack
import org.jetbrains.annotations.Contract

// 有牛啊😭😭😭
private typealias Predicate<T> = (T.() -> Boolean)
private typealias Supplier<T> = () -> T

@Contract("true -> this; false -> param2()")
inline fun <T> T.orElseGet(predicate: Predicate<T> = { this == null }, default: Supplier<T>): T =
    if (this.predicate()) this else default()

inline fun <T> T.orElse(default: T, predicate: Predicate<T> = { this == null }): T =
    this.orElseGet(predicate) { default }

inline fun ItemStack.orElseGet(default: () -> ItemStack): ItemStack =
    this.orElseGet({ this.isEmpty }, default)

fun ItemStack.orElse(default: ItemStack): ItemStack =
    this.orElseGet { default }