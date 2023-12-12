package com.berasil.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.berasil.data.adapter.HistoryAdapter
import com.berasil.data.local.entity.ResultDetection
import com.berasil.databinding.FragmentHistoryBinding
import com.berasil.ui.BerasilViewModelFactory

class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!

    private val historyViewModel by viewModels<HistoryViewModel> {
        BerasilViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvResult.layoutManager = LinearLayoutManager(requireContext())

        historyViewModel.getAllResult().observe(viewLifecycleOwner) { result ->
            if (result != null) {
                val histories = arrayListOf<ResultDetection>()
                result.map {
                    val history = ResultDetection(
                        it.id,
                        it.quality,
                        it.createdAt,
                        it.butirKepala,
                        it.butirPatah,
                        it.butirMenir,
                        it.butirMerah,
                        it.butirRusak,
                        it.butirKapur,
                        it.butirGabah,
                        it.sekam,
                        it.kutu,
                        it.batu,
                        it.total,
                        it.url
                    )
                    histories.add(history)
                }
                binding.rvResult.adapter = HistoryAdapter(requireContext(), histories)
            }

            if (result.isEmpty()) {
                binding.historyEmpty.visibility = View.VISIBLE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}