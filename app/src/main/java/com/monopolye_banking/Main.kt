package com.monopolye_banking

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.core.text.set
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import java.io.IOException
import java.net.URL

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Main.newInstance] factory method to
 * create an instance of this fragment.
 */
class Main : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }
    fun updateBalanceBox(v: View) {
        v.findViewById<EditText>(R.id.BalanceBox).setText("$" + balance + "\n$" + NUMBER)
    }
    fun toggleActionButtons(v: View, state: Boolean) {
        if(state) {
            v.findViewById<Button>(R.id.SendBtn).isEnabled = true;
            v.findViewById<Button>(R.id.SendBtn).setBackgroundColor(resources.getColor(R.color.yellow))
            v.findViewById<Button>(R.id.SendBtn).setTextColor(resources.getColor(R.color.black))
            v.findViewById<Button>(R.id.AddBtn).isEnabled = true;
            v.findViewById<Button>(R.id.AddBtn).setBackgroundColor(resources.getColor(R.color.blue))
            v.findViewById<Button>(R.id.MinusBtn).isEnabled = true;
            v.findViewById<Button>(R.id.MinusBtn).setBackgroundColor(resources.getColor(R.color.red))
        }
        else {
            v.findViewById<Button>(R.id.SendBtn).isEnabled = false;
            v.findViewById<Button>(R.id.SendBtn).setBackgroundColor(resources.getColor(R.color.lightgray))
            v.findViewById<Button>(R.id.SendBtn).setTextColor(resources.getColor(R.color.black))
            v.findViewById<Button>(R.id.AddBtn).isEnabled = false;
            v.findViewById<Button>(R.id.AddBtn).setBackgroundColor(resources.getColor(R.color.lightgray))
            v.findViewById<Button>(R.id.MinusBtn).isEnabled = false;
            v.findViewById<Button>(R.id.MinusBtn).setBackgroundColor(resources.getColor(R.color.lightgray))
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var v = inflater.inflate(R.layout.fragment_main, container, false)
        toggleActionButtons(v, false)
        updateBalanceBox(v)
        v.findViewById<Button>(R.id.btn1).setOnClickListener {
            toggleActionButtons(v, true)
            NUMBER += v.findViewById<Button>(it.id).text
            updateBalanceBox(v)
        }
        v.findViewById<Button>(R.id.btn2).setOnClickListener {
            toggleActionButtons(v, true)
            NUMBER += v.findViewById<Button>(it.id).text
            updateBalanceBox(v)
        }
        v.findViewById<Button>(R.id.btn3).setOnClickListener {
            toggleActionButtons(v, true)
            NUMBER += v.findViewById<Button>(it.id).text
            updateBalanceBox(v)
        }
        v.findViewById<Button>(R.id.btn4).setOnClickListener {
            toggleActionButtons(v, true)
            NUMBER += v.findViewById<Button>(it.id).text
            updateBalanceBox(v)
        }
        v.findViewById<Button>(R.id.btn5).setOnClickListener {
            toggleActionButtons(v, true)
            NUMBER += v.findViewById<Button>(it.id).text
            updateBalanceBox(v)
        }
        v.findViewById<Button>(R.id.btn6).setOnClickListener {
            toggleActionButtons(v, true)
            NUMBER += v.findViewById<Button>(it.id).text
            updateBalanceBox(v)
        }
        v.findViewById<Button>(R.id.btn7).setOnClickListener {
            toggleActionButtons(v, true)
            NUMBER += v.findViewById<Button>(it.id).text
            updateBalanceBox(v)
        }
        v.findViewById<Button>(R.id.btn8).setOnClickListener {
            toggleActionButtons(v, true)
            NUMBER += v.findViewById<Button>(it.id).text
            updateBalanceBox(v)
        }
        v.findViewById<Button>(R.id.btn9).setOnClickListener {
            toggleActionButtons(v, true)
            NUMBER += v.findViewById<Button>(it.id).text
            updateBalanceBox(v)
        }
        v.findViewById<Button>(R.id.btn0).setOnClickListener {
            toggleActionButtons(v, true)
            NUMBER += v.findViewById<Button>(it.id).text
            updateBalanceBox(v)
        }
//        TODO: Button Functions, update plus minus buttons to confirm other players
        v.findViewById<Button>(R.id.ClearBtn).setOnClickListener {
            NUMBER = ""
            updateBalanceBox(v)
            toggleActionButtons(v, false)
        }
        v.findViewById<Button>(R.id.AddBtn).setOnClickListener {
            balance += NUMBER.toInt()
            History.history += "Added $" + NUMBER
            NUMBER = ""
            updateBalanceBox(v)
            modifiyBalance(this.requireContext(), balance)
            toggleActionButtons(v, false)
        }
        v.findViewById<Button>(R.id.MinusBtn).setOnClickListener {
            balance -= NUMBER.toInt()
            History.history += "Subtracted $" + NUMBER
            NUMBER = ""
            updateBalanceBox(v)
            modifiyBalance(this.requireContext(), balance)
            toggleActionButtons(v, false)
        }
        v.findViewById<Button>(R.id.GoBtn).setOnClickListener {
            balance += goAmount.toInt()
            History.history += "Landed on Go and got $" + goAmount
            NUMBER = ""
            updateBalanceBox(v)
            modifiyBalance(this.requireContext(), balance)
            toggleActionButtons(v, false)
        }
        v.findViewById<Button>(R.id.SendBtn).setOnClickListener {
            startActivity(Intent(context, SendMoney::class.java))
        }

        return v
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Main.
         */
        var NUMBER = "";
        var balance = 15000;
        var goAmount = 2000;

        fun modifiyBalance(context: android.content.Context, balance: Int): Boolean {
            var client: OkHttpClient = OkHttpClient();
            var request = Request.Builder()
                .url(StartupActivity.sysURL + "setbalance")
                .method("POST", RequestBody.create(
                    "application/json".toMediaType(),
                    ""
                ))
                .addHeader("id", StartupActivity.id)
                .addHeader("balance", balance.toString() )
                .build()
            var success = false
            try {

                client.newCall(request).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        var alertBuilder = MaterialAlertDialogBuilder(context)
                        alertBuilder.setTitle("Error")
                        alertBuilder.setIcon(R.drawable.baseline_error_outline_24)
                        alertBuilder.setMessage(e.message.toString())
                        alertBuilder.setPositiveButton("OK") { dialog, id ->
                            // Perform some action when OK button is clicked
                        }
                        alertBuilder.create().show()
                        success = false
                    }

                    override fun onResponse(call: Call, response: Response) {
                        if(response.code == 200) {
                            success = true
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
            return success
        }
        fun addCard(context: android.content.Context, cardID: String): Boolean {
            var client: OkHttpClient = OkHttpClient();
            var request = Request.Builder()
                .url(StartupActivity.sysURL + "setbalance")
                .method("POST", RequestBody.create(
                    "application/json".toMediaType(),
                    ""
                ))
                .addHeader("userID", StartupActivity.id)
                .addHeader("cardID", cardID)
                .build()
            var success = false
            try {

                client.newCall(request).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        var alertBuilder = MaterialAlertDialogBuilder(context)
                        alertBuilder.setTitle("Error")
                        alertBuilder.setIcon(R.drawable.baseline_error_outline_24)
                        alertBuilder.setMessage(e.message.toString())
                        alertBuilder.setPositiveButton("OK") { dialog, id ->
                            // Perform some action when OK button is clicked
                        }
                        alertBuilder.create().show()
                        success = false
                    }

                    override fun onResponse(call: Call, response: Response) {
                        if(response.code == 200) {
                            success = true
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
            return success
        }
        fun getUserName(userID: String): String? {
            var client = OkHttpClient()
            var request = Request.Builder()
                .url(StartupActivity.sysURL + "getusername")
                .addHeader("userID", userID)
                .method("GET", null)
                .build()
            val response = client.newCall(request).execute();
            return response.body?.string()
        }

        fun sendMoney(context: android.content.Context, receiverID: String, amount: Int): Boolean {
            var client: OkHttpClient = OkHttpClient();
            var request = Request.Builder()
                .url(StartupActivity.sysURL + "sendmoney")
                .method("POST", RequestBody.create(
                    "application/json".toMediaType(),
                    ""
                ))
                .addHeader("userID", StartupActivity.id)
                .addHeader("receiverID", receiverID)
                .addHeader("amount", amount.toString())
                .build()
            var success = false
            try {

                client.newCall(request).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        var alertBuilder = MaterialAlertDialogBuilder(context)
                        alertBuilder.setTitle("Error")
                        alertBuilder.setIcon(R.drawable.baseline_error_outline_24)
                        alertBuilder.setMessage(e.message.toString())
                        alertBuilder.setPositiveButton("OK") { dialog, id ->
                            // Perform some action when OK button is clicked
                        }
                        alertBuilder.create().show()
                        success = false
                    }

                    override fun onResponse(call: Call, response: Response) {
                        if(response.code == 200) {
                            var alertBuilder = MaterialAlertDialogBuilder(context)
                            alertBuilder.setTitle("Success")
                            alertBuilder.setIcon(R.drawable.baseline_error_outline_24)
                            alertBuilder.setMessage("Sent $" + amount + "to" + getUserName(receiverID))
                            alertBuilder.setPositiveButton("OK") { dialog, id ->
                                // Perform some action when OK button is clicked
                            }
                            alertBuilder.create().show()
                            success = true
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
            return success
        }

        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Main().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}