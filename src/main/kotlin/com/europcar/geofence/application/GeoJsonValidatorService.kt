package com.europcar.geofence.application

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.github.fge.jsonschema.core.exceptions.ProcessingException
import com.github.fge.jsonschema.core.load.configuration.LoadingConfiguration
import com.github.fge.jsonschema.main.JsonSchema
import com.github.fge.jsonschema.main.JsonSchemaFactory
import org.json.JSONObject
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStream

@Component
class GeoJsonValidatorService(
    @Value("classpath:geoJsonSchema/geojson.json") val geoJson: String?,
    @Value("classpath:geoJsonSchema/geometry.json")val geometry: String?,
    @Value("classpath:geoJsonSchema/crs.json")val crs: String?,
    @Value("classpath:geoJsonSchema/bbox.json")val bbox: String?
) {

    fun validate(geoJson: JSONObject): Boolean {
        val geoJsonSchema: JsonSchema = this.getLocalGeoJsonSchema()
        val objectMapper = ObjectMapper()
        return geoJsonSchema.validate(objectMapper.readTree(geoJson.toString())).isSuccess
    }

    @Throws(IOException::class, ProcessingException::class)
    private fun getLocalGeoJsonSchema(): JsonSchema {

        val geoJsonSchema: InputStream =
            FileInputStream(File(geoJson))
        val geometrySchema: InputStream =
            FileInputStream(File(geometry))
        val crsSchema: InputStream = FileInputStream(File(crs))
        val bboxSchema: InputStream = FileInputStream(File(bbox))

        val objectMapper = ObjectMapper()

        val geoJsonSchemaNode: JsonNode = objectMapper.readTree(geoJsonSchema)
        val geometrySchemaNode: JsonNode = objectMapper.readTree(geometrySchema)
        val crsSchemaNode: JsonNode = objectMapper.readTree(crsSchema)
        val bboxSchemaNode: JsonNode = objectMapper.readTree(bboxSchema)
        val loadingCfg = LoadingConfiguration.newBuilder()
            .preloadSchema(geoJsonSchemaNode)
            .preloadSchema(geometrySchemaNode)
            .preloadSchema(crsSchemaNode)
            .preloadSchema(bboxSchemaNode)
            .freeze()
        val jsonSchemaFactory = JsonSchemaFactory.newBuilder()
            .setLoadingConfiguration(loadingCfg)
            .freeze()
        return jsonSchemaFactory.getJsonSchema(geoJsonSchemaNode)
    }

}