package cache.exceptions

sealed class CacheExceptions(message: String) : Exception(message) {
    class CacheNotFoundException(message: String) : CacheExceptions("No se ha encontrado el valor $message")
}