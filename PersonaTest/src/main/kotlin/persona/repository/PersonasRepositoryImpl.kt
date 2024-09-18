package persona.repository

import persona.models.Persona
import org.lighthousegames.logging.logging

class PersonasRepositoryImpl : PersonasRepository {
    
    private val logger = logging()

    private val db = hashMapOf<String, Persona>()
    
    override fun getAll(): List<Persona> {
        logger.debug { "Obteniendo todas las personas" }
        logger.info { "Total de personas: ${db.size}" }

        return db.values.toList()
    }

    override fun getById(dni: String): Persona? {
        logger.debug { "Obteniendo persona con dni: $dni" }

        val persona = db[dni]

        if (persona == null){
            logger.warn { "La persona con dni $dni no existe" }
        }else{
            logger.info { "Persona encontrada con deni $dni" }
        }

        return persona
    }

    override fun save(t: Persona): Persona {
        logger.debug { "Guardando persona $t" }

        val nuevaPersona = t.copy()
        logger.info { "Persona guardada $t" }

        return nuevaPersona
    }

    override fun update(dni: String, t: Persona): Persona? {
        logger.debug { "Actualizando persona $t" }
        val persona = db[dni]

        if (persona == null){
            logger.warn { "La persona con dni $dni no existe" }
            return null
        }else{
            val updatePersona = persona.copy(
                id= t.id,
                dni = t.dni,
                nombre = t.nombre,
                cuentaBancaria = t.cuentaBancaria,
                tarjeta = t.tarjeta
            )
            db[dni] = updatePersona
            logger.info { "Persona actualizada $updatePersona" }
            return updatePersona
        }
    }

    override fun delete(dni: String): Persona? {
        logger.debug { "Eliminando persona con dni: $dni" }
        val persona = db[dni]

        if (persona == null){
            logger.warn { "La persona con dni $dni no existe" }
            return null
        }
        db.remove(dni)
        logger.info { "Persona eliminada $persona" }
        return persona
    }
}