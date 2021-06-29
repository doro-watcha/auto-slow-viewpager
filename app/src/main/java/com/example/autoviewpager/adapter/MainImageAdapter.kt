package com.example.autoviewpager.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewGroup
import androidx.databinding.library.baseAdapters.BR
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.autoviewpager.databinding.ItemMainImageBinding
import com.example.autoviewpager.model.Event
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject


class MainImageAdapter:
    RecyclerView.Adapter<MainImageAdapter.MainPosterHolder>() {

    private val TAG = MainImageAdapter::class.java.simpleName


    private val onClick: PublishSubject<Pair<Event, ImageView>> = PublishSubject.create()
    val clickEvent: Observable<Pair<Event, ImageView>> = onClick

    private val onTouch : PublishSubject<Boolean> = PublishSubject.create()
    val touchEvent : Observable<Boolean> = onTouch


    private val diff = object : DiffUtil.ItemCallback<Event>() {
        override fun areItemsTheSame(oldItem: Event, newItem: Event): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Event, newItem: Event): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, diff)

    fun submitItems(items: List<Event>?) {
        differ.submitList(items)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainPosterHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemMainImageBinding.inflate(inflater, parent, false)

        return MainPosterHolder(binding)
    }

    override fun getItemCount(): Int = Integer.MAX_VALUE

    override fun onBindViewHolder(holder: MainPosterHolder, position: Int) = holder.bind(differ.currentList[position % differ.currentList.size ])

    inner class MainPosterHolder(private val binding: ItemMainImageBinding) : RecyclerView.ViewHolder(binding.root){
        init {


        }

        fun bind(item: Event) {
            binding.setVariable(BR.item, item)
            binding.executePendingBindings()


        }
    }

}

@BindingAdapter("app:recyclerview_main_images")
fun ViewPager2.setMainImages(items: List<Event>?) {
    (adapter as? MainImageAdapter)?.run {
        this.submitItems(items)
    }
}

