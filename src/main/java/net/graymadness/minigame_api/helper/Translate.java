package net.graymadness.minigame_api.helper;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.graymadness.minigame_api.MinigameAPI;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.TranslatableComponent;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.lang.reflect.Type;
import java.util.*;

public class Translate
{
    private Translate() {}

    private static final Map<String, Map<String, String>> locales = new HashMap<>();

    @Nullable
    public static String t(@NotNull String key, @NotNull String locale) {
        if(locale.isEmpty())
            return key;

        Map<String, String> translations = locales.getOrDefault(locale, null);
        if(translations == null)
            return "en_us".equals(locale) ? key : t(key, "en_us");

        String value = translations.getOrDefault(key, null);
        if(value == null)
            return "en_us".equals(locale) ? key : t(key, "en_us");

        return value;
    }

    @Nullable
    public static String t(@NotNull String key, @NotNull Player context) {
        return t(key, context.getLocale());
    }

    @NotNull
    public static BaseComponent t(@NotNull BaseComponent component, @NotNull String locale) {
        BaseComponent copy = null;
        if(component instanceof TranslatableComponent) {
            TranslatableComponent componentTranslate = (TranslatableComponent) component;
            String translatedText = t(componentTranslate.getTranslate(), locale);

            if(translatedText == null || translatedText.equals(componentTranslate.getTranslate())) {
                copy = null;
            } else {

                copy = new TextComponent(translatedText);

                copy.setBold(component.isBoldRaw());
                copy.setItalic(component.isItalicRaw());
                copy.setUnderlined(component.isUnderlinedRaw());
                copy.setStrikethrough(component.isStrikethroughRaw());
                copy.setObfuscated(component.isObfuscatedRaw());

                copy.setInsertion(component.getInsertion());

                copy.setClickEvent(component.getClickEvent());
                copy.setHoverEvent(component.getHoverEvent());

                copy.setColor(component.getColorRaw());

                List<BaseComponent> with = componentTranslate.getWith();
                if (with != null)
                {
                    processComponentWith((TextComponent) copy, with, component.getColorRaw());

                    // With
                    {
                        List<BaseComponent> extra = new ArrayList<>();

                        if (copy.getExtra() != null && copy.getExtra().size() > 0)
                            extra.addAll(copy.getExtra());

                        if (extra == null || extra.size() == 0)
                        {
                            // Clear extra
                            copy.setExtra(new ArrayList<>());
                            copy.addExtra(new TextComponent());

                        }
                        else
                        {
                            copy.setExtra(new ArrayList<>());

                            for (BaseComponent bc : extra)
                            {
                                BaseComponent tc = t(bc, locale);
                                copy.addExtra(tc);
                            }
                        }
                    }
                }
            }
        }

        if(copy == null) {
            copy = component.duplicate();

            // Clear extra
            copy.setExtra(new ArrayList<>());
            copy.addExtra(new TextComponent());
        }

        // Extras
        {
            List<BaseComponent> extra = component.getExtra();
            if(extra != null)
                for (BaseComponent bc : extra)
                    copy.addExtra(t(bc, locale));
        }

        return copy;
    }

    @NotNull
    public static BaseComponent t(@NotNull BaseComponent component, @NotNull Player context) {
        return t(component, context.getLocale());
    }

    @Nullable
    private static BaseComponent evaluateVariable(@NotNull String formatString, int[] argumentAutoIncrement, @NotNull List<BaseComponent> with){
        int formatLength = formatString.length();
        char formatType = formatString.charAt(formatLength - 1);
        int nthWith = -1;
        if(formatLength > 1){
            char[] formatChars = formatString.toCharArray();
            int nth = 0;
            for(int i=0; i<formatLength && formatChars[i] != '$'; i++){
                nth *= 10;
                nth += Character.getNumericValue(formatChars[i]);
            }
            nthWith = nth-1;
        }
        if(formatType == 's' || formatType == 'd'){
            if(nthWith >= 0){
                if(with.size() > nthWith){
                    return with.get(nthWith); //non-increment
                }else return null;
            }
            if(with.size() > argumentAutoIncrement[0]){
                return with.get(argumentAutoIncrement[0]++); //increment
            }else return null;
        }
        if(formatType == '%') return ComponentBuilder.text("%").build();
        return ComponentBuilder.text("%"+formatString).build();
    }

    private static void processComponentWith(@NotNull TextComponent component, @NotNull List<BaseComponent> with, @Nullable ChatColor color) {
        if(with.size() == 0)
            return;

        String text = component.getText();

        int[] argumentAutoIncrement = {0}; //mutable int

        int formatStartIndex = 0, formatEndIndex = 0, indexAfterLastFormat = 0;
        boolean firstReplacement = true;
        char[] textChars = text.toCharArray();
        int textLength = textChars.length;
        for(int i=0; i<textLength; i++){
            if(textChars[i] == '%'){
                formatStartIndex = i;
                for(++i; i<textLength; i++){
                    if(textChars[i] == ' ' || textChars[i] == 's' || textChars[i] == 'd' || textChars[i] == '%'){
                        formatEndIndex = i;
                        break;
                    }
                }
                if(formatEndIndex > formatStartIndex){
                    if(firstReplacement){
                        firstReplacement = false;
                        component.setText(text.substring(0, formatStartIndex));
                    }else{
                        BaseComponent textComponent = ComponentBuilder.text(text.substring(indexAfterLastFormat, formatStartIndex)).color(color).build();
                        component.addExtra(textComponent);
                    }
                    String formatString = text.substring(formatStartIndex+1, formatEndIndex+1);
                    BaseComponent evaluated = evaluateVariable(formatString, argumentAutoIncrement, with);
                    component.addExtra(Objects.requireNonNull(evaluated));
                    indexAfterLastFormat = formatEndIndex+1;
                }
            }
        }
        if(firstReplacement){
            component.setText(text);
        }else{
            component.addExtra(text.substring(indexAfterLastFormat));
        }
    }

    public static void reloadLocales() {
        locales.clear();

        File langDir = new File(MinigameAPI.getInstance().getDataFolder(), "lang/");
        if(!langDir.exists() && !langDir.mkdirs()) {
            System.out.println("Failed to create <plugin>/lang/ directory");
            return;
        }

        // Load files from /lang/ directory
        {
            File[] files = langDir.listFiles();
            if(files != null) {
                for (File child : files) {
                    String name = child.getName();
                    if(!name.endsWith(".json"))
                        continue;

                    String locale = name.substring(0, name.length() - ".json".length());

                    try(Reader reader = new FileReader(child)) {
                        loadLocale(locale, reader);
                    } catch (FileNotFoundException ex) {
                        System.out.println("Listed file not found: " + child.getPath());
                    } catch (IOException ex) {
                        System.out.println("IOException during file read: " + child.getPath());
                    }
                }
            }
        }
    }

    private static void loadLocale(@NotNull String locale, @NotNull Reader reader) {
        Gson gson = new Gson();
        Type collectionType = new TypeToken<Map<String, String>>() {}.getType();
        Map<String, String> translations = gson.fromJson(reader, collectionType);

        System.out.println("Loaded " + translations.size() + " translations for " + locale);
        locales.put(locale, translations);
    }
}
