package validators

class DniValidator {

    fun validarDni(dni : String) : Boolean {
        val regex = Regex("[0-9]{8}[A-Z]")

        if (dni.matches(regex)){
            return false
        }

        val numerosDni = dni.slice(0..8).toInt()
        val letraDni = dni.slice(8..9).uppercase()

        val letrasDni = "TRWAGMYFPDXBNJZSQVHLCKE"

        val resto = (numerosDni % 23)
        val letraCorrecta = letrasDni[resto].toString()

        if (letraDni == letraCorrecta){
            return true
        }else{
            return false        }

    }
}