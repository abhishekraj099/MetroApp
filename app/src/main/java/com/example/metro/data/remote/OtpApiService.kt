package com.example.metro.data.remote

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.util.concurrent.TimeUnit

class OtpApiService {

    private val client = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    private val baseUrl = "https://new-otp-sand.vercel.app/api/auth"
    private val jsonMediaType = "application/json".toMediaType()

    suspend fun sendOtp(email: String): Result<String> {
        return try {
            val jsonBody = JSONObject().apply { put("email", email) }
            val requestBody = jsonBody.toString().toRequestBody(jsonMediaType)
            val request = Request.Builder()
                .url("$baseUrl/send-otp")
                .post(requestBody)
                .addHeader("Content-Type", "application/json")
                .build()

            val response = client.newCall(request).execute()
            response.use { resp ->
                val responseBody = resp.body?.string() ?: "{}"
                if (resp.code == 200) {
                    val json = JSONObject(responseBody)
                    val success = json.optBoolean("success", false)
                    val message = json.optString("message", "OTP sent")
                    if (success) Result.success(message)
                    else Result.failure(Exception(message))
                } else {
                    Result.failure(Exception("Server error: ${resp.code}"))
                }
            }
        } catch (e: Exception) {
            Result.failure(Exception("Network error: ${e.message}"))
        }
    }

    suspend fun verifyOtp(email: String, otp: String): Result<String> {
        return try {
            val jsonBody = JSONObject().apply {
                put("email", email)
                put("otp", otp)
            }
            val requestBody = jsonBody.toString().toRequestBody(jsonMediaType)
            val request = Request.Builder()
                .url("$baseUrl/verify-otp")
                .post(requestBody)
                .addHeader("Content-Type", "application/json")
                .build()

            val response = client.newCall(request).execute()
            response.use { resp ->
                val responseBody = resp.body?.string() ?: "{}"
                if (resp.code == 200) {
                    val json = JSONObject(responseBody)
                    val success = json.optBoolean("success", false)
                    if (success) Result.success("Login successful!")
                    else Result.failure(Exception("Invalid OTP"))
                } else {
                    Result.failure(Exception("Server error: ${resp.code}"))
                }
            }
        } catch (e: Exception) {
            Result.failure(Exception("Network error: ${e.message}"))
        }
    }
}

