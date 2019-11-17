package ru.dionisis.live.impementations;


import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * читает и генерирует конфигурационные файлы для World
 */
public class Config {
    public static final File CONFIG_FILE = new File("src\\main\\resources\\config.txt");

    public static void main(String[] args) {
        generateConfigFile(1000, 1000, CONFIG_FILE);
}

    public static List<String> readConfig(File configFile) {
        List<String> config = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(configFile)))) {
            while (reader.ready()) {
                config.add(reader.readLine());
            }
        } catch (FileNotFoundException e) {
            System.out.println("Нет такого файла: " + configFile);
        } catch (IOException e) {

        }
        return config;
    }

    public static void generateConfigFile(int height, int width, File configFile) {
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(configFile)))) {
            for (int i = 0; i < height; i++) {
                writer.write(generateString(width) + "\n");
            }
            writer.flush();
        } catch (FileNotFoundException e) {
            System.out.println("Нет такого файла: " + configFile);
        } catch (IOException e) {

        }
    }

    private static String generateString(int width) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < width; i++) {
            if (Math.random() > 0.5) {
                stringBuilder.append("1");
            } else {
                stringBuilder.append("0");
            }
        }
        return stringBuilder.toString();
    }
}
