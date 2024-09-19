package org.example.cache

import cache.Cache
import cache.exceptions.CacheExceptions
import org.lighthousegames.logging.logging
import persona.models.Persona
import java.util.*

private val logger = logging()

open class CachePersonasImpl(
    var size: Int = 1
) : Cache<UUID, Persona> {


    private val cache = mutableMapOf<UUID, Persona>()

    override fun get(key: UUID): Persona {
        logger.debug { "Obteniendo valor de la cache" }
        return cache[key] ?: run {
            logger.info { "No existe el valor en la cache" }
            throw CacheExceptions.CacheNotFoundException("No existe el valor en la cache")
        }
    }

    override fun put(key: UUID, value: Persona): Persona {
        logger.debug { "Guardando valor en la cache" }
        if (cache.size >= size && !cache.containsKey(key)) {
            logger.debug { "Eliminando valor más antiguo de la cache" }
            cache.remove(cache.keys.first())
        }
        cache[key] = value
        logger.debug { "Guardado valor en la cache con éxito" }
        return value
    }

    override fun remove(key: UUID): Persona {
        logger.debug { "Eliminando valor de la cache" }
        return cache.remove(key) ?: run {
            logger.info { "No existe el valor en la cache" }
            throw CacheExceptions.CacheNotFoundException("No existe el valor en la cache")
        }
    }

    override fun clear() {
        logger.debug { "Limpiando cache" }
        cache.clear()
        logger.debug { "Cache limpiada con éxito" }
    }
}
