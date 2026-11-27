package ie.setu.persistence

import tools.jackson.databind.json.JsonMapper
import tools.jackson.module.kotlin.kotlinModule
import java.io.File

class JSONSerializer(private val file: File) : Serializer {

    private val mapper = JsonMapper.builder()
        .addModule(kotlinModule())
        .build()

    override fun write(obj: Any?) {
        mapper.writerWithDefaultPrettyPrinter().writeValue(file, obj)
    }

    override fun <T> read(classType: Class<T>): T {
        return mapper.readValue(file, classType)
    }
}