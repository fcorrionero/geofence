package com.europcar.geofence.application

import com.europcar.geofence.domain.GeofenceAggregate
import com.europcar.geofence.domain.GeofenceRepository
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.mock

class FindAllGeofencesPaginatedUCTests {

    @Mock
    lateinit var geofenceRepositoryMock: GeofenceRepository

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
    fun shouldFindAnArrayOfGeofenceAggregates() {
        val geofence1 = mock<GeofenceAggregate>()
        val geofence2 = mock<GeofenceAggregate>()
        val geofence3 = mock<GeofenceAggregate>()
        val geofence4 = mock<GeofenceAggregate>()
        val geofence5 = mock<GeofenceAggregate>()

        val geofenceArray = arrayOf(geofence1, geofence2, geofence3, geofence4, geofence5)

        val limit = 5
        val offset = 0
        val findAllGeofencePaginatedCommand = FindAllGeofencePaginatedCommand(limit, offset)

        Mockito.`when`(
            geofenceRepositoryMock.findAllPaginated(
                findAllGeofencePaginatedCommand.limit,
                findAllGeofencePaginatedCommand.offset
            )
        )
            .thenReturn(geofenceArray)

        val findAllGeofencePaginatedCommandHandler = FindAllGeofencePaginatedCommandHandler(geofenceRepositoryMock)

        val aggregates = findAllGeofencePaginatedCommandHandler.dispatch(findAllGeofencePaginatedCommand)
        Mockito.verify(geofenceRepositoryMock, Mockito.times(1))
            .findAllPaginated(findAllGeofencePaginatedCommand.limit, findAllGeofencePaginatedCommand.offset)

        Assertions.assertEquals(aggregates, geofenceArray)
    }

    @Test
    fun shouldReturnAnEmptyArrayOfGeofence() {
        val geofenceArray = emptyArray<GeofenceAggregate>()

        val limit = 5
        val offset = 10
        val findAllGeofencePaginatedCommand = FindAllGeofencePaginatedCommand(limit, offset)

        Mockito.`when`(
            geofenceRepositoryMock.findAllPaginated(
                findAllGeofencePaginatedCommand.limit,
                findAllGeofencePaginatedCommand.offset
            )
        )
            .thenReturn(geofenceArray)

        val findAllGeofencePaginatedCommandHandler = FindAllGeofencePaginatedCommandHandler(geofenceRepositoryMock)
        val aggregates = findAllGeofencePaginatedCommandHandler.dispatch(findAllGeofencePaginatedCommand)
        Mockito.verify(geofenceRepositoryMock, Mockito.times(1))
            .findAllPaginated(findAllGeofencePaginatedCommand.limit, findAllGeofencePaginatedCommand.offset)
        Assertions.assertEquals(aggregates, geofenceArray)
    }
}