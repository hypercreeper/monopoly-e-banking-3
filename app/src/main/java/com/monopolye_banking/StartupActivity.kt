package com.monopolye_banking

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.wifi.WifiManager
import android.os.Bundle
import android.text.format.Formatter
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ContextThemeWrapper
import androidx.lifecycle.lifecycleScope
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.internal.ContextUtils.getActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Headers
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.util.Random


class StartupActivity : AppCompatActivity() {
    private val ALLOWED_CHARACTERS = "0123456789qwertyuiopasdfghjklzxcvbnm"
    companion object {
        var sysURL = "http://192.168.4.1:5000/"
        var id = ""
        var sharedPref: SharedPreferences? = null
        data class Device(
            var id: String,
            var name: String,
            val frontFacing: String = name + " - " + id
        )
        var listofusers = arrayListOf<Device>()
        var connectedUsers = arrayListOf<Device>()

        suspend fun getUsers(context: Context): ArrayList<Device> {
            withContext<ArrayList<Device>>(Dispatchers.IO) {
                var client = OkHttpClient()
                var request = Request.Builder()
                    .url(StartupActivity.sysURL + "getusers")
                    .method("GET", null)
                    .build()
                var returnVal: Array<String> = arrayOf()
                var response = client.newCall(request).execute()
                if (response.code == 200) {
                    val str = response.body?.string()
                    if (str != null) {
                        val arr = str.removePrefix("[").removeSuffix("]").split(",")
                            .map { it.trim('"') }
                            .toTypedArray()
                        var temp = arrayListOf<Device>()
                        for (item in arr) {
                            temp.add(Device(item, Main.getUserName(item).toString()))
                        }
                        return@withContext temp
                    } else {
                        return@withContext arrayListOf()
                    }
                } else {
                    return@withContext arrayListOf()
                }
            }
        }
    }

    private fun getRandomString(sizeOfRandomString: Int): String {
        val random = Random()
        val sb = StringBuilder(sizeOfRandomString)
        for (i in 0 until sizeOfRandomString)
            sb.append(ALLOWED_CHARACTERS[random.nextInt(ALLOWED_CHARACTERS.length)])
        return sb.toString()
    }
    private lateinit var context: StartupActivity
    private val debug: Boolean = false;
    fun initUserList() {
        var alertBuilder = MaterialAlertDialogBuilder(context)
        alertBuilder.setTitle("Select Users: ")
        listofusers = getUsers(context)
        var tempArray = arrayListOf<String>()
        for (item in listofusers) {
            tempArray.add(item.frontFacing)
        }
        alertBuilder.setMultiChoiceItems(tempArray.toTypedArray(), null) { dialog, which, isChecked ->
            // Do something.
            Log.i("listofusers", which.toString())
            Log.i("listofusers", isChecked.toString())
        }
        alertBuilder.setPositiveButton("OK") { dialog, id ->
            // Perform some action when OK button is clicked
            startActivity(
                Intent(
                    baseContext,
                    MainActivity::class.java
                )
            )
        }
        alertBuilder.setNeutralButton("Refresh") { dialog, id ->
            dialog.dismiss()
            initUserList()
        }
        runOnUiThread {
            alertBuilder.create().show()
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_startup)
        context = this
        sharedPref = getSharedPreferences("Monopoly E-Banking", Context.MODE_PRIVATE)
        findViewById<Button>(R.id.GetStartedButton).setOnClickListener {
            if(debug) {
                Main.goAmount = findViewById<EditText>(R.id.GoBox).text.toString().replace("$", "").toInt()
                Main.balance = findViewById<EditText>(R.id.StartingMoneyBox).text.toString().replace("$", "").toInt()
                startActivity(Intent(baseContext, MainActivity::class.java))
            }
            else {
                sysURL = "http://" + findViewById<EditText>(R.id.IPBox).text.toString() + ":5000/"
                val url = URL(sysURL + "connectioncheck")
                var client: OkHttpClient = OkHttpClient();
                var request = Request.Builder()
                    .url(url)
                    .method("GET", null)
                    .addHeader("ip","validip")
                    .build()
                try {
                    client.newCall(request).enqueue(object : Callback {
                        override fun onFailure(call: Call, e: IOException) {
                            var alertBuilder = MaterialAlertDialogBuilder(context)
                            alertBuilder.setTitle("Error")
                            alertBuilder.setIcon(R.drawable.baseline_error_outline_24)
                            alertBuilder.setMessage(e.message.toString())
//                            alert.setPositiveButton("OK") { dialog, id ->
//                                // Perform some action when OK button is clicked
//                            }
                            runOnUiThread {
                                alertBuilder.create().show()
                            }
                        }

                        override fun onResponse(call: Call, response: Response) {
                            if(response.code == 200) {
                                Main.goAmount = findViewById<EditText>(R.id.GoBox).text.toString().replace("$", "").toInt()
                                Main.balance = findViewById<EditText>(R.id.StartingMoneyBox).text.toString().replace("$", "").toInt()
                                id = sharedPref?.getString("id", "notfound").toString()
                                if(id == "notfound") {
                                    id = getRandomString(18)
                                    val editor = sharedPref?.edit()
                                    editor?.putString("id", id)
                                    editor?.apply()
                                }
                                var editText = EditText(context)
                                var alertBuilder = MaterialAlertDialogBuilder(context)
                                alertBuilder.setTitle("Complete Account")
                                alertBuilder.setMessage("Enter your name: ")
                                alertBuilder.setView(editText)
                                alertBuilder.setPositiveButton("Create Account") { dialog, which ->
                                    var client2: OkHttpClient = OkHttpClient();
                                    var request2 = Request.Builder()
                                        .url(sysURL + "createuser")
                                        .method(
                                            "POST",
                                            RequestBody.create(
                                                "application/json".toMediaType(),
                                                ""
                                            )
                                        )
                                        .addHeader("name", editText.text.toString())
                                        .addHeader("balance", findViewById<EditText>(R.id.StartingMoneyBox).text.toString().replace("$",""))
                                        .addHeader("id", id)
                                        .build()
                                    client2.newCall(request2).enqueue(object : Callback {
                                        override fun onFailure(call: Call, e: IOException) {
                                            var alertBuilder = MaterialAlertDialogBuilder(context)
                                            alertBuilder.setTitle("Error")
                                            alertBuilder.setIcon(R.drawable.baseline_error_outline_24)
                                            alertBuilder.setMessage(e.message.toString())
                                            alertBuilder.setPositiveButton("Ok", { dialog, which ->

                                            })
                                            runOnUiThread {
                                                alertBuilder.show()
                                            }
                                        }

                                        override fun onResponse(call: Call, response: Response) {
                                            if (response.code == 200) {
                                                initUserList()
                                            }
                                        }
                                    })
                                }
                                runOnUiThread {
                                    alertBuilder.show()
                                }
                            }
                        }
                    })
                }
                catch (e: IOException) {
                    var alertBuilder = MaterialAlertDialogBuilder(context)
                    alertBuilder.setTitle("Error")
                    alertBuilder.setIcon(R.drawable.baseline_error_outline_24)
                    alertBuilder.setMessage(e.message.toString())
//                            alert.setPositiveButton("OK") { dialog, id ->
//                                // Perform some action when OK button is clicked
//                            }
                    var alert = alertBuilder.create()
                    alert.show()
                }

            }
        }
    }
}