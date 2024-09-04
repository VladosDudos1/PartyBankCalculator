package vlados.dudos.party.bank.calculator.presentation.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import vlados.dudos.party.bank.calculator.R
import vlados.dudos.party.bank.calculator.app.App
import vlados.dudos.party.bank.calculator.databinding.FragmentStartBinding
import vlados.dudos.party.bank.calculator.presentation.fragment.base.BaseFragment


class StartFragment : BaseFragment() {

    private val binding: FragmentStartBinding by lazy { FragmentStartBinding.inflate(layoutInflater) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        goNextPage()
    }

    private fun goNextPage(){
        findNavController().navigate(R.id.action_startFragment_to_listEventFragment)
    }
}