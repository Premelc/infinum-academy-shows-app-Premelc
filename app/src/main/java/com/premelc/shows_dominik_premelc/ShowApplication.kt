package com.premelc.shows_dominik_premelc

import android.app.Application
import com.premelc.shows_dominik_premelc.db.ShowsDatabase

class ShowApplication : Application() {
    val database by lazy {
        ShowsDatabase.getDatabase(this)
    }
}