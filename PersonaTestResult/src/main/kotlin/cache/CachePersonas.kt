package cache

import cache.common.Cache
import persona.models.Persona
import java.util.*

interface CachePersonas : Cache<UUID, Persona> {
}