package io.github.ranolp.servicetest

import java.util.*

data class Version private constructor(val major: Int, val minor: Int, val patch: Int, val tag: String?, val buildMetadata: String?) : Comparable<Version> {
    class VersionParseError(val version: String) : Error("Version $version is mismatch to semver format")
    companion object {
        private val intRange = '0'..'9'
        operator fun invoke(major: Int, minor: Int = 0, patch: Int = 0, tag: String? = null, buildMetadata: String? = null): Version = Version(
                major,
                minor,
                patch,
                tag,
                buildMetadata)

        @Throws(VersionParseError::class) operator fun invoke(version: String): Version {
            var pos = 0
            fun parseInt(require: Boolean): Int {
                if (!require && version[pos] == '-' || version[pos] == '+') return 0
                if (version[pos] !in intRange) {
                    throw VersionParseError(version)
                }
                var result = 0
                loop@ while (pos < version.length) {
                    when (version[pos]) {
                        in intRange -> {
                            result = result * 10 + version[pos].toInt() - 48
                        }
                        '.' -> {
                            pos++
                            break@loop
                        }
                        '-', '+' -> break@loop
                        else -> throw VersionParseError(version)
                    }
                    pos++
                }
                return result
            }

            val major = parseInt(true)
            val minor = parseInt(true)
            val patch = parseInt(false)
            val tag = if (version.length > pos && version[pos] == '-') version.substring(pos) else null
            val buildMetadata = if (version.length > pos && version[pos] == '+') version.substring(pos) else null

            return Version(major, minor, patch, tag, buildMetadata)
        }
    }

    override fun compareTo(other: Version): Int {
        return when {
            major != other.major -> major.compareTo(other.major)
            minor != other.minor -> minor.compareTo(other.minor)
            patch != other.patch -> patch.compareTo(other.patch)
            tag == null -> if (other.tag == null) {
                0
            } else {
                1
            }
            else -> if (other.tag == null) {
                -1
            } else {
                0
            }
        }
    }

    override fun equals(other: Any?): Boolean = this === other || other is Version && compareTo(other) == 0

    override fun hashCode(): Int = Objects.hash(major, minor, patch, if (tag !== null) 1 else 0)

    fun stringfy(): String = "$major.$minor.$patch" + (if (tag !== null) "-$tag" else "") + (if (buildMetadata !== null) "+$buildMetadata" else "")
}