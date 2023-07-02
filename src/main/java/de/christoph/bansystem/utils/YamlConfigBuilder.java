package de.christoph.bansystem.utils;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class YamlConfigBuilder {
    private final String filePath;
    private final Yaml yaml;
    private Map<String, Object> data;

    public YamlConfigBuilder(String filePath) {
        this.filePath = filePath;
        this.yaml = createYamlInstance();
        this.data = loadDataFromFile();

        createFolderIfNotExists();
    }

    private Yaml createYamlInstance() {
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        options.setPrettyFlow(true); // Schön formatierte Ausgabe
        return new Yaml(options);
    }

    private Map<String, Object> loadDataFromFile() {
        try {
            if (Files.exists(Paths.get(filePath))) {
                try (InputStream inputStream = new FileInputStream(filePath)) {
                    return yaml.load(inputStream);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new HashMap<>();
    }

    private void createFolderIfNotExists() {
        File folder = new File(Paths.get(filePath).getParent().toString());
        if (!folder.exists()) {
            folder.mkdirs();
        }
    }

    public Object get(String key) {
        return data.get(key);
    }

    public void set(String key, Object value) {
        data.put(key, value);
        saveDataToFile();
    }

    public boolean contains(String key) {
        return data.containsKey(key);
    }

    private void saveDataToFile() {
        try (Writer writer = new FileWriter(filePath)) {
            // Sortierung der Schlüssel
            Map<String, Object> sortedData = new LinkedHashMap<>();
            data.entrySet()
                    .stream()
                    .sorted(Map.Entry.comparingByKey(Comparator.naturalOrder()))
                    .forEach(entry -> sortedData.put(entry.getKey(), entry.getValue()));

            yaml.dump(sortedData, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
