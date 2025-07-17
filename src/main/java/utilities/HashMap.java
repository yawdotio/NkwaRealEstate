package utilities;

import java.util.*;

/**
 * Custom implementation of HashMap data structure.
 * This implementation uses separate chaining with linked lists for collision resolution.
 * 
 * @param <K> the type of keys maintained by this map
 * @param <V> the type of mapped values
 */
public class HashMap<K, V> implements Map<K, V> {
    
    // Default initial capacity - MUST be a power of two
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    
    // Maximum capacity, used if a higher value is implicitly specified
    private static final int MAXIMUM_CAPACITY = 1 << 30;
    
    // Default load factor
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    
    // The table, resized as necessary. Length MUST Always be a power of two
    private Entry<K, V>[] table;
    
    // The number of key-value mappings contained in this map
    private int size;
    
    // The next size value at which to resize (capacity * load factor)
    private int threshold;
    
    // The load factor for the hash table
    private final float loadFactor;
    
    /**
     * Constructs an empty HashMap with the specified initial capacity and load factor.
     * 
     * @param initialCapacity the initial capacity
     * @param loadFactor the load factor
     */
    @SuppressWarnings("unchecked")
    public HashMap(int initialCapacity, float loadFactor) {
        if (initialCapacity < 0) {
            throw new IllegalArgumentException("Illegal initial capacity: " + initialCapacity);
        }
        if (initialCapacity > MAXIMUM_CAPACITY) {
            initialCapacity = MAXIMUM_CAPACITY;
        }
        if (loadFactor <= 0 || Float.isNaN(loadFactor)) {
            throw new IllegalArgumentException("Illegal load factor: " + loadFactor);
        }
        
        // Find a power of 2 >= initialCapacity
        int capacity = 1;
        while (capacity < initialCapacity) {
            capacity <<= 1;
        }
        
        this.loadFactor = loadFactor;
        this.threshold = (int) (capacity * loadFactor);
        this.table = new Entry[capacity];
        this.size = 0;
    }
    
    /**
     * Constructs an empty HashMap with the specified initial capacity and default load factor.
     * 
     * @param initialCapacity the initial capacity
     */
    public HashMap(int initialCapacity) {
        this(initialCapacity, DEFAULT_LOAD_FACTOR);
    }
    
    /**
     * Constructs an empty HashMap with the default initial capacity and load factor.
     */
    public HashMap() {
        this(DEFAULT_INITIAL_CAPACITY, DEFAULT_LOAD_FACTOR);
    }
    
    /**
     * Constructs a new HashMap with the same mappings as the specified Map.
     * 
     * @param m the map whose mappings are to be placed in this map
     */
    public HashMap(Map<? extends K, ? extends V> m) {
        this(Math.max((int) (m.size() / DEFAULT_LOAD_FACTOR) + 1, DEFAULT_INITIAL_CAPACITY), DEFAULT_LOAD_FACTOR);
        putAll(m);
    }
    
    /**
     * Applies a supplemental hash function to a given hashCode, which defends against
     * poor quality hash functions.
     * 
     * @param key the key
     * @return the supplemental hash
     */
    private int hash(Object key) {
        if (key == null) {
            return 0;
        }
        int h = key.hashCode();
        h ^= (h >>> 20) ^ (h >>> 12);
        return h ^ (h >>> 7) ^ (h >>> 4);
    }
    
    /**
     * Returns index for hash code h.
     * 
     * @param h the hash code
     * @param length the length of the table
     * @return the index
     */
    private int indexFor(int h, int length) {
        return h & (length - 1);
    }
    
    /**
     * Returns the number of key-value mappings in this map.
     * 
     * @return the number of key-value mappings in this map
     */
    @Override
    public int size() {
        return size;
    }
    
    /**
     * Returns true if this map contains no key-value mappings.
     * 
     * @return true if this map contains no key-value mappings
     */
    @Override
    public boolean isEmpty() {
        return size == 0;
    }
    
    /**
     * Returns the value to which the specified key is mapped, or null if this map
     * contains no mapping for the key.
     * 
     * @param key the key whose associated value is to be returned
     * @return the value to which the specified key is mapped, or null
     */
    @Override
    public V get(Object key) {
        if (key == null) {
            return getForNullKey();
        }
        
        int hash = hash(key);
        int index = indexFor(hash, table.length);
        
        for (Entry<K, V> e = table[index]; e != null; e = e.next) {
            if (e.hash == hash && (e.key == key || key.equals(e.key))) {
                return e.value;
            }
        }
        return null;
    }
    
    /**
     * Returns the value associated with null key, or null if no such mapping exists.
     * 
     * @return the value associated with null key
     */
    private V getForNullKey() {
        for (Entry<K, V> e = table[0]; e != null; e = e.next) {
            if (e.key == null) {
                return e.value;
            }
        }
        return null;
    }
    
    /**
     * Returns true if this map contains a mapping for the specified key.
     * 
     * @param key the key whose presence in this map is to be tested
     * @return true if this map contains a mapping for the specified key
     */
    @Override
    public boolean containsKey(Object key) {
        return get(key) != null || (key == null && getForNullKey() != null);
    }
    
    /**
     * Associates the specified value with the specified key in this map.
     * 
     * @param key key with which the specified value is to be associated
     * @param value value to be associated with the specified key
     * @return the previous value associated with key, or null if there was no mapping for key
     */
    @Override
    public V put(K key, V value) {
        if (key == null) {
            return putForNullKey(value);
        }
        
        int hash = hash(key);
        int index = indexFor(hash, table.length);
        
        // Check if key already exists
        for (Entry<K, V> e = table[index]; e != null; e = e.next) {
            if (e.hash == hash && (e.key == key || key.equals(e.key))) {
                V oldValue = e.value;
                e.value = value;
                return oldValue;
            }
        }
        
        // Add new entry
        addEntry(hash, key, value, index);
        return null;
    }
    
    /**
     * Handles put operation for null key.
     * 
     * @param value the value to be associated with null key
     * @return the previous value associated with null key
     */
    private V putForNullKey(V value) {
        for (Entry<K, V> e = table[0]; e != null; e = e.next) {
            if (e.key == null) {
                V oldValue = e.value;
                e.value = value;
                return oldValue;
            }
        }
        addEntry(0, null, value, 0);
        return null;
    }
    
    /**
     * Adds a new entry with the specified key, value and hash code to the specified bucket.
     * 
     * @param hash the hash code of the key
     * @param key the key
     * @param value the value
     * @param bucketIndex the index of the bucket
     */
    private void addEntry(int hash, K key, V value, int bucketIndex) {
        Entry<K, V> e = table[bucketIndex];
        table[bucketIndex] = new Entry<>(hash, key, value, e);
        
        if (size++ >= threshold) {
            resize(2 * table.length);
        }
    }
    
    /**
     * Rehashes the contents of this map into a new array with a larger capacity.
     * 
     * @param newCapacity the new capacity
     */
    @SuppressWarnings("unchecked")
    private void resize(int newCapacity) {
        Entry<K, V>[] oldTable = table;
        int oldCapacity = oldTable.length;
        
        if (oldCapacity == MAXIMUM_CAPACITY) {
            threshold = Integer.MAX_VALUE;
            return;
        }
        
        Entry<K, V>[] newTable = new Entry[newCapacity];
        transfer(newTable);
        table = newTable;
        threshold = (int) (newCapacity * loadFactor);
    }
    
    /**
     * Transfers all entries from current table to newTable.
     * 
     * @param newTable the new table
     */
    private void transfer(Entry<K, V>[] newTable) {
        Entry<K, V>[] src = table;
        int newCapacity = newTable.length;
        
        for (int j = 0; j < src.length; j++) {
            Entry<K, V> e = src[j];
            if (e != null) {
                src[j] = null;
                do {
                    Entry<K, V> next = e.next;
                    int i = indexFor(e.hash, newCapacity);
                    e.next = newTable[i];
                    newTable[i] = e;
                    e = next;
                } while (e != null);
            }
        }
    }
    
    /**
     * Removes the mapping for the specified key from this map if present.
     * 
     * @param key key whose mapping is to be removed from the map
     * @return the previous value associated with key, or null if there was no mapping for key
     */
    @Override
    public V remove(Object key) {
        Entry<K, V> e = removeEntryForKey(key);
        return (e == null ? null : e.value);
    }
    
    /**
     * Removes and returns the entry associated with the specified key in this map.
     * 
     * @param key the key
     * @return the entry associated with the specified key, or null if no such entry exists
     */
    private Entry<K, V> removeEntryForKey(Object key) {
        int hash = (key == null) ? 0 : hash(key);
        int index = indexFor(hash, table.length);
        Entry<K, V> prev = table[index];
        Entry<K, V> e = prev;
        
        while (e != null) {
            Entry<K, V> next = e.next;
            if (e.hash == hash && (e.key == key || (key != null && key.equals(e.key)))) {
                size--;
                if (prev == e) {
                    table[index] = next;
                } else {
                    prev.next = next;
                }
                return e;
            }
            prev = e;
            e = next;
        }
        return null;
    }
    
    /**
     * Copies all of the mappings from the specified map to this map.
     * 
     * @param m mappings to be stored in this map
     */
    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        int numKeysToBeAdded = m.size();
        if (numKeysToBeAdded == 0) {
            return;
        }
        
        // Calculate new capacity if needed
        if (numKeysToBeAdded > threshold) {
            int targetCapacity = (int) (numKeysToBeAdded / loadFactor + 1);
            if (targetCapacity > MAXIMUM_CAPACITY) {
                targetCapacity = MAXIMUM_CAPACITY;
            }
            
            int newCapacity = table.length;
            while (newCapacity < targetCapacity) {
                newCapacity <<= 1;
            }
            
            if (newCapacity > table.length) {
                resize(newCapacity);
            }
        }
        
        for (Map.Entry<? extends K, ? extends V> e : m.entrySet()) {
            put(e.getKey(), e.getValue());
        }
    }
    
    /**
     * Removes all of the mappings from this map.
     */
    @Override
    public void clear() {
        Entry<K, V>[] tab = table;
        for (int i = 0; i < tab.length; i++) {
            tab[i] = null;
        }
        size = 0;
    }
    
    /**
     * Returns true if this map maps one or more keys to the specified value.
     * 
     * @param value value whose presence in this map is to be tested
     * @return true if this map maps one or more keys to the specified value
     */
    @Override
    public boolean containsValue(Object value) {
        if (value == null) {
            return containsNullValue();
        }
        
        Entry<K, V>[] tab = table;
        for (int i = 0; i < tab.length; i++) {
            for (Entry<K, V> e = tab[i]; e != null; e = e.next) {
                if (value.equals(e.value)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    /**
     * Special-case code for containsValue with null argument.
     * 
     * @return true if this map contains null value
     */
    private boolean containsNullValue() {
        Entry<K, V>[] tab = table;
        for (int i = 0; i < tab.length; i++) {
            for (Entry<K, V> e = tab[i]; e != null; e = e.next) {
                if (e.value == null) {
                    return true;
                }
            }
        }
        return false;
    }
    
    /**
     * Returns a Set view of the keys contained in this map.
     * 
     * @return a set view of the keys contained in this map
     */
    @Override
    public Set<K> keySet() {
        Set<K> ks = new HashSet<>();
        for (Entry<K, V> entry : table) {
            for (Entry<K, V> e = entry; e != null; e = e.next) {
                ks.add(e.key);
            }
        }
        return ks;
    }
    
    /**
     * Returns a Collection view of the values contained in this map.
     * 
     * @return a collection view of the values contained in this map
     */
    @Override
    public Collection<V> values() {
        Collection<V> vs = new java.util.ArrayList<>();
        for (Entry<K, V> entry : table) {
            for (Entry<K, V> e = entry; e != null; e = e.next) {
                vs.add(e.value);
            }
        }
        return vs;
    }
    
    /**
     * Returns a Set view of the mappings contained in this map.
     * 
     * @return a set view of the mappings contained in this map
     */
    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        Set<Map.Entry<K, V>> es = new HashSet<>();
        for (Entry<K, V> entry : table) {
            for (Entry<K, V> e = entry; e != null; e = e.next) {
                es.add(e);
            }
        }
        return es;
    }
    
    /**
     * Entry class for key-value pairs in the hash table.
     * 
     * @param <K> the type of keys
     * @param <V> the type of values
     */
    static class Entry<K, V> implements Map.Entry<K, V> {
        final int hash;
        final K key;
        V value;
        Entry<K, V> next;
        
        /**
         * Creates new entry.
         * 
         * @param h the hash code
         * @param k the key
         * @param v the value
         * @param n the next entry in the chain
         */
        Entry(int h, K k, V v, Entry<K, V> n) {
            value = v;
            next = n;
            key = k;
            hash = h;
        }
        
        @Override
        public K getKey() {
            return key;
        }
        
        @Override
        public V getValue() {
            return value;
        }
        
        @Override
        public V setValue(V newValue) {
            V oldValue = value;
            value = newValue;
            return oldValue;
        }
        
        @Override
        public boolean equals(Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            Map.Entry<?, ?> e = (Map.Entry<?, ?>) o;
            Object k1 = getKey();
            Object k2 = e.getKey();
            if (k1 == k2 || (k1 != null && k1.equals(k2))) {
                Object v1 = getValue();
                Object v2 = e.getValue();
                if (v1 == v2 || (v1 != null && v1.equals(v2))) {
                    return true;
                }
            }
            return false;
        }
        
        @Override
        public int hashCode() {
            return Objects.hashCode(getKey()) ^ Objects.hashCode(getValue());
        }
        
        @Override
        public String toString() {
            return getKey() + "=" + getValue();
        }
    }
}
