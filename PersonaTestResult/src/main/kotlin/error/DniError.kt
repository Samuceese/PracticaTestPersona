package error

sealed class DniError(message:String):AllError(message) {
    class DniNoValidoError(dni:String):DniError("El DNI $dni no es valido")
}