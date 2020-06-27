package com.infinity_coder.divcalendar.presentation.settings.dialogs

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import com.infinity_coder.divcalendar.R
import com.infinity_coder.divcalendar.presentation._common.BottomDialog
import com.infinity_coder.divcalendar.presentation._common.delegate.AppThemeDelegate
import com.infinity_coder.divcalendar.presentation.settings.SettingsFragment
import com.infinity_coder.divcalendar.presentation.settings.SettingsViewModel
import kotlinx.android.synthetic.main.bottom_dialog_change_theme.*

class ChangeThemeBottomDialog : BottomDialog() {

    private lateinit var parentViewModel: SettingsViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (parentFragment is SettingsFragment) {
            parentViewModel = (parentFragment as SettingsFragment).viewModel
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setStyle(DialogFragment.STYLE_NORMAL, R.style.BottomDialogStyle)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.bottom_dialog_change_theme, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        parentViewModel.changeThemeTypeEvent.observe(viewLifecycleOwner, Observer { changeThemeType() })

        themeTypeRadioGroup.clearCheck()
        val radioButton = when (parentViewModel.getCurrentThemeType()) {
            AppCompatDelegate.MODE_NIGHT_NO -> darkThemeOffRadioButton
            AppCompatDelegate.MODE_NIGHT_YES -> darkThemeOnRadioButton
            AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM -> darkThemeAutomaticallyRadioButton
            else -> darkThemeAutomaticallyRadioButton
        }
        radioButton.isChecked = true

        themeTypeRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.darkThemeOffRadioButton -> parentViewModel.saveCurrentThemeType(AppCompatDelegate.MODE_NIGHT_NO)
                R.id.darkThemeOnRadioButton -> parentViewModel.saveCurrentThemeType(AppCompatDelegate.MODE_NIGHT_YES)
                R.id.darkThemeAutomaticallyRadioButton -> parentViewModel.saveCurrentThemeType(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            }
        }
    }

    private fun changeThemeType() {
        AppThemeDelegate.setAppTheme((requireActivity() as AppCompatActivity))
        dismiss()
    }

    companion object {
        const val TAG = "ChangePortfolioBottomDialog"

        fun newInstance(): ChangeThemeBottomDialog {
            return ChangeThemeBottomDialog()
        }
    }
}