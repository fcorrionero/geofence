package com.europcar.geofence.domain

import com.europcar.geofence.application.GeoJsonValidatorService
import org.json.JSONObject
import org.springframework.beans.factory.annotation.Autowired
import java.time.Duration
import java.util.*


class GeofenceAggregate(
    val id: UUID,
    val geoJson: JSONObject,
    val name: String,
    val description: String,
    val delayInSecondForGeofenceDetection: Duration,
    @Autowired val geoJsonValidator: GeoJsonValidatorService
) {

    init {
        if (!geoJsonValidator.validate(geoJson)) {
            throw GeoJsonInvalidException()
        }
    }

}
