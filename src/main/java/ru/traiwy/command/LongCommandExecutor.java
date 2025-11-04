package ru.traiwy.command;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.command.TabExecutor;
import org.bukkit.permissions.Permission;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public abstract class LongCommandExecutor implements TabExecutor {
    private final List<SubCommandWrapper> subCommands = new ArrayList<>();

    protected void addSubCommand(SubCommand subCommand, String[] aliases, Permission permission){
        this.subCommands.add(new SubCommandWrapper(subCommand, aliases, permission));
    }

    @Nullable
    protected SubCommandWrapper getWrapperFromLabel(String label){
        for(SubCommandWrapper wrapper : subCommands){
            for(String alias : wrapper.aliases){
                if(alias.equalsIgnoreCase(label)){
                    return wrapper;
                }
            }
        }
        return null;
    }

    protected List<String> getFirstAliases() {
        final List<String> result = new ArrayList<>();
        for (final SubCommandWrapper wrapper : subCommands) {
            final String alias = wrapper.aliases[0];
            result.add(alias);
        }
        return result;
    }

    @AllArgsConstructor
    @Getter
    public class SubCommandWrapper{
        private final SubCommand subCommand;
        private final String[] aliases;
        private final Permission permission;
    }



}
