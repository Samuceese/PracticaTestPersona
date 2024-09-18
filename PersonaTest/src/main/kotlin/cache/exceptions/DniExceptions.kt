package cache.exceptions

sealed class DniExceptions(message : String) : Exception(message) {
    class DniInvalidoException(message: String) : DniExceptions("El formato del DNI no es válido.")
    class DniLetraInvalidaException(message: String) : DniExceptions("La letra del DNI no es válida.")
}