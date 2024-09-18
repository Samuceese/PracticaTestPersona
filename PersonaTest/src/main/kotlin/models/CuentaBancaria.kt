package models

data class CuentaBancaria(
    val iban : String,
    val saldo : Double = 0.0
) {
}