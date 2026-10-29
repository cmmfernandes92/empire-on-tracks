package pt.ipp.isep.dei.repository;

import pt.ipp.isep.dei.domain.Size;

import java.util.*;

public class SizeRepository {

    private final Map<Integer, Size> sizeMap = new LinkedHashMap<>();
    private int nextId = 1;

    public SizeRepository() {
        addPredefinedSizes();
    }

    private void addPredefinedSizes() {
        addSize(20, 15); // ID = 1
        addSize(30, 20); // ID = 2
        addSize(40, 30); // ID = 3
    }

    private void addSize(int width, int height) {
        sizeMap.put(nextId++, new Size(width, height));
    }

    public List<Size> getAvailableSizes() {
        return new ArrayList<>(sizeMap.values());
    }

    public Size findSizeByID(int id) {
        return sizeMap.get(id);
    }

    public Map<Integer, Size> getSizeMap() {
        return Collections.unmodifiableMap(sizeMap);
    }

    public int getSizeID(Size target) {
        for (Map.Entry<Integer, Size> entry : sizeMap.entrySet()) {
            if (entry.getValue().equals(target)) {
                return entry.getKey();
            }
        }
        return -1;
    }
}
