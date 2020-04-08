package com.vincentzhangz.maverickcount

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import com.vincentzhangz.maverickcount.utilities.ThemeManager
import kotlinx.android.synthetic.main.fragment_setting.view.*


class SettingFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_setting, container, false)
        // Inflate the layout for this fragment
        val themeManager = ThemeManager(rootView.context)
        rootView.dark_mode.isChecked = themeManager.loadTheme()

        rootView.dark_mode.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                themeManager.saveTheme(true)
            } else {
                themeManager.saveTheme(false)
            }

            themeManager.loadTheme()
            this.fragmentManager!!.beginTransaction().detach(this).attach(this).commit()
        }
        val languages = arrayOf("English", "Indonesia")
        val arrayAdapter = ArrayAdapter(
            activity!!.baseContext,
            android.R.layout.simple_spinner_dropdown_item, languages
        )
//        with(rootView.language) {
//            adapter = arrayAdapter
//        }

        rootView.language.adapter = arrayAdapter

//        rootView.language.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//            override fun onNothingSelected(parent: AdapterView<*>?) {
//                TODO("Not yet implemented")
//            }
//
//            override fun onItemSelected(
//                parent: AdapterView<*>?,
//                view: View?,
//                position: Int,
//                id: Long
//            ) {
//                TODO("Not yet implemented")
//                SystemUtility.toast(rootView.context, "Tst")
//            }
//
//        }
        return rootView
    }

}
