package vlados.dudos.party.bank.calculator.presentation.fragment

import android.animation.LayoutTransition
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import vlados.dudos.domain.utils.ActionHolder.setActionId
import vlados.dudos.domain.utils.MapHolder.getMapOfValues
import vlados.dudos.party.bank.calculator.R
import vlados.dudos.party.bank.calculator.app.App
import vlados.dudos.party.bank.calculator.databinding.FragmentSettingsBinding
import vlados.dudos.party.bank.calculator.interfaces.INavigateChange
import vlados.dudos.party.bank.calculator.presentation.adapter.LanguageAdapter
import vlados.dudos.party.bank.calculator.presentation.adapter.ValueAdapter
import vlados.dudos.party.bank.calculator.presentation.fragment.base.BaseFragment
import vlados.dudos.party.bank.calculator.presentation.viewmodel.HostViewModel
import vlados.dudos.party.bank.calculator.presentation.viewmodel.SettingsViewModel

class SettingsFragment : BaseFragment(), INavigateChange, LanguageAdapter.OnClick, ValueAdapter.OnClick {

    override fun clickLanguage(language: String) {
        App.settingsManager.setLanguage(language, activity())
        changeViewVisibility(binding.languageRecycler)
    }

    override fun clickValue(value: String) {
        App.sharedManager.setBaseValue(value)
        changeViewVisibility(binding.valueRecycler)
    }

    private val binding: FragmentSettingsBinding by lazy {
        FragmentSettingsBinding.inflate(
            layoutInflater
        )
    }

    private val viewModel: SettingsViewModel by viewModels()
    private val hostViewModel: HostViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        putNavigateId()
        setupUi()
    }

    override fun applyClick() {
        with(binding) {
            valueCard.setOnClickListener {
                setValue()
            }
            languageCard.setOnClickListener {
                setLanguage()
            }
            lightThemeBtn.setOnClickListener {
                setLightTheme()
            }
            darkThemeBtn.setOnClickListener {
                setDarkTheme()
            }
            goBackLayout.setOnClickListener {
                activity().onBackPressed()
            }
        }
    }

    private fun setDarkTheme() {
        val currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        when (currentNightMode) {
            Configuration.UI_MODE_NIGHT_NO -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                App.settingsManager.saveThemePreference(AppCompatDelegate.MODE_NIGHT_YES)
            }
        }
    }

    private fun setLightTheme() {
        val currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        when (currentNightMode) {
            Configuration.UI_MODE_NIGHT_YES -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                App.settingsManager.saveThemePreference(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }

    private fun setLanguage() {
        with(binding){
            changeViewVisibility(languageRecycler)
        }
    }

    private fun setValue() {
        with(binding){
            changeViewVisibility(valueRecycler)
        }
    }

    override fun updateUi() {
        with(binding){
            languageName.text = getString(App.settingsManager.getLanguageName())
            valueName.text = App.sharedManager.getBaseValue()
        }
    }

    private fun setupUi() {
        with(binding) {
            toggleGroup.check(if (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK ==
                Configuration.UI_MODE_NIGHT_YES) R.id.darkThemeBtn else R.id.lightThemeBtn)
            languageLayout.layoutTransition = LayoutTransition().apply {
                enableTransitionType(LayoutTransition.CHANGING)
            }
            valueLayout.layoutTransition = LayoutTransition().apply {
                enableTransitionType(LayoutTransition.CHANGING)
            }
            languageName.text = getString(App.settingsManager.getLanguageName())
            valueName.text = App.sharedManager.getBaseValue()
        }
    }
    private fun changeViewVisibility(view: View) {
        view.visibility = if (view.visibility == View.GONE) View.VISIBLE else View.GONE
        updateUi()
    }

    override fun setAdapter() {
        with(binding){
            languageRecycler.layoutManager = LinearLayoutManager(context())
            languageRecycler.adapter = LanguageAdapter(context(), App.settingsManager.getMapOfLanguages(), this@SettingsFragment)
            valueRecycler.layoutManager = LinearLayoutManager(context())
            valueRecycler.adapter = ValueAdapter(context(), getMapOfValues(context()), this@SettingsFragment)
        }
    }

    override fun putNavigateId() {
        setActionId(R.id.action_settingsFragment_to_listEventFragment)
    }
}