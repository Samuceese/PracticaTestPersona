package cache

import org.lighthousegames.logging.logging
import persona.models.Persona
import java.util.*

private val logger= logging()
class CachePersonasImpl : CachePersonas {


    private val cache: MutableMap<UUID, Persona> = mutableMapOf()

    override fun get(id: UUID): Persona? {
        logger.debug { "Buscando persona con id $id en Cache" }
        return cache[id]
    }

    override fun put(id: UUID, value: Persona) {
        logger.debug { "Metiendo persona con id $id en Cache" }
        cache[id] = value
    }

    override fun remove(id: UUID) {
        logger.debug { "Eliminando persona con id $id de Cache" }
        cache.remove(id)
    }

    override fun clear(){
        logger.debug { "Eliminando cache" }
        cache.clear()
    }

    override fun size() = cache.size
}