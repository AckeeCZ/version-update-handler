package com.ackee.versionupdatehandler

import com.google.common.truth.Truth
import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test

/**
 * Tests for [RestVersionFetcher]
 *
 * @author Eduard Ablekimov [eduard.ablekimov@ackee.cz]
 * @since 09/06/2020
 */
class RestVersionFetcherTest {

    private lateinit var server: MockWebServer

    @Before
    fun setup() {
        server = MockWebServer().apply {
            start()
        }
    }

    @After
    fun cleanup() {
        server.shutdown()
    }

    @Test
    fun `should correctly parse response`() = runBlocking {
        // arrange
        val json = JsonObject()
        json.add(MINIMAL_VERSION, JsonPrimitive(10))
        json.add(CURRENT_VERSION, JsonPrimitive(15))

        server.enqueue(MockResponse().setBody(json.toString()))

        // act
        val restVersionFetcher = RestVersionFetcher("http://${server.hostName}:${server.port}")
        val result = restVersionFetcher.fetch()

        // assert
        Truth.assertThat(result.currentVersion())
            .isEqualTo(15)
        Truth.assertThat(result.minimalVersion())
            .isEqualTo(10)
    }

    @Test
    fun `should return default object if json is empty`() = runBlocking {
        // arrange
        server.enqueue(MockResponse().setBody(JsonObject().toString()))

        // act
        val restVersionFetcher = RestVersionFetcher("http://${server.hostName}:${server.port}")
        val result = restVersionFetcher.fetch()

        // assert
        Truth.assertThat(result.currentVersion())
            .isEqualTo(-1)
        Truth.assertThat(result.minimalVersion())
            .isEqualTo(-1)
    }

    @Test
    fun `should return correct values if only one parameter is present`() = runBlocking {
        // arrange
        val json = JsonObject()
        json.add(MINIMAL_VERSION, JsonPrimitive(10))

        server.enqueue(MockResponse().setBody(json.toString()))

        // act
        val restVersionFetcher = RestVersionFetcher("http://${server.hostName}:${server.port}")
        val result = restVersionFetcher.fetch()

        // assert
        Truth.assertThat(result.currentVersion())
            .isEqualTo(-1)
        Truth.assertThat(result.minimalVersion())
            .isEqualTo(10)
    }

    companion object {
        const val MINIMAL_VERSION = "minimal_version_android"
        const val CURRENT_VERSION = "current_version_android"
    }
}