package com.berasil.data.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.berasil.R
import com.berasil.data.local.entity.ResultDetection
import com.berasil.databinding.ResultItemBinding
import com.berasil.ui.history.detail.DetailHistoryActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions

class HistoryAdapter(
    private val context: Context,
    private val historyList: List<ResultDetection>
) :
    RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    inner class HistoryViewHolder(private val binding: ResultItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(resultDetection: ResultDetection) {
            resultDetection.url?.let { binding.ivResult.loadImage(it) }
            binding.tvCreatedAt.text = resultDetection.createdAt
            binding.tvQuality.text = resultDetection.quality

            when (resultDetection.quality) {
                "Premium" -> {
                    binding.tvQuality.setTextColor(ContextCompat.getColor(context, R.color.premium))
                    binding.tvQuality.backgroundTintList =
                        ContextCompat.getColorStateList(context, R.color.bg_premium)
                }

                "Medium 1" -> {
                    binding.tvQuality.setTextColor(
                        ContextCompat.getColor(
                            context,
                            R.color.medium_1
                        )
                    )
                    binding.tvQuality.backgroundTintList =
                        ContextCompat.getColorStateList(context, R.color.bg_medium_1)
                }

                "Medium 2" -> {
                    binding.tvQuality.setTextColor(
                        ContextCompat.getColor(
                            context,
                            R.color.medium_2
                        )
                    )
                    binding.tvQuality.backgroundTintList =
                        ContextCompat.getColorStateList(context, R.color.bg_medium_2)
                }

                else -> {
                    binding.tvQuality.setTextColor(ContextCompat.getColor(context, R.color.bawah))
                    binding.tvQuality.backgroundTintList =
                        ContextCompat.getColorStateList(context, R.color.bg_bawah)
                }
            }

            itemView.setOnClickListener { view ->
                val toDetailHistory = Intent(view.context, DetailHistoryActivity::class.java)
                toDetailHistory.putExtra(DetailHistoryActivity.EXTRA_HISTORY, resultDetection)
                view.context.startActivity(toDetailHistory)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val view = ResultItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HistoryViewHolder(view)
    }

    override fun getItemCount(): Int = historyList.size

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.bind(historyList[position])
    }

    fun ImageView.loadImage(url: String) {
        Glide.with(this.context)
            .load(url)
            .transition(DrawableTransitionOptions.withCrossFade())
            .centerCrop()
            .into(this)
    }
}