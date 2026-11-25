package org.bazar.space.infrastructure.config.env

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import org.springframework.boot.env.OriginTrackedMapPropertySource
import org.springframework.context.support.GenericApplicationContext
import org.springframework.core.env.MapPropertySource
import org.springframework.core.env.MissingRequiredPropertiesException
import org.springframework.core.env.StandardEnvironment

class RequiredEnvironmentVariablesInitializerTest {

    private val initializer = RequiredEnvironmentVariablesInitializer()

    @Test
    fun `context refresh fails listing missing env variables`() {
        val context = GenericApplicationContext()
        context.environment = environmentWith(
            mapOf("a" to "\${MISSING_ONE}", "b" to "\${PRESENT_ONE}")
        ).apply {
            propertySources.addFirst(MapPropertySource("env", mapOf("PRESENT_ONE" to "x")))
        }

        initializer.initialize(context)

        assertThatThrownBy { context.refresh() }
            .isInstanceOf(MissingRequiredPropertiesException::class.java)
            .hasMessageContaining("MISSING_ONE")
            .hasMessageNotContaining("PRESENT_ONE")
    }

    private fun environmentWith(config: Map<String, Any>): StandardEnvironment =
        StandardEnvironment().apply {
            propertySources.addLast(OriginTrackedMapPropertySource("application.yaml", config))
        }
}
