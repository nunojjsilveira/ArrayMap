package ga.hdncompany.spot.util;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

public class ArrayMap<K, V> implements Map<K, V> {

    private ArrayList<K> keys = new ArrayList<>();
    private ArrayList<V> values = new ArrayList<>();

    @Override
    public int size() {
        return keys.size();
    }

    @Override
    public boolean isEmpty() {
        return keys.size() == 0;
    }

    @Override
    public boolean containsKey(Object key) {
        return keys.contains(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return values.contains(value);
    }

    @Override
    public V get(Object key) {
        int index = keys.indexOf(key);
        if(index < 0){
            throw new IndexOutOfBoundsException("Doesn't contain " + key.toString());
        }else if(index < keys.size()){
            return values.get(index);
        }else{
            throw new IndexOutOfBoundsException("Index bigger than map size");
        }
    }

    public V get(int index){
        if(index < 0){
            throw new IndexOutOfBoundsException("Index lower than 0");
        }else if(index < keys.size()){
            return values.get(index);
        }else{
            throw new IndexOutOfBoundsException("Index bigger than map size");
        }
    }

    public K getKey(int index){
        if(index < 0){
            throw new IndexOutOfBoundsException("Index lower than 0");
        }else if(index < keys.size()){
            return keys.get(index);
        }else{
            throw new IndexOutOfBoundsException("Index bigger than map size");
        }
    }

    public int indexOf(K key){
        int index = keys.indexOf(key);
        if(index < 0){
            throw new IndexOutOfBoundsException("Doesn't contain " + key.toString());
        }else if(index < keys.size()){
            return index;
        }else{
            throw new IndexOutOfBoundsException("Index bigger than map size");
        }
    }

    @Override
    public V put(K key, V value){
        int index = keys.indexOf(key);
        if(index < 0){
            this.add(key, value);
            return value;
        }else if(index < keys.size()){
            return values.set(index, value);
        }else{
            throw new IndexOutOfBoundsException("Index bigger than map size");
        }
    }

    @Override
    public V remove(Object key) {
        int index = keys.indexOf(key);
        if(index > -1){
            return this.remove(index);
        }
        return null;
    }

    @Override
    public void putAll(@NonNull Map<? extends K, ? extends V> m) {
        for(Entry<? extends K, ? extends V> entry: m.entrySet()){
            this.put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public void clear() {
        keys.clear();
        values.clear();
    }

    @NonNull
    @Override
    public Set<K> keySet() {
        return new LinkedHashSet<>(keys);
    }

    @NonNull
    @Override
    public Collection<V> values() {
        return values;
    }

    @NonNull
    @Override
    public Set<Entry<K, V>> entrySet() {
        Set<Entry<K, V>> set = new LinkedHashSet<>();
        int length = keys.size();
        for(int i = 0; i < length; i++){
            final int finalI = i;
            set.add(new Entry<K, V>() {
                private int index = finalI;

                @Override
                public K getKey() {
                    return keys.get(finalI);
                }

                @Override
                public V getValue() {
                    return values.get(finalI);
                }

                @Override
                public V setValue(V value) {
                    return values.set(index, value);
                }
            });
        }
        return set;
    }

    @Override
    public V getOrDefault(Object key, V defaultValue) {
        int index = keys.indexOf(key);
        if(index > -1 && index < keys.size()){
            V value = values.get(index);
            if (value == null) {
                return defaultValue;
            }else{
                return value;
            }
        }else{
            return defaultValue;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void forEach(BiConsumer<? super K, ? super V> action) {
        int length = keys.size();
        for(int i = 0; i < length; i++){
            action.accept(keys.get(i), values.get(i));
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void replaceAll(BiFunction<? super K, ? super V, ? extends V> function) {
        int length = keys.size();
        for(int i = 0; i < length; i++){
            values.set(i, function.apply(keys.get(i), values.get(i)));
        }
    }

    @Override
    public V putIfAbsent(K key, V value) {
        int index = keys.indexOf(key);
        V oldValue = null;
        if(index < 0){
            this.add(key, value);
        }else if(index < keys.size()){
            oldValue = values.get(index);
            if(oldValue == null){
                values.set(index, value);
            }
        }else{
            throw new IndexOutOfBoundsException();
        }
        return oldValue;
    }

    @Override
    public boolean remove(Object key, Object value) {
        int index = keys.indexOf(key);
        if(index > -1 && index < keys.size() && values.get(index).equals(value)){
            this.remove(index);
            return true;
        }else{
            return false;
        }
    }

    @Override
    public boolean replace(K key, V oldValue, V newValue) {
        int index = keys.indexOf(key);
        if(index > -1 && index < keys.size() && values.get(index).equals(oldValue)){
            values.set(index, newValue);
            return true;
        }else{
            return false;
        }
    }

    @Override
    public V replace(K key, V value) {
        int index = keys.indexOf(key);
        if(index < 0){
            return null;
        }else if(index < keys.size()){
            return values.set(index, value);
        }else{
            throw new IndexOutOfBoundsException();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public V computeIfAbsent(K key, Function<? super K, ? extends V> mappingFunction) {
        int index = keys.indexOf(key);
        if(index < 0){
            V newValue = mappingFunction.apply(key);
            if(newValue != null){
                this.add(key, newValue);
            }
            return newValue;
        }else if(index < keys.size()){
            V oldValue = values.get(index);
            if(oldValue == null){
                V newValue = mappingFunction.apply(key);
                if(newValue != null){
                    values.set(index, newValue);
                }
                return newValue;
            }
            return oldValue;
        }else{
            throw new IndexOutOfBoundsException();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public V computeIfPresent(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        int index = keys.indexOf(key);
        if(index > -1 && index < keys.size()){
            V oldValue = values.get(index);
            if(oldValue != null){
                V newValue = remappingFunction.apply(key, oldValue);
                if(newValue != null){
                    values.set(index, newValue);
                }else{
                    this.remove(index);
                }
                return newValue;
            }else{
                return oldValue;
            }
        }else{
            throw new IndexOutOfBoundsException();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public V compute(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        int index = keys.indexOf(key);
        V newValue;
        if(index < 0){
            newValue = remappingFunction.apply(key, null);
            if(newValue != null){
                this.add(key, newValue);
            }
        }else if(index < keys.size()){
            V oldValue = values.get(index);
            newValue = remappingFunction.apply(key, oldValue);
            if(newValue != null){
                values.set(index, newValue);
            }else if(oldValue != null){
                this.remove(index);
            }
        }else{
            throw new IndexOutOfBoundsException();
        }
        return newValue;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public V merge(K key, V value, BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
        int index = keys.indexOf(key);
        if(index < 0){
            if(value != null){
                this.add(key, value);
                return value;
            }else{
                return null;
            }
        }else if(index < keys.size()){
            V oldValue = values.get(index);
            V newValue = (oldValue == null ? value : remappingFunction.apply(oldValue, value));
            if(newValue != null){
                values.set(index, newValue);
            }else{
                this.remove(index);
            }
            return newValue;
        }else{
            throw new IndexOutOfBoundsException();
        }
    }

    public boolean areAllSet(){
        for(V v: values){
            if((v instanceof String && ((String) v).isEmpty()) || v == null){
                return false;
            }
        }
        return true;
    }

    private V remove(int index){
        keys.remove(index);
        return values.remove(index);
    }

    private void add(K key, V value){
        keys.add(key);
        values.add(value);
    }

}
