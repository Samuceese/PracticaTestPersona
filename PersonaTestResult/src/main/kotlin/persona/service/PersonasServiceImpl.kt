package persona.service

import com.github.michaelbull.result.Result
import cache.CachePersonas
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.mapBoth
import error.AllError
import org.lighthousegames.logging.logging
import persona.errors.PersonaError
import persona.models.Persona
import persona.repository.PersonasRepository
import persona.validators.PersonaValidator
import java.util.*

private val logger = logging()

class PersonasServiceImpl(
    private val repository: PersonasRepository,
    private val cache: CachePersonas,
    private val personaValidator: PersonaValidator
):PersonasService {

    override fun getAll(): Result<List<Persona>, AllError> {
        logger.debug { "Obteniendo personas del repositorio" }
        val personas = repository.getAll()
        logger.info { "Personas obtenidos: $personas" }
        return Ok(personas)
    }

    override fun getById(id: UUID): Result<Persona,AllError> {
        logger.debug { "Buscando por id en cache" }
        return cache.get(id)
            ?.let { Ok(it) }
            ?: repository.getById(id)
                ?.let { Ok(it) }
            ?: Err(PersonaError.PersonaNoEncontradoError(id.toString()))

    }

    override fun save(persona: Persona): Result<Persona,AllError> {
        logger.debug { "Guardando persona: $persona" }

        return personaValidator.validarPersona(persona).mapBoth(
            success = {
                repository.save(persona)
                logger.debug { "Guardando persona en cache: $persona" }
                cache.put(persona.id,persona)
                Ok(persona)
            },
            failure = {
                Err(it)
            }
        )
    }

    override fun update(id: UUID, persona: Persona): Result<Persona,AllError> {
        logger.debug { "Actualizando persona con id: $id" }
        return personaValidator.validarPersona(persona).mapBoth(
            success = {
                repository.update(id, persona)?: return Err(PersonaError.PersonaNoActalizadoError(id.toString()))
                cache.put(id, persona)
                Ok(it)
            }, failure = {
                Err(it)
            }
        )
    }

    override fun delete(id: UUID): Result<Persona,AllError> {
        logger.debug { "Borrando persona con id: $id" }
        return repository.delete(id)
            ?.let {
                cache.remove(id)
                Ok(it)
            }
            ?: Err(PersonaError.PersonaNoEliminadoError(id.toString()))
    }
}