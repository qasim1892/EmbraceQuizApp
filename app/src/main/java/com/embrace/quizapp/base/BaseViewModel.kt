package com.embrace.quizapp.base

import androidx.lifecycle.ViewModel
import com.embrace.quizapp.commons.CoroutineContextProvider
import kotlinx.coroutines.Job
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

@OptIn(KoinApiExtension::class)
abstract class BaseViewModel : ViewModel(), KoinComponent {

    val coroutineContextProvider by inject<CoroutineContextProvider>()
    private val apiCallJobs = mutableListOf<Job>()

    override fun onCleared() {
        super.onCleared()
        apiCallJobs.forEach { it.cancel() }
    }
}