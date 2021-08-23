package com.europcar.geofence.domain


import com.europcar.sharedkernel.domain.EntityDeleteException
import com.europcar.sharedkernel.domain.EntityNotFoundException
import com.europcar.sharedkernel.domain.EntityPersistException
import java.util.*
import kotlin.jvm.Throws

interface GeofenceRepository {
    @Throws(EntityPersistException::class)
    fun save(aggregate: GeofenceAggregate)

    @Throws(EntityNotFoundException::class)
    fun findOrFailsById(id: UUID): GeofenceAggregate

    @Throws(EntityDeleteException::class)
    fun delete(aggregate: GeofenceAggregate)

    fun findAllPaginated(limit: Int, offset: Int): Array<GeofenceAggregate>
}
