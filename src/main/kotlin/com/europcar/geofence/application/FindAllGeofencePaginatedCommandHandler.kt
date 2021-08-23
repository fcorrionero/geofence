package com.europcar.geofence.application

import com.europcar.geofence.domain.GeofenceAggregate
import com.europcar.geofence.domain.GeofenceRepository

class FindAllGeofencePaginatedCommandHandler(private val geofenceRepository: GeofenceRepository) {
    fun dispatch(command: FindAllGeofencePaginatedCommand): Array<GeofenceAggregate> {
        return geofenceRepository.findAllPaginated(command.limit, command.offset)
    }

}
