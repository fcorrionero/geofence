package com.europcar.geofence.application

import com.europcar.geofence.domain.GeofenceAggregate
import com.europcar.geofence.domain.GeofenceRepository
import com.europcar.sharedkernel.domain.EntityDeleteException
import com.europcar.sharedkernel.domain.EntityNotFoundException
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import java.util.*

class DeleteGeofenceUCTest {

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
    fun shouldDeleteAggregate() {
        val deleteGeofenceCommand = DeleteGeofenceCommand("0c7edd6a-b03c-4029-913f-1c734f978dbd")
        Mockito.`when`(geofenceAggregateMock.id).thenReturn(UUID.fromString(deleteGeofenceCommand.id))
        Mockito.`when`(geofenceRepositoryMock.findOrFailsById(UUID.fromString(deleteGeofenceCommand.id)))
            .thenReturn(geofenceAggregateMock)

        val deleteGeofenceCommandHandler = DeleteGeofenceCommandHandler(geofenceRepositoryMock)

        deleteGeofenceCommandHandler.dispatch(deleteGeofenceCommand)
        Mockito.verify(geofenceRepositoryMock, Mockito.times(1)).delete(geofenceAggregateMock)
    }

    @Test
    fun shouldThrowAnExceptionIfFindFails() {
        val deleteGeofenceCommand = DeleteGeofenceCommand("0c7edd6a-b03c-4029-913f-1c734f978dbd")
        Mockito.`when`(geofenceRepositoryMock.findOrFailsById(UUID.fromString(deleteGeofenceCommand.id)))
            .thenThrow(EntityNotFoundException::class.java)

        val deleteGeofenceCommandHandler = DeleteGeofenceCommandHandler(geofenceRepositoryMock)

        assertThrows<EntityNotFoundException> { deleteGeofenceCommandHandler.dispatch(deleteGeofenceCommand) }
    }

    @Test
    fun shouldThrowAnExceptionIfDeleteFails() {
        val deleteGeofenceCommand = DeleteGeofenceCommand("0c7edd6a-b03c-4029-913f-1c734f978dbd")
        Mockito.`when`(geofenceAggregateMock.id).thenReturn(UUID.fromString(deleteGeofenceCommand.id))
        Mockito.`when`(geofenceRepositoryMock.findOrFailsById(UUID.fromString(deleteGeofenceCommand.id)))
            .thenReturn(geofenceAggregateMock)
        Mockito.`when`(geofenceRepositoryMock.delete(geofenceAggregateMock))
            .thenThrow(EntityDeleteException::class.java)

        val deleteGeofenceCommandHandler = DeleteGeofenceCommandHandler(geofenceRepositoryMock)

        assertThrows<EntityDeleteException> { deleteGeofenceCommandHandler.dispatch(deleteGeofenceCommand) }
        Mockito.verify(geofenceRepositoryMock, Mockito.times(1)).findOrFailsById(UUID.fromString(deleteGeofenceCommand.id))
    }

}