package models.persona

import java.util.*

data class Persona(
    val id : UUID,
    val nombre : String,
    val dni : String,
    val cuentaBancaria : CuentaBancaria,
    val tarjeta : Tarjeta
) {
}