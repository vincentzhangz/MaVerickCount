package com.vincentzhangz.maverickcount

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import com.vincentzhangz.maverickcount.utilities.SystemUtility
import com.vincentzhangz.maverickcount.utilities.ThemeManager
import kotlinx.android.synthetic.main.fragment_setting.view.*
import java.util.*


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

        rootView.language.adapter = arrayAdapter

        rootView.language.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                when (position) {
                    0 -> {
                        context?.let { updateResources(it, "en") }
                    }
                    1 -> {
                        context?.let { updateResources(it, "in") }
                    }
                }
            }

        }

        return rootView
    }


    fun updateResources(context: Context, language: String): Context {

        context.let { SystemUtility.toast(it, "Language change success.") }
        val locale = Locale(language)
        val configuration = context.resources.configuration
        val displayMetrics = context.resources.displayMetrics

        Locale.setDefault(locale)

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLocale(locale)
            context.createConfigurationContext(configuration)
        } else {
            configuration.locale = locale
            context.resources.updateConfiguration(configuration, displayMetrics)
            context
        }

    }
}
