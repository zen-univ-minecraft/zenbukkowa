package com.zenbukkowa.domain;

import com.zenbukkowa.persistence.SettingsDao;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class LocaleService {
    private final Map<String, YamlConfiguration> langs = new HashMap<>();
    private final Map<UUID, String> playerLocales = new ConcurrentHashMap<>();
    private final SettingsDao settingsDao;
    private final String defaultLang;

    public LocaleService(JavaPlugin plugin, SettingsDao settingsDao, String defaultLang) {
        this.settingsDao = settingsDao;
        this.defaultLang = defaultLang;
        loadLang(plugin, "en");
        loadLang(plugin, "ja");
        for (UUID uuid : settingsDao.loadAllLocales().keySet()) {
            playerLocales.put(uuid, settingsDao.loadAllLocales().get(uuid));
        }
    }

    private void loadLang(JavaPlugin plugin, String code) {
        InputStream stream = plugin.getResource("lang/" + code + ".yml");
        if (stream != null) {
            langs.put(code, YamlConfiguration.loadConfiguration(new InputStreamReader(stream)));
        }
    }

    public String get(UUID uuid, String key) {
        String lang = playerLocales.getOrDefault(uuid, defaultLang);
        YamlConfiguration cfg = langs.getOrDefault(lang, langs.get(defaultLang));
        if (cfg == null) return key;
        String val = cfg.getString(key);
        return val != null ? ChatColor.translateAlternateColorCodes('&', val) : key;
    }

    public List<String> getList(UUID uuid, String key) {
        String lang = playerLocales.getOrDefault(uuid, defaultLang);
        YamlConfiguration cfg = langs.getOrDefault(lang, langs.get(defaultLang));
        if (cfg == null) return List.of();
        List<String> list = cfg.getStringList(key);
        return list.stream().map(s -> ChatColor.translateAlternateColorCodes('&', s)).toList();
    }

    public void setLocale(UUID uuid, String lang) {
        playerLocales.put(uuid, lang);
        settingsDao.saveLocale(uuid, lang);
    }

    public String getLocale(UUID uuid) {
        return playerLocales.getOrDefault(uuid, defaultLang);
    }

    public String toggleLocale(UUID uuid) {
        String current = getLocale(uuid);
        String next = current.equals("ja") ? "en" : "ja";
        setLocale(uuid, next);
        return next;
    }
}
