package dev.adryanev.dicodingstory.features.story.presentation.story_list.pages.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import dev.adryanev.dicodingstory.R
import dev.adryanev.dicodingstory.databinding.FragmentStoryItemBinding
import dev.adryanev.dicodingstory.features.story.domain.entities.Story
import java.time.format.DateTimeFormatter

class StoryListAdapter(
    val listener: (story: Story) -> Unit
) : ListAdapter<Story, StoryItemViewHolder>(StoryDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryItemViewHolder {
        val holder = StoryItemViewHolder(
            FragmentStoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
        holder.binding.root.setOnClickListener {
            listener(getItem(holder.bindingAdapterPosition))
        }
        return holder
    }

    override fun onBindViewHolder(holder: StoryItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class StoryDiffCallback : DiffUtil.ItemCallback<Story>() {
    override fun areItemsTheSame(oldItem: Story, newItem: Story): Boolean = oldItem.id == newItem.id


    override fun areContentsTheSame(oldItem: Story, newItem: Story): Boolean = oldItem == newItem

}

class StoryItemViewHolder(val binding: FragmentStoryItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Story) {

        binding.apply {
            itemStoryAuthor.text = item.name
            itemStoryDescription.text = item.description
            itemStoryLocation.text = root.context.getString(
                R.string.coordinates,
                item.location?.latitude,
                item.location?.longitude
            )
            if (item.location == null) {
                itemStoryLocation.visibility = View.INVISIBLE
            }
            val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm:ss")
            val formattedDate = item.createdAt.format(formatter)

            itemStoryCreatedDate.text = formattedDate
            Picasso.get()
                .load(item.photoUrl)
                .placeholder(R.drawable.undraw_not_found)
                .error(R.drawable.undraw_not_found)
                .fit()
                .into(itemStoryImage)
        }
    }
}