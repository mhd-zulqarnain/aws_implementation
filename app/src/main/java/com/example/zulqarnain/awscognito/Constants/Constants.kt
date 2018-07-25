package com.panacloud.joviparks.Constants

import com.amazonaws.regions.Regions

/**
 * Created by MuhammadFarhan on 18/07/2018.
 */
class Constants private constructor() {

    companion object {
        const val STATIC_MOVIES_BASE_URL = "https://"

        const val USER_POOL_ID = "us-east-1_C5VoGDmik"
        const val APP_CLIENT_ID = "bjgplsvn6neuhn3erlovrcro3"
        const val ACCESSS_KEY = "AKIAJR3WSRNWYQAW25OA"
        const val SECRETE_KEY = "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"
        val COGNITO_REGION = Regions.US_EAST_1
        val APP_SECRET = null
    }
}