package vlados.dudos.party.bank.calculator.presentation.activity

import android.os.Bundle
import androidx.navigation.findNavController
import vlados.dudos.party.bank.calculator.R
import vlados.dudos.party.bank.calculator.databinding.ActivityMainBinding
import vlados.dudos.party.bank.calculator.presentation.activity.base.BaseActivity

class MainActivity : BaseActivity() {
    private val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }

    override fun navigate(action: Int) {
        super.navigate(action)
        binding.navMainFragment.findNavController().navigate(action)
    }
}