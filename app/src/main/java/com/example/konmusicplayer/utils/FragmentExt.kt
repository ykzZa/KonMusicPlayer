package com.example.konmusicplayer.utils

import android.os.Build
import android.util.Log
import androidx.fragment.app.Fragment

fun Fragment.recreateFragment() {
    val fragment = this
    Log.i("FRG", this.toString())
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        parentFragmentManager.beginTransaction().detach(fragment).commitNow()
        parentFragmentManager.beginTransaction().attach(fragment).commitNow()
    } else {
        parentFragmentManager.beginTransaction().detach(fragment).attach(fragment).commitNow()
    }
}
