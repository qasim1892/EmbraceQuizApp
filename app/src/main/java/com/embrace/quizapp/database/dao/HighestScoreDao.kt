package com.embrace.quizapp.database.dao

import com.embrace.quizapp.database.model.HighestScore
import com.embrace.quizapp.utils.Utils
import io.realm.Realm
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HighestScoreDao {
    private val realm: Realm by lazy {
        Realm.getDefaultInstance()
    }

    fun updateScore(score: HighestScore) {
        CoroutineScope(Dispatchers.Main).launch {
           realm.where(HighestScore::class.java).max(Utils.HIGH_SCORE_FILED)
        }.invokeOnCompletion {
            realm.executeTransactionAsync {
                it.insertOrUpdate(score)
            }
        }
    }

    fun getHighScore(): Int? {
        return realm.where(HighestScore::class.java).max(Utils.HIGH_SCORE_FILED)?.toInt()
    }
}
