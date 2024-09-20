package persona

import error.AllError

sealed class PersonaError(message:String): AllError(message)  {
    class NombreError(nombre:String):PersonaError("El nombre $nombre no es valido")
    class ClienteNoEncontradoError(message: String):PersonaError("La persona no se encuentra")
    class ClienteNoActalizadoError(message: String):PersonaError("La persona no se ha podido actualizar")
    class ClienteNoEliminadoError(message: String):PersonaError("La persona no se ha podido eliminar")
}