package sh.miles.pineapple.config.superclass;

import sh.miles.pineapple.chat.PineappleChat;
import sh.miles.pineapple.chat.PineappleComponent;
import sh.miles.pineapple.config.annotation.ConfigPath;

public abstract class ConfigSuperClass {

    @ConfigPath("superclass.string")
    public String superClassString = "MyCoolSuperClass";

    @ConfigPath("superclass.component")
    public PineappleComponent superClassComponent = PineappleChat.component("<red>MyCool<green>SuperClass");
}
