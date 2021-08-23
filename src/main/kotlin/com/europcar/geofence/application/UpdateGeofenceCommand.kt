package com.europcar.geofence.application

class UpdateGeofenceCommand(
    val id: String,
    val geoJson: String,
    val name: String,
    val description: String,
    val delayInSecondsForGeofenceDetection: Long)
