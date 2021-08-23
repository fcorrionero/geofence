package com.europcar.geofence.application

data class CreateGeofenceCommand(
    val id: String,
    val geoJson: String,
    val name: String,
    val description: String,
    val delayInSecondsForGeofenceDetection: Long)
