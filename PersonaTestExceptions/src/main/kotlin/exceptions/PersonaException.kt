package cache.exceptions

sealed class PersonaException(message:String): Exception(message) {
    class PersonaNoEncontradaExcepcion(): PersonaException("La persona no ha sido encontrada.")
    class PersonaNoActualizadaExcepcion(): PersonaException("La persona no ha sido actualizada.")
    class PersonaNoBorradaExcepcion(): PersonaException("La persona no ha sido borrada.")
}