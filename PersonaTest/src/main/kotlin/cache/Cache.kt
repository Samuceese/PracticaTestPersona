package cache

interface Cache<K, T> {
    fun get(key: K): T?
    fun put(key: K, value: T): T
    fun remove(key: K): T?
    fun clear()
}
