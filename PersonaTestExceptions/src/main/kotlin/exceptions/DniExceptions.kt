package cache.exceptions

import jdk.jshell.spi.ExecutionControl.RunException

sealed class DniExceptions(message : String) : RuntimeException(message) {
    class DniInvalidoException() : DniExceptions("El formato del DNI no es válido.")
    class DniLetraInvalidaException() : DniExceptions("La letra del DNI no es válida.")
}