package persona.repository

import org.lighthousegames.logging.logging
import persona.models.Persona
import java.util.UUID

private val logger= logging()

class PersonasRepositoryImpl : PersonasRepository {

    private val db = mutableMapOf<UUID, Persona>()

    override fun getAll(): List<Persona> {
        logger.debug { "Obteniendo todos los clientes" }

        return db.values.toList()
    }

    override fun getById(id: UUID): Persona? {
        logger.debug { "Obteniendo persona por id: $id" }

        val persona = db[id]
        if (persona == null) {
            logger.warn { "Persona con id: $id no encontrada" }
        } else {
            logger.info { "Persona encontrado: $persona" }
        }
        return persona
    }

    override fun save(persona: Persona): Persona {
        logger.debug { "Guardando persona: $persona" }
        db[persona.id] = persona
        logger.info { "Persona guardada: $persona" }
        return persona
    }

    override fun update(id: UUID, persona: Persona): Persona? {
        logger.debug { "Actualizando persona con id: $id" }
        val personaExistente = db[id]
        if (personaExistente == null) {
            logger.warn { "Persona con id: $id no encontrada" }
            return null
        } else {
            val personaActualizada = personaExistente.copy(
                nombre = persona.nombre,
                tarjeta = persona.tarjeta,
                cuentaBancaria = persona.cuentaBancaria
            )
            db[id] = personaActualizada
            logger.info { "Cliente actualizado: $personaActualizada" }
            return personaActualizada
        }
    }


    override fun delete(id: UUID): Persona? {
        logger.debug { "Borrando persona con id: $id" }
        val persona = db[id]
        if (persona == null) {
            logger.warn { "Persona con id: $id no encontrado" }
            return null
        } else {
            db.remove(id)
            logger.info { "Cliente borrado: $persona" }
            return persona
        }
    }

}