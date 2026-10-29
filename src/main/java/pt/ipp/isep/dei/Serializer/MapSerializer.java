package pt.ipp.isep.dei.Serializer;

import pt.ipp.isep.dei.domain.Map;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class MapSerializer {

    public static void salvar(Map map, String path) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path))) {
            oos.writeObject(map);
        }
    }

    public static Map load(String path) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path))) {
            return (Map) ois.readObject();
        }
    }

    public static List<Map> loadAll(String dirPath) {
        List<Map> maps = new ArrayList<>();
        File dir = new File(dirPath);
        if (!dir.exists() || !dir.isDirectory()) {
            return maps;
        }
        File[] files = dir.listFiles((d, name) -> name.endsWith(".dat"));
        if (files != null) {
            for (File f : files) {
                try {
                    maps.add(load(f.getPath()));
                    System.out.println("Mapa carregado: " + f.getName());
                } catch (Exception ignored) { }
            }
        }
        return maps;
    }
}
