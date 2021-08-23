package com.europcar.geofence.domain

import com.europcar.geofence.application.GeoJsonValidatorService
import org.json.JSONObject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.core.io.ClassPathResource
import java.time.Duration
import java.util.*


class GeofenceAggregateTests {

    companion object {
        lateinit var geoJsonValidatorService: GeoJsonValidatorService

        @BeforeAll
        @JvmStatic
        internal fun beforeAll() {
            val geoJson = ClassPathResource("geoJsonSchema/geojson.json").file.absolutePath
            val geometry = ClassPathResource("geoJsonSchema/geometry.json").file.absolutePath
            val crs = ClassPathResource("geoJsonSchema/crs.json").file.absolutePath
            val bbox = ClassPathResource("geoJsonSchema/bbox.json").file.absolutePath

            geoJsonValidatorService = GeoJsonValidatorService(geoJson, geometry, crs, bbox)
        }
    }


    @Test
    fun geoJsonValidatorShouldCreateGeofenceCorrectly() {
        val givenId = UUID.randomUUID()
        val givenGeoJson = JSONObject(
            "{\n" +
                    "  \"type\": \"FeatureCollection\",\n" +
                    "  \"features\": [\n" +
                    "    {\n" +
                    "      \"type\": \"Feature\",\n" +
                    "      \"properties\": {},\n" +
                    "      \"geometry\": {\n" +
                    "        \"type\": \"Polygon\",\n" +
                    "        \"coordinates\": [\n" +
                    "          [\n" +
                    "            [\n" +
                    "              -5.361328125,\n" +
                    "              28.07198030177986\n" +
                    "            ],\n" +
                    "            [\n" +
                    "              -5.537109374999999,\n" +
                    "              19.228176737766262\n" +
                    "            ],\n" +
                    "            [\n" +
                    "              7.822265625000001,\n" +
                    "              27.994401411046148\n" +
                    "            ],\n" +
                    "            [\n" +
                    "              -5.361328125,\n" +
                    "              28.07198030177986\n" +
                    "            ]\n" +
                    "          ]\n" +
                    "        ]\n" +
                    "      }\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"type\": \"Feature\",\n" +
                    "      \"properties\": {},\n" +
                    "      \"geometry\": {\n" +
                    "        \"type\": \"Polygon\",\n" +
                    "        \"coordinates\": [\n" +
                    "          [\n" +
                    "            [\n" +
                    "              -2.48291015625,\n" +
                    "              19.435514339097825\n" +
                    "            ],\n" +
                    "            [\n" +
                    "              0.9228515625,\n" +
                    "              19.932041306115536\n" +
                    "            ],\n" +
                    "            [\n" +
                    "              -1.12060546875,\n" +
                    "              21.3303150734318\n" +
                    "            ],\n" +
                    "            [\n" +
                    "              -2.48291015625,\n" +
                    "              19.435514339097825\n" +
                    "            ]\n" +
                    "          ]\n" +
                    "        ]\n" +
                    "      }\n" +
                    "    }\n" +
                    "  ]\n" +
                    "}"
        )
        val givenName = "some geofence name"
        val givenDescription = "some geofence description"
        val givenDelayInSecondForGeofenceDetection = Duration.ofSeconds(1)

        val geofence = GeofenceAggregate(
            givenId,
            givenGeoJson,
            givenName,
            givenDescription,
            givenDelayInSecondForGeofenceDetection,
            geoJsonValidatorService
        )
        assertEquals(givenId, geofence.id)
        assertEquals(givenGeoJson, geofence.geoJson)
        assertEquals(givenName, geofence.name)
        assertEquals(givenDescription, geofence.description)
        assertEquals(givenDelayInSecondForGeofenceDetection, geofence.delayInSecondForGeofenceDetection)
    }


    @Test
    fun shouldThrowErrorWhenInvalidGeoJsonIsProvided() {
        val givenId = UUID.randomUUID()
        val givenGeoJson = JSONObject(
            "{\n" +
                    "  \"features\": [\n" +
                    "    {\n" +
                    "      \"type\": \"Feature\",\n" +
                    "      \"properties\": {},\n" +
                    "      \"geometry\": {\n" +
                    "        \"type\": \"Polygon\",\n" +
                    "        \"coordinates\": [\n" +
                    "          [\n" +
                    "            [\n" +
                    "              -5.361328125,\n" +
                    "              28.07198030177986\n" +
                    "            ],\n" +
                    "            [\n" +
                    "              -5.537109374999999,\n" +
                    "              19.228176737766262\n" +
                    "            ],\n" +
                    "            [\n" +
                    "              7.822265625000001,\n" +
                    "              27.994401411046148\n" +
                    "            ],\n" +
                    "            [\n" +
                    "              -5.361328125,\n" +
                    "              28.07198030177986\n" +
                    "            ]\n" +
                    "          ]\n" +
                    "        ]\n" +
                    "      }\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"type\": \"Feature\",\n" +
                    "      \"properties\": {},\n" +
                    "      \"geometry\": {\n" +
                    "        \"type\": \"Polygon\",\n" +
                    "        \"coordinates\": [\n" +
                    "          [\n" +
                    "            [\n" +
                    "              -2.48291015625,\n" +
                    "              19.435514339097825\n" +
                    "            ],\n" +
                    "            [\n" +
                    "              0.9228515625,\n" +
                    "              19.932041306115536\n" +
                    "            ],\n" +
                    "            [\n" +
                    "              -1.12060546875,\n" +
                    "              21.3303150734318\n" +
                    "            ],\n" +
                    "            [\n" +
                    "              -2.48291015625,\n" +
                    "              19.435514339097825\n" +
                    "            ]\n" +
                    "          ]\n" +
                    "        ]\n" +
                    "      }\n" +
                    "    }\n" +
                    "  ]\n" +
                    "}"
        )

        val givenName = "some geofence name"
        val givenDescription = "some geofence description"
        val givenDelayInSecondForGeofenceDetection = Duration.ofSeconds(1)


        assertThrows<GeoJsonInvalidException> {
            GeofenceAggregate(
                givenId,
                givenGeoJson,
                givenName,
                givenDescription,
                givenDelayInSecondForGeofenceDetection,
                geoJsonValidatorService
            )
        }

    }

}
