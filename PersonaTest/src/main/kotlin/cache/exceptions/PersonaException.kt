package cache.exceptions

sealed class PersonaException(message:String): Exception(message) {
    class PersonaNoEncontradaExcepcion(message:String): PersonaException("La persona no ha sido encontrada.")
    class PersonaNoActualizadaExcepcion(message:String): PersonaException("La persona no ha sido actualizada.")
    class PersonaNoBorradaExcepcion(message:String): PersonaException("La persona no ha sido borrada.")
}