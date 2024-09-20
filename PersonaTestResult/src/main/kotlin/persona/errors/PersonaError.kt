package persona.errors

import error.AllError

sealed class PersonaError(message:String): AllError(message)  {
    class NombreError(nombre:String): PersonaError("El nombre $nombre no es valido")
    class DniNoValidoError(dni:String): PersonaError("El dni $dni no es valido")
    class PersonaNoEncontradoError(message: String): PersonaError("La persona no se encuentra")
    class PersonaNoActalizadoError(message: String): PersonaError("La persona no se ha podido actualizar")
    class PersonaNoEliminadoError(message: String): PersonaError("La persona no se ha podido eliminar")
}