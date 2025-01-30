package sh.miles.pineapple.item

import org.bukkit.inventory.ItemStack

fun buildItem(block: ItemBuilder.() -> Unit): ItemStack = ItemBuilder().apply(block).build()