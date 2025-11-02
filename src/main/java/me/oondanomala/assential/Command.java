package me.oondanomala.assential;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.SyntaxErrorException;
import net.minecraft.util.BlockPos;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

//#if MC>=11202
//$$ import net.minecraft.server.MinecraftServer;
//#endif

public abstract class Command extends CommandBase {
    protected final String name;

    protected Command(String name) {
        this.name = name;
    }

    @Override
    public String getCommandName() {
        return name;
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/" + name + " help for help.";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    //#if MC==10809
    public final void processCommand(ICommandSender sender, String[] args) {
    //#else
    //$$ public final void execute(MinecraftServer server, ICommandSender sender, String[] args) {
    //#endif
        try {
            if (args.length == 0) {
                onCommand("", new String[0]);
            } else {
                onCommand(args[0].toLowerCase(Locale.ENGLISH), Arrays.copyOfRange(args, 1, args.length));
            }
        } catch (SyntaxErrorException e) {
            Assential.sendMessage("&cUsage: /" + name + " " + e.getMessage(), false);
        } catch (CommandException e) {
            Assential.sendMessage(e.getMessage(), true);
        }
    }

    @Override
    //#if MC==10809
    public final List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
    //#else
    //$$ public final List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos targetPos) {
    //#endif
        String[] lowercaseArgs = Arrays.stream(args).map(String::toLowerCase).toArray(String[]::new);
        return getListOfStringsMatchingLastWord(lowercaseArgs, addTabCompletions(Arrays.copyOfRange(lowercaseArgs, 0, args.length - 1)));
    }

    protected abstract void onCommand(String subCommand, String[] args) throws CommandException;

    protected String[] addTabCompletions(String[] args) {
        return new String[0];
    }

    /**
     * Equivalent to {@link Integer#parseInt(String)},
     * but will throw a {@link SyntaxErrorException} with
     * the provided {@code errorMessage} instead of a {@link NumberFormatException}.
     *
     * @param arg          The string to parse
     * @param errorMessage The error message for the {@link SyntaxErrorException} that will be thrown
     * @throws SyntaxErrorException If the given argument is not a valid integer.
     */
    protected static int parseInt(String arg, String errorMessage) throws SyntaxErrorException {
        try {
            return Integer.parseInt(arg);
        } catch (NumberFormatException e) {
            throw new SyntaxErrorException(errorMessage);
        }
    }

    /**
     * Equivalent to {@link Float#parseFloat(String)},
     * but will throw a {@link SyntaxErrorException} with
     * the provided {@code errorMessage} instead of a {@link NumberFormatException}.
     *
     * @param arg          The string to parse
     * @param errorMessage The error message for the {@link SyntaxErrorException} that will be thrown
     * @throws SyntaxErrorException If the given argument is not a valid float.
     */
    protected static float parseFloat(String arg, String errorMessage) throws SyntaxErrorException {
        try {
            return Float.parseFloat(arg);
        } catch (NumberFormatException e) {
            throw new SyntaxErrorException(errorMessage);
        }
    }
}
