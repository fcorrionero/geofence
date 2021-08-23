package com.europcar.geofence.application

import com.europcar.geofence.domain.GeofenceAggregate
import com.europcar.geofence.domain.GeofenceRepository
import java.util.*

class FindGeofenceCommandHandler(private val geofenceRepository: GeofenceRepository) {

    fun dispatch(findGeofenceCommand: FindGeofenceCommand): GeofenceAggregate {
        return geofenceRepository.findOrFailsById(UUID.fromString(findGeofenceCommand.id))
    }
}
