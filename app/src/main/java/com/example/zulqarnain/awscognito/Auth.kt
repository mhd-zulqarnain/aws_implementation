package com.example.zulqarnain.awscognito

import android.content.Context

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool
import com.amazonaws.regions.Regions


class Auth{


    companion object {
        val userPoolId = "us-east-1_C5VoGDmik"
        val clientId = "bjgplsvn6neuhn3erlovrcro3"
        val cognitoRegion = Regions.US_EAST_1
        val clientSecret = null

        fun  initCredentional(ctx:Context):CognitoUserPool{
                val userPool = CognitoUserPool(ctx, userPoolId, clientId, clientSecret, cognitoRegion)
            return userPool
        }
    }


}