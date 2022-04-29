package com.kareem.appusergithub.utils

import androidx.recyclerview.widget.DiffUtil
import com.kareem.appusergithub.data.model.UserItems

class DiffUtils(
    private val oldList:List<UserItems>,
    private val newList:List<UserItems>,
    ):DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return when{
            oldList[oldItemPosition].id != newList[newItemPosition].id -> {
                false
            }

            oldList[oldItemPosition].login != newList[newItemPosition].login -> {
                false
            }

            oldList[oldItemPosition].avatar_url != newList[newItemPosition].avatar_url -> {
                false
            }
            else -> true
        }
    }
}