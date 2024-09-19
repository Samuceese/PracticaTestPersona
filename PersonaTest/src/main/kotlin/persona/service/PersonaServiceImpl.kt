package dev.joseluisgs.personas.services

import cache.CachePersonas
import cache.exceptions.PersonaException
import org.lighthousegames.logging.logging
import persona.models.Persona
import persona.repository.PersonasRepository
import persona.service.PersonasService
import java.util.*

class PersonasServiceImpl(
    private val cache: CachePersonas,
    private val repository: PersonasRepository
) : PersonasService {

    private val logger = logging()

    override fun getAll(): List<Persona> {
        logger.debug { "Obteniendo todas las personas del repositorio" }
        val personas = repository.getAll()
        logger.info { "Personas obtenidas: $personas" }
        return personas
    }

    override fun findById(id: UUID): Persona {
        logger.debug { "Buscando persona con ID: $id en cache" }
        val persona = cache.get(id)
        if (persona != null) {
            logger.info { "Persona encontrada en cache: $persona" }
            return persona
        }

        logger.debug { "Buscando persona con ID: $id en el repositorio" }
        val personaFromDb = repository.getById(id)
        if (personaFromDb != null) {
            logger.info { "Persona encontrada en el repositorio: $personaFromDb" }
            cache.put(id, personaFromDb)
            return personaFromDb
        }

        logger.error { "Persona no encontrada con ID: $id" }
        throw PersonaException.PersonaNoEncontradaExcepcion("La persona no ha sido encontrada.")
    }

    override fun save(persona: Persona): Persona {
        logger.debug { "Guardando persona en el repositorio: $persona" }
        val result = repository.save(persona)
        logger.info { "Persona guardada en el repositorio: $result" }
        return result
    }

    override fun update(id: UUID, persona: Persona): Persona {
        logger.debug { "Actualizando persona en el repositorio con ID: $id y datos: $persona" }
        cache.remove(id)
        val result = repository.update(id, persona)
        if (result == null) {
            logger.error { "No se pudo actualizar la persona con ID: $id" }
            throw PersonaException.PersonaNoActualizadaExcepcion("La persona no ha sido actualizada.")
        }
        logger.info { "Persona actualizada en el repositorio: $result" }
        return result
    }

    override fun delete(id: UUID): Persona {
        logger.debug { "Eliminando persona en el repositorio con ID: $id" }
        cache.remove(id)
        val result = repository.delete(id)
        if (result == null) {
            logger.error { "No se pudo eliminar la persona con ID: $id" }
            throw PersonaException.PersonaNoBorradaExcepcion("La persona no ha sido borrada.")
        }
        logger.info { "Persona eliminada en el repositorio: $result" }
        return result
    }
}
