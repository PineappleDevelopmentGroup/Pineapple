package sh.miles.pineapple.item

import org.bukkit.inventory.ItemStack

fun buildItem(block : ItemBuilder.() -> Unit): ItemStack {
    return ItemBuilder().apply(block).build()
}