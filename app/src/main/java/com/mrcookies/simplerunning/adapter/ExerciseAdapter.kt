package com.mrcookies.simplerunning.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mrcookies.simplerunning.core.TrackingUtility
import com.mrcookies.simplerunning.data.model.Exercise
import com.mrcookies.simplerunning.databinding.ItemExerciseBinding
import java.text.SimpleDateFormat
import java.util.*

class ExerciseAdapter: RecyclerView.Adapter<ExerciseAdapter.ExerciseViewHolder>() {

    private var _binding : ItemExerciseBinding? = null
    private val binding get() = _binding

    inner class ExerciseViewHolder(binding: ItemExerciseBinding?) : RecyclerView.ViewHolder(binding!!.root)

    private val diffCallback = object : DiffUtil.ItemCallback<Exercise>(){
        override fun areItemsTheSame(oldItem: Exercise, newItem: Exercise): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Exercise, newItem: Exercise): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }

    private val differ = AsyncListDiffer(this,diffCallback)

    fun submitList(list : List<Exercise>){
        differ.submitList(list)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseViewHolder {
        _binding = ItemExerciseBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ExerciseViewHolder(_binding)
    }

    override fun onBindViewHolder(holder: ExerciseViewHolder, position: Int) {
        val currentExercise = differ.currentList[position]
        holder.itemView.apply {
            Glide.with(this).load(currentExercise.img).into(binding!!.imgMap)

            val calendar = Calendar.getInstance().apply {
                timeInMillis = currentExercise.timestamp
            }
            val dateFormat = SimpleDateFormat("dd/MM/yy hh:mm",Locale.getDefault())
            binding!!.txvDate.text = dateFormat.format(calendar.time)

            val avgSpeed = "${currentExercise.avgSpeed} km/h"
            binding!!.txvPace.text = avgSpeed

            val distance =  "%.2f".format(currentExercise.distance/1000)+"km"
            binding!!.txvDistance.text = distance

            binding!!.txvTime.text = TrackingUtility.getFormattedStopWatchTime(currentExercise.time)

            val calories = "${currentExercise.calories}"
            binding!!.txvCalories.text = calories
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

}