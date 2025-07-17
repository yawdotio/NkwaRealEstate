package utilities;

/**
 * Custom implementation of Map data structure.
 * This is a key-value store.
 *
 * @param <K> the type of keys maintained by this map
 * @param <V> the type of mapped values
 */
public class Map<K, V> {
    private HashMap<K, V> hashMap;

    public Map() {
        hashMap = new HashMap<>();
    }

    public void put(K key, V value) {
        hashMap.put(key, value);
    }

    public V get(K key) {
        return hashMap.get(key);
    }

    public boolean containsKey(K key) {
        return hashMap.containsKey(key);
    }

    public boolean remove(K key) {
        return hashMap.remove(key) != null;
    }

    public int size() {
        return hashMap.size();
    }

    public boolean isEmpty() {
        return hashMap.size() == 0;
    }
}
