package com.embrace.quizapp.application

import android.app.Application
import com.embrace.quizapp.injection.apiModule
import com.embrace.quizapp.injection.appModule
import com.embrace.quizapp.injection.repositoryModule
import com.embrace.quizapp.injection.viewModelModule
import io.realm.Realm
import io.realm.RealmConfiguration
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class QuizApplication : Application() {

    private val schemaVersion = 1L

    override fun onCreate() {
        super.onCreate()
        Realm.init(this)  //init realmdb this covers all use of realm within the project.
        // Set the default Realm database name
        val config: RealmConfiguration by lazy {
            RealmConfiguration.Builder()
                .schemaVersion(schemaVersion)
                .allowWritesOnUiThread(true)
                .deleteRealmIfMigrationNeeded()
                .build()
        }

        Realm.setDefaultConfiguration(config)

        startKoin {
            androidContext(this@QuizApplication)
            modules(
                listOf(
                    appModule, repositoryModule, viewModelModule, apiModule
                )
            )
        }
    }
}