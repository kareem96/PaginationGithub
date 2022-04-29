package com.kareem.appusergithub.presentation.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.kareem.appusergithub.data.model.UserItems
import com.kareem.appusergithub.databinding.UserItemBinding


class GithubUserAdapter: ListAdapter<UserItems, GithubUserAdapter.UserViewHolder>(DATA_COMPARATOR) {
    companion object{
        private val DATA_COMPARATOR = object : DiffUtil.ItemCallback<UserItems>(){
            override fun areItemsTheSame(oldItem: UserItems, newItem: UserItems): Boolean {
                return oldItem.avatarUrl == newItem.avatarUrl
            }

            override fun areContentsTheSame(oldItem: UserItems, newItem: UserItems): Boolean {
                return oldItem == newItem
            }

        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GithubUserAdapter.UserViewHolder {
        val view = UserItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(view)
    }




    inner class UserViewHolder(private val binding: UserItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: UserItems){
            binding.apply {
                itemName.text = user.login
                Glide.with(itemView.context)
                    .load(user.avatarUrl)
                    .apply(RequestOptions.circleCropTransform())
                    .into(itemImage)
            }
        }
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val repoItem = getItem(position)
        if(repoItem != null){
            holder.bind(repoItem)
        }
    }
}



