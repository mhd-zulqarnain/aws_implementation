package com.example.zulqarnain.awscognito

import android.content.Context

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool
import com.amazonaws.regions.Regions


class Auth{


    companion object {
        val userPoolId = "CLINT_ID"
        val clientId = "CLINT_KEY"
        val cognitoRegion = Regions.US_EAST_1
        val clientSecret = null

        fun  initCredentional(ctx:Context):CognitoUserPool{
                val userPool = CognitoUserPool(ctx, userPoolId, clientId, clientSecret, cognitoRegion)
            return userPool
        }
    }


}
