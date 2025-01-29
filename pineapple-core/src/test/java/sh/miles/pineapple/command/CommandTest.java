package sh.miles.pineapple.command;

import be.seeseemelk.mockbukkit.entity.PlayerMock;
import org.junit.jupiter.api.Test;
import sh.miles.pineapple.BukkitTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CommandTest extends BukkitTest {

    @Test
    void test_Should_Correct_Completion_Args() {
        final PlayerMock player = server.addPlayer();
        player.setOp(true);

        final Command command = simpleCommand();
        final Command subcommand = new Command(new CommandLabel("subcommand0", "permission"));
        subcommand.registerSubcommand(new Command(new CommandLabel("subsubcommand0", "permission")));
        command.registerSubcommand(subcommand);
        command.registerSubcommand(new Command(new CommandLabel("subcommand1", "permission")));

        assertEquals(List.of("subcommand0", "subcommand1"), command.complete(player, new String[]{""}));
        assertEquals(List.of("subsubcommand0"), command.complete(player, new String[]{"subcommand0", ""}));
    }

    @Test
    void test_Should_Execute_If_Permission() {
        final PlayerMock playerMock = server.addPlayer();
        playerMock.addAttachment(plugin, "command.permission", true);

        final Command command = simpleCommand();
        assertTrue(command.execute(playerMock, new String[]{}));
        assertEquals("Command Executed", playerMock.nextMessage());
    }

    @Test
    void test_Should_Send_Error_Message_If_Not_Permission() {
        final PlayerMock playerMock = server.addPlayer();
        playerMock.addAttachment(plugin, "command.permission", false);
        final Command command = simpleCommand();

        assertTrue(command.execute(playerMock, new String[]{}));
        assertNotEquals("Command Executor", playerMock.nextMessage());
    }

    private Command simpleCommand() {
        Command command = new Command(new CommandLabel("command", "command.permission"));
        command.noArgExecutor = (sender, args) -> {
            sender.sendMessage("Command Executed");
            return true;
        };
        return command;
    }
}
