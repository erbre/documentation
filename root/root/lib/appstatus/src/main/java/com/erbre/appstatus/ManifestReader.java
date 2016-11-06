package com.erbre.appstatus;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class ManifestReader {

    Map<String, String> map = new HashMap<>();

    public Map<String, String> getMap() {
        return map;
    }

    public void load(InputStream is) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            StringBuilder currentLine = new StringBuilder(reader.readLine());
            if (currentLine != null) {
                String newLine;
                while ((newLine = reader.readLine()) != null) {
                    if (newLine.startsWith(" ")) {
                        currentLine.append(newLine.substring(1));
                    } else {
                        loadEntry(currentLine.toString());
                        currentLine = new StringBuilder(newLine);
                    }
                }
                loadEntry(currentLine.toString());

            }
        }
    }

    private void loadEntry(String currentLine) {
        String line = currentLine.trim();
        if (!line.isEmpty()) {
            int sep = line.indexOf(':');
            if (sep == -1) {
                map.put(line, "");
            } else {
                String key = line.substring(0, sep).trim();
                String value = line.substring(sep + 1, line.length()).trim();
                map.put(key, value);
            }
        }
    }
}
