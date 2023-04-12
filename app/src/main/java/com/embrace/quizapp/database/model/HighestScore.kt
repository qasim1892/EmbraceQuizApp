package com.embrace.quizapp.database.model

import io.realm.RealmObject

//Realm Model class
open class HighestScore(
    var highestScore: Int = 0
) : RealmObject()

