tasks.register("createFeature") {
    group = "generation"
    description =
        "Creates a new complex feature package under the unified server-features module. Usage: ./gradlew createFeature -PfeatureName=myfeature"

    doLast {
        val name = project.findProperty("featureName") as? String
            ?: throw GradleException("Please provide a feature name using -PfeatureName=feature-name")

        val capitalizedName = name.replaceFirstChar { it.uppercase() }
        val rootDir = project.rootDir
        val basePackage = "com.smach.zapmancer.$name"
        val packagePath = basePackage.replace(".", "/")
        val featureSrcDir = File(rootDir, "server/features/src/main/kotlin/$packagePath")
        val featureTestDir = File(rootDir, "server/features/src/test/kotlin/$packagePath")

        if (featureSrcDir.exists()) {
            println("Feature package path '$featureSrcDir' already exists.")
            return@doLast
        }

        // 1. Directories (Source + Test)
        val sourceLayers = listOf("api", "domain", "data", "routing", "di")
        sourceLayers.forEach { File(featureSrcDir, it).mkdirs() }

        val testLayers = listOf("domain", "routing")
        testLayers.forEach { File(featureTestDir, it).mkdirs() }

        fun createSrcFile(layer: String, fileName: String, content: String) =
            File(File(featureSrcDir, layer), fileName).writeText(content.trimIndent())

        fun createTestFile(layer: String, fileName: String, content: String) =
            File(File(featureTestDir, layer), fileName).writeText(content.trimIndent())

        // 2. Source Boilerplate
        createSrcFile(
            "api", "${capitalizedName}Response.kt", """
            package $basePackage.api
            import kotlinx.serialization.Serializable

            @Serializable
            data class ${capitalizedName}Response(val id: Int, val name: String)
        """
        )

        createSrcFile(
            "domain", "${capitalizedName}Service.kt", """
            package $basePackage.domain
            import $basePackage.api.${capitalizedName}Response
            import $basePackage.data.${capitalizedName}Repository

            interface ${capitalizedName}Service {
                suspend fun getById(id: Int): ${capitalizedName}Response?
            }
            
            class ${capitalizedName}ServiceImpl(private val repository: ${capitalizedName}Repository) : ${capitalizedName}Service {
                override suspend fun getById(id: Int): ${capitalizedName}Response? = repository.findById(id)
            }
        """
        )

        createSrcFile(
            "data", "${capitalizedName}Repository.kt", """
            package $basePackage.data
            import org.jetbrains.exposed.v1.core.Table
            import $basePackage.api.${capitalizedName}Response

            object ${capitalizedName}Table : Table("$name") {
                val id = integer("id").autoIncrement()
                val name = varchar("name", 255)
                override val primaryKey = PrimaryKey(id)
            }

            class ${capitalizedName}Repository {
                suspend fun findById(id: Int): ${capitalizedName}Response? {
                    return ${capitalizedName}Response(id, "Sample")
                }
            }
        """
        )

        createSrcFile(
            "di", "${capitalizedName}Module.kt", """
            package $basePackage.di
            
            import org.koin.dsl.module
            import $basePackage.domain.${capitalizedName}Service
            import $basePackage.domain.${capitalizedName}ServiceImpl
            import $basePackage.data.${capitalizedName}Repository

            val ${name}Module = module {
                single { ${capitalizedName}Repository() }
                single<${capitalizedName}Service> { ${capitalizedName}ServiceImpl(get()) }
            }
        """
        )

        createSrcFile(
            "routing", "${capitalizedName}Routing.kt", """
            package $basePackage.routing
            
            import io.ktor.server.application.*
            import io.ktor.server.routing.*
            import io.ktor.server.response.*
            import $basePackage.domain.${capitalizedName}Service
            import org.koin.ktor.ext.inject
            import com.smach.zapmancer.common.ApiResponse

            fun Route.${name}Routing() {
                val service by inject<${capitalizedName}Service>()

                route("/$name") {
                    get("/{id}") {
                        val id = call.parameters["id"]?.toIntOrNull() ?: 0
                        val result = service.getById(id)
                        call.respond(ApiResponse(success = true, data = result))
                    }
                }
            }
        """
        )

        // 3. Test Boilerplate
        createTestFile(
            "domain", "${capitalizedName}ServiceTest.kt", """
            package $basePackage.domain
            
            import $basePackage.data.${capitalizedName}Repository
            import io.mockk.coEvery
            import io.mockk.mockk
            import kotlinx.coroutines.runBlocking
            import kotlin.test.Test
            import kotlin.test.assertNotNull

            class ${capitalizedName}ServiceTest {
                private val repository = mockk<${capitalizedName}Repository>()
                private val service = ${capitalizedName}ServiceImpl(repository)

                @Test
                fun `should return data when repository finds it`() = runBlocking {
                    coEvery { repository.findById(1) } returns mockk()
                    val result = service.getById(1)
                    assertNotNull(result)
                }
            }
        """
        )

        println("\nFeature package '$name' created under server/features/src/main/kotlin/$packagePath!")
        println("Re-register the module DI or route configurations in server-app if needed.")
    }
}
