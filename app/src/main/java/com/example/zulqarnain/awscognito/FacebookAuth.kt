package com.example.zulqarnain.awscognito

import android.content.Intent
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import android.util.Log
import android.view.View
import com.amazonaws.auth.CognitoCachingCredentialsProvider
import com.amazonaws.mobileconnectors.cognitoidentityprovider.*
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationContinuation
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationDetails
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.ChallengeContinuation
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.MultiFactorAuthenticationContinuation
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.AuthenticationHandler
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.SignUpHandler
import com.amazonaws.regions.Regions
import com.amazonaws.services.cognitoidentity.AmazonCognitoIdentity
import com.amazonaws.services.cognitoidentity.AmazonCognitoIdentityClient
import com.amazonaws.services.cognitoidentityprovider.AmazonCognitoIdentityProviderClient
import com.amazonaws.services.cognitoidentityprovider.model.GetUserRequest
import com.amazonaws.services.cognitosync.AmazonCognitoSyncClient
import com.facebook.*
import com.facebook.login.LoginResult
import com.facebook.login.LoginManager
import com.panacloud.joviparks.Constants.Constants
import kotlinx.android.synthetic.main.activity_facebook_auth.*
import java.lang.Exception
import java.util.*


class FacebookAuth : AppCompatActivity() {
    private var callbackManager: CallbackManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_facebook_auth)
        callbackManager = CallbackManager.Factory.create()
        FacebookSdk.sdkInitialize(this)
        btnLoginFacebook.setOnClickListener(object :View.OnClickListener{
            override fun onClick(p0: View?) {
                setUpFacebookSignIn()
            }

        })

    }

    private fun setUpFacebookSignIn() {

        callbackManager = CallbackManager.Factory.create()

        LoginManager.getInstance().logInWithReadPermissions(this@FacebookAuth, Arrays.asList( "public_profile","email"))

        LoginManager.getInstance().registerCallback(callbackManager, object : FacebookCallback<LoginResult> {

            override fun onSuccess(loginResult: LoginResult) {
                Log.e("TAG", "onSuccess" + loginResult.accessToken)
                //loginResult.accessToken
               handleFacebookLogin()
            }

            override fun onCancel() {

            }

            override fun onError(error: FacebookException) {
                println("Error  caused "+error)
            }
        })
    }

    private var token: String?=""

    private fun handleFacebookLogin() {


//        val logins: HashMap<String, String> = hashMapOf()
//        logins.put("graph.facebook.com", AccessToken.getCurrentAccessToken().token)
        Log.e("TAG", "Fb Token: " + AccessToken.getCurrentAccessToken().token)

        token = AccessToken.getCurrentAccessToken().token
        CreateIdentityTask().execute(AccessToken.getCurrentAccessToken().token)

    }
    inner class CreateIdentityTask() : AsyncTask<String, Integer, CognitoCachingCredentialsProvider>() {

        // Initializing the CredentialsProvider    *****Missing Identity Pool Id

        private var credentialsProvider: CognitoCachingCredentialsProvider = CognitoCachingCredentialsProvider(
                this@FacebookAuth,
                "us-east-1:8cc8f25e-cf33-4860-9f1a-694e8856690f",
                Regions.US_EAST_1)

        override fun doInBackground(vararg params:  String): CognitoCachingCredentialsProvider {
            Log.e("TAG", "Runs doInBackground")


           // credentialsProvider.logins = logins
            val identity = credentialsProvider.identityId
            val logins: HashMap<String, String> = hashMapOf()
            logins.put("graph.facebook.com", token!!)

            credentialsProvider.logins=logins

            credentialsProvider.refresh()


            if (identity != null && !identity.isEmpty()) {

                Log.e("TAG", "identity True $identity")

            }

//

            return credentialsProvider
        }

        override fun onPostExecute(result: CognitoCachingCredentialsProvider?) {
            super.onPostExecute(result)
                // We successfully logged in... Can bo back to main activity
                Toast.makeText(this@FacebookAuth, "FaceBook signIn Successfully",Toast.LENGTH_SHORT).show()
                println("the success result "+result)

           getUserSession(result)




        }
    }

    fun getUserSession(result: CognitoCachingCredentialsProvider?) {

        val amazonCognitoIdentityClient = AmazonCognitoSyncClient(result?.credentials)

        var cognitoUserPool = CognitoUserPool(this@FacebookAuth, Constants.USER_POOL_ID, Constants.APP_CLIENT_ID, Constants.APP_SECRET, Constants.COGNITO_REGION)


        val user2 = cognitoUserPool.getUser(result!!.identityId)

        user2.getSessionInBackground(object :AuthenticationHandler{
            override fun onSuccess(userSession: CognitoUserSession?, newDevice: CognitoDevice?) {

                println("success user session result ")

            }

            override fun onFailure(exception: Exception?) {
                println("failed result "+exception)

            }

            override fun getAuthenticationDetails(authenticationContinuation: AuthenticationContinuation?, userId: String?) {
                println("AuthenticationDetails user session result ")


                val authenticationDetails = AuthenticationDetails(userId,"",null)

                // Pass the user sign-in credentials to the continuation
                authenticationContinuation!!.setAuthenticationDetails(authenticationDetails)

                // Allow the sign-in to continue
                authenticationContinuation!!.continueTask()

            }

            override fun authenticationChallenge(continuation: ChallengeContinuation?) {
                println("success uthenticationChallenge ")

            }

            override fun getMFACode(continuation: MultiFactorAuthenticationContinuation?) {
                println("getMFACode user session result ")

            }
        })



    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager!!.onActivityResult(requestCode, resultCode, data)

        super.onActivityResult(requestCode, resultCode, data)
    }


}
