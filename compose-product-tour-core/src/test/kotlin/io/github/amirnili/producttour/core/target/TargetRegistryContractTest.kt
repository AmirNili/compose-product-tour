package io.github.amirnili.producttour.core.target

import io.github.amirnili.producttour.core.model.Bounds
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class TargetRegistryContractTest {
    private class FakeRegistry : TargetRegistry {
        private val values = linkedMapOf<String, TargetRegistration>()

        override fun register(target: TargetRegistration) {
            values[target.id] = target
        }

        override fun unregister(id: String) {
            values.remove(id)
        }

        override fun find(id: String): TargetRegistration? = values[id]

        override fun all(): List<TargetRegistration> = values.values.toList()
    }

    @Test
    fun unknownTargetIsNullAndRegistrationCanBeRemoved() {
        val registry = FakeRegistry()
        assertNull(registry.find("missing"))
        registry.register(
            TargetRegistration(
                id = "search",
                bounds = Bounds(0f, 0f, 10f, 10f),
                isLaidOut = true,
            ),
        )
        assertEquals(1, registry.all().size)
        registry.unregister("search")
        assertNull(registry.find("search"))
    }
}
