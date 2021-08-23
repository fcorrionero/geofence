package com.europcar.geofence.entryPoint.API

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class GeofenceApplication

fun main(args: Array<String>) {
	runApplication<GeofenceApplication>(*args)
}
