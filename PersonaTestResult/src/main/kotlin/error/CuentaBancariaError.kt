package error



sealed class CuentaBancariaError(message:String) : AllError(message) {
    class IbanIncorrectoError(message:String):CuentaBancariaError("El iban no es valido")
    class SaldoError(message: String):CuentaBancariaError("El saldo no es correcto")
}