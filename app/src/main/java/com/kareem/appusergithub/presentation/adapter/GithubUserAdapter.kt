package com.kareem.appusergithub.presentation.adapter


import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.kareem.appusergithub.data.model.UserItems
import com.kareem.appusergithub.databinding.UserItemBinding
import com.kareem.appusergithub.utils.DiffUtils


class GithubUserAdapter: RecyclerView.Adapter<GithubUserAdapter.GithubUserViewHolder>() {
    private var oldList = emptyList<UserItems>()

    fun setData(newList: ArrayList<UserItems>){
        val diffUtils = DiffUtils(oldList, newList)
        val diffResults = DiffUtil.calculateDiff(diffUtils)
        oldList = newList
        diffResults.dispatchUpdatesTo(this)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GithubUserAdapter.GithubUserViewHolder {
        val view = UserItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GithubUserViewHolder(view)
    }

    override fun onBindViewHolder(holder: GithubUserAdapter.GithubUserViewHolder, position: Int) {
        val user = oldList[position]
        holder.bind(user)
    }

    override fun getItemCount(): Int {
        return oldList.size
    }

    inner class GithubUserViewHolder(private val binding: UserItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: UserItems){
            binding.apply {
                itemName.text = user.login
                Glide.with(itemView.context)
                    .load(user.avatar_url)
                    .apply(RequestOptions.circleCropTransform())
                    .into(itemImage)
            }
            itemView.setOnClickListener{
                val intent = Intent(itemView.context, DetailActivity::class.java)
                intent.putExtra(DetailActivity.EXTRA_ID, user.id)
                intent.putExtra(DetailActivity.EXTRA_USERNAME, user.login)
                intent.putExtra(DetailActivity.EXTRA_AVATAR, user.avatar_url)
                itemView.context.startActivity(intent)
            }
        }
    }
}



