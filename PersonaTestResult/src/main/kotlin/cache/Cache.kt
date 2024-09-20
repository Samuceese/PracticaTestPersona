package cache

interface Cache<K, T> {
    fun get(key: K): T?
    fun put(key: K, value: T)
    fun remove(key: K)
    fun clear()
    fun size(): Int
}
