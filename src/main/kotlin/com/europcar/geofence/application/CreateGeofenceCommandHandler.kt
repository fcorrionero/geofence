package com.europcar.geofence.application

import com.europcar.geofence.domain.GeofenceAggregate
import com.europcar.geofence.domain.GeofenceRepository
import org.json.JSONObject
import java.time.Duration
import java.util.*

class CreateGeofenceCommandHandler(
    val geofenceRepository: GeofenceRepository,
    val geoJsonValidatorService: GeoJsonValidatorService) {

    fun dispatch(command: CreateGeofenceCommand) {
        val aggregate = GeofenceAggregate(
            UUID.fromString(command.id),
            JSONObject(command.geoJson),
            command.name,
            command.description,
            Duration.ofSeconds(command.delayInSecondsForGeofenceDetection),
            geoJsonValidatorService)

        geofenceRepository.save(aggregate)
    }

}
