package error

sealed class TarjetaError (message:String): AllError(message)  {
    class NumeroError(message:String):TarjetaError("Numero de tarjeta no valido")
    class CaducidadError(message:String):TarjetaError("Fecha de caducidad no valida")
}