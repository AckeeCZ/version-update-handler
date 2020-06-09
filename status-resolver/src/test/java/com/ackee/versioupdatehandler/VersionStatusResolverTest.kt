package com.ackee.versioupdatehandler

import com.ackee.versioupdatehandler.model.BasicVersionsConfiguration
import com.ackee.versioupdatehandler.model.VersionStatus
import com.ackee.versioupdatehandler.model.VersionsConfiguration
import com.google.common.truth.Truth
import org.junit.Test
import kotlinx.coroutines.runBlocking

/**
 * Tests for [VersionStatusResolver]
 *
 * @author David Bilik [david.bilik@ackee.cz]
 * @since 06/02/2017
 */
class VersionStatusResolverTest {

    companion object {
        val BASIC_VERSIONS_CONFIGURATION = BasicVersionsConfiguration(10, 15)
    }

    @Test
    fun should_be_up_to_date_greater_than_last() {
        checkSuccess(15, VersionStatus.UP_TO_DATE)
    }

    @Test
    fun should_be_up_to_date_same_as_last() {
        checkSuccess(20, VersionStatus.UP_TO_DATE)
    }

    @Test
    fun should_be_mandatory_update() {
        checkSuccess(8, VersionStatus.UPDATE_REQUIRED)
    }

    @Test
    fun should_be_not_mandatory_update() {
        checkSuccess(13, VersionStatus.UPDATE_AVAILABLE)
    }

    private fun checkSuccess(actualVersion: Int, expectedResult: VersionStatus) = runBlocking {
        val resolver = VersionStatusResolver( object : VersionFetcher {
            override suspend fun fetch(): VersionsConfiguration {
                return BASIC_VERSIONS_CONFIGURATION
            }
        })
        val versionStatus = resolver.checkVersionStatus(actualVersion)
        Truth.assertThat(versionStatus)
            .isEqualTo(expectedResult)
    }
}