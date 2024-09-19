package cache

import persona.models.Persona
import java.util.*

interface CachePersonas : Cache<UUID, Persona> {
}