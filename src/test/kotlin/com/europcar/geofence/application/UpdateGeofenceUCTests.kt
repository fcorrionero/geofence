package com.europcar.geofence.application

import com.europcar.geofence.domain.GeofenceAggregate
import com.europcar.geofence.domain.GeofenceRepository
import com.europcar.sharedkernel.domain.EntityNotFoundException
import com.europcar.sharedkernel.domain.EntityPersistException
import org.json.JSONObject
import org.junit.jupiter.api.*
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.argumentCaptor
import java.time.Duration
import java.util.*

class UpdateGeofenceUCTests {

    @Mock
    lateinit var geofenceRepositoryMock: GeofenceRepository

    @Mock
    lateinit var geoJsonValidatorServiceMock: GeoJsonValidatorService

    private lateinit var closeable: AutoCloseable

    @BeforeEach
    fun beforeEach() {
        closeable = MockitoAnnotations.openMocks(this)
    }

    @AfterEach
    @Throws(Exception::class)
    fun afterEach() {
        closeable.close()
    }

    @Test
    fun shouldUpdateGeofenceWhenCommandIsProvided() {
        val updateGeofenceCommand = UpdateGeofenceCommand(
            "0c7edd6a-b03c-4029-913f-1c734f978dbd",
            givenGeoJson(),
            "geofence name",
            "geofence desciption",
            60
        )

        val geoJsonObject = JSONObject(updateGeofenceCommand.geoJson)
        val geoJsonArgumentCaptor = argumentCaptor<JSONObject>()
        Mockito.`when`(geoJsonValidatorServiceMock.validate(geoJsonArgumentCaptor.capture())).thenReturn(true)

        val updateGeofenceCommandHandler =
            UpdateGeofenceCommandHandler(geofenceRepositoryMock, geoJsonValidatorServiceMock)

        updateGeofenceCommandHandler.dispatch(updateGeofenceCommand)
        Mockito.verify(geoJsonValidatorServiceMock, Mockito.times(1)).validate(geoJsonArgumentCaptor.capture())
        Assertions.assertEquals(geoJsonObject.toString(), geoJsonArgumentCaptor.firstValue.toString())

        Mockito.verify(geofenceRepositoryMock, Mockito.times(1))
            .findOrFailsById(UUID.fromString(updateGeofenceCommand.id))

        val geofenceAggregateCaptor = argumentCaptor<GeofenceAggregate>()

        Mockito.verify(geofenceRepositoryMock, Mockito.times(1)).save(geofenceAggregateCaptor.capture())
        Assertions.assertEquals(updateGeofenceCommand.id, geofenceAggregateCaptor.firstValue.id.toString())
        Assertions.assertEquals(
            JSONObject(updateGeofenceCommand.geoJson).toString(),
            geofenceAggregateCaptor.firstValue.geoJson.toString()
        )
        Assertions.assertEquals(updateGeofenceCommand.description, geofenceAggregateCaptor.firstValue.description)
        Assertions.assertEquals(
            Duration.ofSeconds(updateGeofenceCommand.delayInSecondsForGeofenceDetection),
            geofenceAggregateCaptor.firstValue.delayInSecondForGeofenceDetection
        )
    }

    @Test
    fun shouldThrowAnExceptionIfFindFails() {
        val updateGeofenceCommand = UpdateGeofenceCommand(
            "0c7edd6a-b03c-4029-913f-1c734f978dbd",
            givenGeoJson(),
            "geofence name",
            "geofence desciption",
            60
        )

        val geoJsonObject = JSONObject(updateGeofenceCommand.geoJson)
        val geoJsonArgumentCaptor = argumentCaptor<JSONObject>()
        Mockito.`when`(geoJsonValidatorServiceMock.validate(geoJsonArgumentCaptor.capture())).thenReturn(true)
        Mockito.`when`(geofenceRepositoryMock.findOrFailsById(UUID.fromString(updateGeofenceCommand.id)))
            .thenThrow(EntityNotFoundException::class.java)

        val updateGeofenceCommandHandler =
            UpdateGeofenceCommandHandler(geofenceRepositoryMock, geoJsonValidatorServiceMock)

        assertThrows<EntityNotFoundException> { updateGeofenceCommandHandler.dispatch(updateGeofenceCommand) }

        Mockito.verify(geoJsonValidatorServiceMock, Mockito.times(1)).validate(geoJsonArgumentCaptor.capture())
        Assertions.assertEquals(geoJsonObject.toString(), geoJsonArgumentCaptor.firstValue.toString())

        Mockito.verify(geofenceRepositoryMock, Mockito.times(1))
            .findOrFailsById(UUID.fromString(updateGeofenceCommand.id))

    }

    @Test
    fun shouldThrowAnExceptionWhenSaveFails() {
        val updateGeofenceCommand = UpdateGeofenceCommand(
            "0c7edd6a-b03c-4029-913f-1c734f978dbd",
            givenGeoJson(),
            "geofence name",
            "geofence desciption",
            60
        )

        val geoJsonObject = JSONObject(updateGeofenceCommand.geoJson)

        val geoJsonArgumentCaptor = argumentCaptor<JSONObject>()
        Mockito.`when`(geoJsonValidatorServiceMock.validate(geoJsonArgumentCaptor.capture())).thenReturn(true)
        val updateGeofenceCommandHandler =
            UpdateGeofenceCommandHandler(geofenceRepositoryMock, geoJsonValidatorServiceMock)

        val geofenceAggregateCaptor = argumentCaptor<GeofenceAggregate>()

        Mockito.`when`(geofenceRepositoryMock.save(geofenceAggregateCaptor.capture()))
            .thenThrow(EntityPersistException::class.java)

        assertThrows<EntityPersistException> { updateGeofenceCommandHandler.dispatch(updateGeofenceCommand) }

        Mockito.verify(geoJsonValidatorServiceMock, Mockito.times(1)).validate(geoJsonArgumentCaptor.capture())
        Assertions.assertEquals(geoJsonObject.toString(), geoJsonArgumentCaptor.firstValue.toString())

        Mockito.verify(geofenceRepositoryMock, Mockito.times(1))
            .findOrFailsById(UUID.fromString(updateGeofenceCommand.id))
        Mockito.verify(geofenceRepositoryMock, Mockito.times(1)).save(geofenceAggregateCaptor.capture())
    }

    private fun givenGeoJson(): String {
        return "{\n" +
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
    }
}