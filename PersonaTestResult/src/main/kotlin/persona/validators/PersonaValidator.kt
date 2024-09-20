package persona.validators

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.mapBoth
import error.AllError
import persona.errors.PersonaError
import persona.models.Persona

class PersonaValidator (
    val tarjetaValidator: TarjetaValidator,
    val cuentaValidator: CuentaBancariaValidator,
    val dniValidator: DniValidator
) {
    fun validarPersona(persona: Persona): Result<Persona,AllError> {
        if (!validarNombre(persona.nombre)) return Err( PersonaError.NombreError(persona.nombre))

        tarjetaValidator.validarTarjeta(persona.tarjeta).mapBoth(
            success = {

            },
            failure = {
                return Err(it)
            }
        )
        cuentaValidator.validarCuenta(persona.cuentaBancaria).mapBoth(
            success = {

            },
            failure = {
                return Err(it)
            }
        )
        dniValidator.validarDni(persona.dni).mapBoth(
            success = {

            },
            failure = {
                return Err(it)
            }
        )

        return Ok( persona)
    }

    fun validarNombre(nombre:String):Boolean{
        return nombre.length > 2
    }
}