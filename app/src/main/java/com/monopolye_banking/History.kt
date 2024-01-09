package com.monopolye_banking

import android.R.attr.height
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [History.newInstance] factory method to
 * create an instance of this fragment.
 */
class History : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var view: View? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var v = inflater.inflate(R.layout.fragment_history, container, false)
        view = v
        return v
    }
    override fun onResume() {
        super.onResume()
        for (element in history) {
            if(element.contains("Added")) {
                var tmpText = TextView(context)
                tmpText.setText(element)
                tmpText.setLayoutParams(FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, resources.getDimensionPixelSize(R.dimen.history_list_item_height)))
                tmpText.gravity = Gravity.CENTER_VERTICAL
                tmpText.setPadding(10,0,0,0)
                tmpText.setBackgroundColor(resources.getColor(R.color.blue))
                tmpText.setTextColor(resources.getColor(R.color.white))
                view?.findViewById<LinearLayout>(R.id.historyList)?.addView(tmpText)
            }
            else if(element.contains("Subtracted")) {
                var tmpText = TextView(context)
                tmpText.setText(element)
                tmpText.setLayoutParams(FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, resources.getDimensionPixelSize(R.dimen.history_list_item_height)))
                tmpText.gravity = Gravity.CENTER_VERTICAL
                tmpText.setPadding(10,0,0,0)
                tmpText.setBackgroundColor(resources.getColor(R.color.red))
                tmpText.setTextColor(resources.getColor(R.color.white))
                view?.findViewById<LinearLayout>(R.id.historyList)?.addView(tmpText)
            }
            else if(element.contains("Landed on Go and got")) {
                var tmpText = TextView(context)
                tmpText.setText(element)
                tmpText.setLayoutParams(FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, resources.getDimensionPixelSize(R.dimen.history_list_item_height)))
                tmpText.gravity = Gravity.CENTER_VERTICAL
                tmpText.setPadding(10,0,0,0)
                tmpText.setBackgroundColor(resources.getColor(R.color.green))
                tmpText.setTextColor(resources.getColor(R.color.white))
                view?.findViewById<LinearLayout>(R.id.historyList)?.addView(tmpText)
            }
            else if(element.contains("Received") || element.contains("Sent")) {
                var tmpText = TextView(context)
                tmpText.setText(element)
                tmpText.setLayoutParams(FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, resources.getDimensionPixelSize(R.dimen.history_list_item_height)))
                tmpText.gravity = Gravity.CENTER_VERTICAL
                tmpText.setPadding(10,0,0,0)
                tmpText.setBackgroundColor(resources.getColor(R.color.yellow))
                tmpText.setTextColor(resources.getColor(R.color.black))
                view?.findViewById<LinearLayout>(R.id.historyList)?.addView(tmpText)
            }

        }
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment History.
         */
        var history = arrayOf("Game Started")
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            History().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}