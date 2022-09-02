package com.eungb.cleanarchapp.presentation.main.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.eungb.cleanarchapp.databinding.FragmentMainAddBinding
import com.eungb.cleanarchapp.presentation.common.extension.showGenericAlertDialog
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class MainAddFragment : Fragment() {

    private var _binding: FragmentMainAddBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AddViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainAddBinding.inflate(inflater, container, false)
        binding.vm = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
        initObserve()
    }

    private fun initObserve() {
        viewModel.productNameLiveData.observe(viewLifecycleOwner) {
            checkSaveButton()
        }

        viewModel.productPriceLiveData.observe(viewLifecycleOwner) {
            checkSaveButton()
        }

        viewModel.state.flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
            .onEach { handleState(it) }
            .launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.event.flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
            .onEach {
                if (it is AddEvent.MoveBack) moveBack()
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun initUi() {
        binding.saveButton.setOnClickListener {
            viewModel.input.clickSave()
        }
    }

    private fun checkSaveButton() {
        binding.saveButton.isEnabled = viewModel.productName.isNotEmpty() && viewModel.productPrice.isNotEmpty()
    }

    private fun handleState(state: AddState) {
        when (state) {
            is AddState.Exception -> requireActivity().showGenericAlertDialog(state.message)
            is AddState.Error -> requireActivity().showGenericAlertDialog(state.error.message)
            is AddState.Success -> {
                Snackbar.make(binding.root, "Replace with your own action", Snackbar.LENGTH_LONG).show()
                viewModel.route.toBack()
            }
        }
    }

    private fun moveBack() {
        findNavController().popBackStack()
    }
}
