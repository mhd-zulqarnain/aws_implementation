package com.example.zulqarnain.awscognito

import android.Manifest
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserCodeDeliveryDetails
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserAttributes
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.SignUpHandler
import com.amazonaws.services.cognitoidentityprovider.model.UserPoolClientDescription
import kotlinx.android.synthetic.main.activity_main.view.*
import java.util.*
import android.support.v4.app.ActivityCompat
import android.content.pm.PackageManager
import android.support.v4.content.ContextCompat
import android.support.annotation.NonNull
import android.widget.Button
import android.widget.EditText


class MainActivity : AppCompatActivity() {

    private val DEVICE_STORAGE_PERMISSION = 9
    val userAttributes: CognitoUserAttributes = CognitoUserAttributes()
    var userPool: CognitoUserPool? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        userPool = Auth.initCredentional(this)

        askPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, DEVICE_STORAGE_PERMISSION)

        var ed_email:EditText =findViewById(R.id.ed_email)
        var ed_pass:EditText =findViewById(R.id.ed_pass)
        var ed_type:EditText =findViewById(R.id.ed_email)
        var bt_sign_up:Button =findViewById(R.id.bt_sign_up)

        var email = ed_email.text.toString()
        var password = ed_pass.text.toString()
        var type = ed_type.text.toString()

        println("Data " + type + " " + password + " " + email)

        bt_sign_up.setOnClickListener {
            Toast.makeText(this, "Click", Toast.LENGTH_SHORT).show()

//            if (!name.equals("") && !pass.equals("") && !email.equals("")) {
            userAttributes.addAttribute("custom:type", type);
            // userAttributes.addAttribute("password", password);

            if (userAttributes != null) {
                userPool!!.signUpInBackground(email, password, userAttributes, null, signupCallback);
            }
            /*}else
                Toast.makeText(this, "Fill all field", Toast.LENGTH_SHORT).show()
*/
        }
    }


    val signupCallback = object : SignUpHandler {
        override fun onSuccess(user: CognitoUser?, signUpConfirmationState: Boolean, cognitoUserCodeDeliveryDetails: CognitoUserCodeDeliveryDetails?) {
            Toast.makeText(applicationContext, "Success", Toast.LENGTH_SHORT).show()

        }

        override fun onFailure(exception: java.lang.Exception?) {
            Toast.makeText(applicationContext, "failed", Toast.LENGTH_SHORT).show()
            println("error type " + exception)
        }

    }

    fun askPermission(permission: String, requestcode: Int) {
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(permission), requestcode)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {

        when (requestCode) {
            DEVICE_STORAGE_PERMISSION -> if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            // Messege.messege(baseContext, "Permission granted")
            else {
                ///Messege.messege(baseContext, "Application must have that permission")
                System.exit(0)
            }
        }
    }

}
