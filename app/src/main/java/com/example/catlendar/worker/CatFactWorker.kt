package com.example.catlendar.worker

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.catlendar.network.RetrofitClient
import com.example.catlendar.util.NotificationHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CatFactWorker(
    private val context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        val backupFacts = listOf(
            "Purring doesn't always indicate happiness. It can also be a sign of pain or stress.",
            "Cats have a specialized collarbone that allows them to always land on their feet.",
            "A cat's brain is biologically more similar to a human brain than a dog's.",
            "Cats sleep for 70% of their lives.",
            "A group of cats is called a 'clowder'.",
            "Cats can rotate their ears 180 degrees.",
            "The oldest known pet cat was found in a 9,500-year-old grave on the Mediterranean island of Cyprus.",
            "An adult cat has 30 teeth."
        )
        
        val factResult = try {
            val response = RetrofitClient.apiService.getRandomFact()
            response.text
        } catch (e: Exception) {
            Log.e("CatFactWorker", "Failed to fetch cat fact from API, falling back.", e)
            backupFacts.random()
        }

        NotificationHelper.showCatFactNotification(context, factResult)

        Result.success()
    }
}
