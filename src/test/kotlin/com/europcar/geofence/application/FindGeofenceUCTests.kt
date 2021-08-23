package com.europcar.geofence.application

import com.europcar.geofence.domain.GeofenceAggregate
import com.europcar.geofence.domain.GeofenceRepository
import com.europcar.sharedkernel.domain.EntityNotFoundException
import org.junit.jupiter.api.*
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import java.util.*

class FindGeofenceUCTests {

    @Mock
    lateinit var geofenceRepositoryMock: GeofenceRepository

    @Mock
    lateinit var geofenceAggregateMock: GeofenceAggregate

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
    fun shouldFindAnAggregate() {
        val findGeofenceCommand = FindGeofenceCommand("0c7edd6a-b03c-4029-913f-1c734f978dbd")
        Mockito.`when`(geofenceAggregateMock.id).thenReturn(UUID.fromString(findGeofenceCommand.id))
        Mockito.`when`(geofenceRepositoryMock.findOrFailsById(UUID.fromString(findGeofenceCommand.id)))
            .thenReturn(geofenceAggregateMock)

        val findGeofenceCommandHandler = FindGeofenceCommandHandler(geofenceRepositoryMock)

        val aggregate = findGeofenceCommandHandler.dispatch(findGeofenceCommand)
        Assertions.assertEquals(findGeofenceCommand.id, aggregate.id.toString())
        Mockito.verify(geofenceRepositoryMock, Mockito.times(1)).findOrFailsById(UUID.fromString(findGeofenceCommand.id))
    }

    @Test
    fun shouldThrowAnExceptionIfFindFails() {
        val findGeofenceCommand = FindGeofenceCommand("0c7edd6a-b03c-4029-913f-1c734f978dbd")
        Mockito.`when`(geofenceRepositoryMock.findOrFailsById(UUID.fromString(findGeofenceCommand.id)))
            .thenThrow(EntityNotFoundException::class.java)

        val findGeofenceCommandHandler = FindGeofenceCommandHandler(geofenceRepositoryMock)

        assertThrows<EntityNotFoundException> { findGeofenceCommandHandler.dispatch(findGeofenceCommand) }
    }
}