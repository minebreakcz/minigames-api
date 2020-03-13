package net.graymadness.minigame_api.helper;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class CommandHelper
{
    private CommandHelper() {}

    @NotNull
    public static List<String> filterOnly(@Nullable String part, @NotNull List<String> list)
    {
        if(part == null)
            return list;

        List<String> rtn = new ArrayList<>();
        for(String s : list)
            if(s.startsWith(part))
                rtn.add(s);
        return rtn;
    }
}
