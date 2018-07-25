package com.example.zulqarnain.awscognito

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import kotlinx.android.synthetic.main.activity_facebook_auth.*
import java.util.*
import android.widget.Toast
import com.facebook.GraphResponse
import org.json.JSONObject
import com.facebook.GraphRequest
import com.facebook.AccessToken



class FacebookSample : AppCompatActivity() {

    private var callbackManager: CallbackManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_facebook_auth)
        callbackManager = CallbackManager.Factory.create()
        FacebookSdk.sdkInitialize(this)


        val fbAccessToken = AccessToken.getCurrentAccessToken()
        if (fbAccessToken != null) {
           // setFacebookSession(fbAccessToken)
            println("already sign in")
            graphRequest(AccessToken.getCurrentAccessToken());

            // btnLoginFacebook.setVisibility(View.GONE)
        }


        btnLoginFacebook.setOnClickListener(object :View.OnClickListener{
            override fun onClick(p0: View?) {
                setUpFacebookSignIn()
            }

        })

    }
    fun setUpFacebookSignIn(){

        LoginManager.getInstance().logInWithReadPermissions(this@FacebookSample, Arrays.asList( "public_profile","email"))

        LoginManager.getInstance().registerCallback(callbackManager, object : FacebookCallback<LoginResult> {

            override fun onSuccess(loginResult: LoginResult) {
                Log.e("TAG", "onSuccess" + loginResult.accessToken)
                graphRequest(loginResult.getAccessToken());
            }

            override fun onCancel() {

            }

            override fun onError(error: FacebookException) {
                println("Error  caused "+error)
            }
        })
    }

    fun graphRequest(token: AccessToken) {

        val request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(),object:GraphRequest.GraphJSONObjectCallback{
            override fun onCompleted(`object`: JSONObject?, response: GraphResponse?) {

                if(response!=null){
                    println("respnse  "+response.toString())
                }else{
                    println("respnse  null" )

                }
            }
        })
        var b = Bundle();
        b.putString("fields", "id,email,first_name,last_name,picture.type(large)");
        request.setParameters(b)
        request.executeAsync()
//        val request = GraphRequest.newMeRequest(token) { `object`, response ->
//        val request = GraphRequest.newMeRequest(token) {
//            try {
//                //                    textView.setText(object.toString());\
//
//                println("response  "+response)
//
//
//            } catch (e: Exception) {
//                println("Exception  facebook "+e)
//
//            }
////        }




    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager!!.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }
}
