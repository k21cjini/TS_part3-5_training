package com.example

import android.app.Application
import com.example.data.AppDatabase
import com.example.data.SentenceRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class EngSpeakApplication : Application() {
    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    val database by lazy { AppDatabase.getDatabase(this, applicationScope) }
    val repository by lazy {
        SentenceRepository(database.sentenceDao(), database.historyDao())
    }

    override fun onCreate() {
        super.onCreate()
        applicationScope.launch(Dispatchers.IO) {
            repository.ensureDefaultSentencesLoaded()
        }
    }
}
