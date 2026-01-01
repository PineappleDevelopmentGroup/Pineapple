package sh.miles.testplugin

import org.bukkit.plugin.java.JavaPlugin
import sh.miles.pineapple.PineappleLib

class TestPlugin : JavaPlugin() {

    override fun onEnable() {
        PineappleLib.initialize(this)
    }

}