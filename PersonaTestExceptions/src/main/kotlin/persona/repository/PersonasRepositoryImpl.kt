import persona.models.Persona
import org.lighthousegames.logging.logging
import persona.repository.PersonasRepository
import java.util.*

class PersonasRepositoryImpl : PersonasRepository {

    private val logger = logging()

    private val db = mutableMapOf<UUID, Persona>()  // Cambiado a UUID como clave

    override fun getAll(): List<Persona> {
        logger.debug { "Obteniendo todas las personas" }
        logger.info { "Total de personas: ${db.size}" }

        return db.values.toList()
    }

    override fun getById(id: UUID): Persona? {
        logger.debug { "Obteniendo persona con id: $id" }

        val persona = db[id]

        if (persona == null) {
            logger.warn { "La persona con id $id no existe" }
        } else {
            logger.info { "Persona encontrada con id $id" }
        }

        return persona
    }

    override fun save(t: Persona): Persona {
        logger.debug { "Guardando persona $t" }

        db[t.id] = t

        logger.info { "Persona guardada $t" }

        return t
    }

    override fun update(id: UUID, t: Persona): Persona? {
        logger.debug { "Actualizando persona con id: $id" }

        val persona = db[id]

        if (persona == null) {
            logger.warn { "La persona con id $id no existe" }
            return null
        } else {
            val updatedPersona = persona.copy(
                nombre = t.nombre,
                cuentaBancaria = t.cuentaBancaria,
                tarjeta = t.tarjeta
            )
            db[id] = updatedPersona
            logger.info { "Persona actualizada $updatedPersona" }
            return updatedPersona
        }
    }

    override fun delete(id: UUID): Persona? {
        logger.debug { "Eliminando persona con id: $id" }

        val persona = db.remove(id)

        if (persona == null) {
            logger.warn { "La persona con id $id no existe" }
        } else {
            logger.info { "Persona eliminada $persona" }
        }

        return persona
    }
}
