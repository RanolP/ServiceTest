package io.github.ranolp.servicetest

import org.junit.Assert.*
import org.junit.Test

class VersionTest {
    @Test
    fun `test version class`() {
        val v1_0 = Version(1, 0)
        val v1_0_1_alpha_001 = Version(1, 0, 1, "alpha", "001")
        val v1_0_1_alpha = Version(1, 0, 1, "alpha")
        val v1_0_1_001 = Version(1, 0, 1, buildMetadata = "001")
        val v1_0_1 = Version(1, 0, 1)
        val v2_0_alpha = Version("2.0-alpha")
        val v2_0_alpha_001 = Version("2.0-alpha+001")
        val v2_0_001 = Version("2.0+001")
        val v2_0 = Version("2.0.0")
        try {
            Version("3-alpha")
            fail()
        } catch (error: Version.VersionParseError) {
        }

        assertTrue(v1_0 < v1_0_1_alpha)
        assertTrue(v1_0 < v1_0_1_alpha_001)
        assertTrue(v1_0_1_alpha == v1_0_1_alpha_001)
        assertTrue(v1_0_1_alpha < v1_0_1)
        assertTrue(v1_0_1_alpha_001 < v1_0_1)
        assertTrue(v1_0_1_001 == v1_0_1)
        assertTrue(v1_0_1 < v2_0_alpha)
        assertTrue(v2_0_alpha == v2_0_alpha_001)
        assertTrue(v2_0_alpha < v2_0_001)
        assertTrue(v2_0_alpha < v2_0)

        assertEquals(v1_0.stringfy(), "1.0.0")
        assertEquals(v1_0_1_alpha_001.stringfy(), "1.0.1-alpha+001")
        assertEquals(v1_0_1_001.stringfy(), "1.0.1+001")
    }
}