package sh.miles.pineapple.config.superclass;

import sh.miles.pineapple.chat.PineappleChat;
import sh.miles.pineapple.chat.PineappleComponent;
import sh.miles.pineapple.config.annotation.ConfigPath;

public class ConfigSubclassMock extends ConfigSuperClass {

    @ConfigPath("subclass.string")
    public String subClassString = "MyCoolSubClass";

    @ConfigPath("subclass.component")
    public PineappleComponent subClassComponent = PineappleChat.component("<red>MyCool<green>SubClass");
}
