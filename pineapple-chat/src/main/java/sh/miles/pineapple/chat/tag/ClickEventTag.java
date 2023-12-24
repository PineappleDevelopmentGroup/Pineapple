package sh.miles.pineapple.chat.tag;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.jetbrains.annotations.NotNull;
import sh.miles.pineapple.PineappleComponentBuilder;
import sh.miles.pineapple.chat.parse.ParserContext;

import java.util.Queue;

public class ClickEventTag extends AbstractTag {

    private final ClickEvent clickEvent;

    protected ClickEventTag(@NotNull final String namespace, final @NotNull Queue<String> arguments, final int childTextLength) {
        super(namespace, arguments, childTextLength);
        arguments.poll();
        ClickEvent.Action action = ClickEvent.Action.valueOf(arguments.poll().toUpperCase());
        String text = arguments.poll();
        this.clickEvent = new ClickEvent(action, text);
    }

    @Override
    public void apply(final @NotNull PineappleComponentBuilder builder, final ParserContext context) {
        builder.event(this.clickEvent);
    }
}
