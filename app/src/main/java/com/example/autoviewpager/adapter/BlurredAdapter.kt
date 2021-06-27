package com.example.autoviewpager.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import androidx.databinding.library.baseAdapters.BR
import com.example.autoviewpager.databinding.ItemBlurredImageBinding
import com.example.autoviewpager.model.Event
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject


class BlurredAdapter:
    RecyclerView.Adapter<BlurredAdapter.BlurredHolder>() {

    private val TAG = BlurredAdapter::class.java.simpleName


    private val onClick: PublishSubject<String> = PublishSubject.create()
    val clickEvent: Observable<String> = onClick

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BlurredHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemBlurredImageBinding.inflate(inflater, parent, false)

        return BlurredHolder(binding)
    }

    override fun getItemCount(): Int = differ.currentList.size

    override fun onBindViewHolder(holder: BlurredHolder, position: Int) = holder.bind(differ.currentList[position])

    inner class BlurredHolder(private val binding: ItemBlurredImageBinding) : RecyclerView.ViewHolder(binding.root)
    {
        init {


        }

        fun bind(item: Event) {
            binding.setVariable(BR.item, item)
            binding.executePendingBindings()

        }
    }

}

@BindingAdapter("app:recyclerview_image_blurred")
fun ViewPager2.setImageBlurred(items: List<Event>?) {
    (adapter as? BlurredAdapter)?.run {
        this.submitItems(items)
    }
}
