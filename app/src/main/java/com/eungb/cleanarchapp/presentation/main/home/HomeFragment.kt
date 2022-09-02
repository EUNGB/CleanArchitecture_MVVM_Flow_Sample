package com.eungb.cleanarchapp.presentation.main.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.eungb.cleanarchapp.databinding.FragmentHomeBinding
import com.eungb.cleanarchapp.domain.entity.ProductEntity
import com.eungb.cleanarchapp.presentation.common.extension.showGenericAlertDialog
import com.eungb.cleanarchapp.presentation.main.home.adapter.ProductListAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels()

    private lateinit var mAdapter: ProductListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.vm = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
        initAdapter()
        initObserve()
    }

    private fun initUi() {
        viewModel.input.onUpdateProducts()
    }

    private fun initAdapter() {
        mAdapter = ProductListAdapter()
        binding.rvProduct.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun initObserve() {
        viewModel.state.flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
            .onEach { handleState(it) }
            .launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.event.flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
            .onEach { handleEvent(it) }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun handleEvent(event: ProductEvent) {
        when (event) {
            is ProductEvent.MoveAdd -> {

            }
            is ProductEvent.UpdateProduct -> {

            }
            is ProductEvent.MoveDetail -> {

            }
        }
    }

    private fun handleState(state: ProductsState) {
        when (state) {
            is ProductsState.Exception -> showErrorDialog(state.message)
            is ProductsState.Error -> showErrorDialog(state.error.message)
            is ProductsState.Success -> setAllProducts(state.products)
            else -> Unit
        }
    }

    private fun showErrorDialog(message: String) {
        requireActivity().showGenericAlertDialog(message)
    }

    private fun setAllProducts(products: List<ProductEntity>) {
        binding.tvEmptyList.isVisible = products.isEmpty()
        mAdapter.setAllProducts(products)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}