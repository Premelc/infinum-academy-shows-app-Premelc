package com.premelc.shows_dominik_premelc

import androidx.fragment.app.Fragment
import com.premelc.shows_dominik_premelc.db.ShowsDatabase
fun Fragment.getAppDatabase(): ShowsDatabase = (requireActivity().application as ShowApplication).database
