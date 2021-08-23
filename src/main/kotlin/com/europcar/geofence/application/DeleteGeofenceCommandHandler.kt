package com.europcar.geofence.application

import com.europcar.geofence.domain.GeofenceRepository
import java.util.*

class DeleteGeofenceCommandHandler(private val geofenceRepository: GeofenceRepository) {
    fun dispatch(command: DeleteGeofenceCommand) {
        val aggregate = geofenceRepository.findOrFailsById(UUID.fromString(command.id))

        geofenceRepository.delete(aggregate)

    }
}
