package com.sidspace.stary.logger

import android.util.Log
import com.sidspace.stary.domain.Logger


class StaryLogger : Logger {
    override fun d(tag: String, message: String) {
        Log.d(tag, message)
    }

    override fun e(tag: String, message: String) {
        Log.e(tag, message)
    }

}
