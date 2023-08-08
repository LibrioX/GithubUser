package com.example.githubuser.adapter


import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.githubuser.activity.UserDetailActivity
import com.example.githubuser.animation.ShimmerAnimation
import com.example.githubuser.databinding.ItemRowUserBinding
import com.example.githubuser.models.Users


class UsersAdapter(private val userList : List<Users>) : ListAdapter<Users,UsersAdapter.ListViewHolder>(DIFF_CALLBACK) {

    class ListViewHolder(var binding: ItemRowUserBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {

        val binding = ItemRowUserBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ListViewHolder(binding)

    }

    override fun getItemCount() = userList.size

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {

        val loading = userList[position]
        holder.binding.txtUsername.text = loading.login
        Glide.with(holder.itemView.context)
            .load(loading.avatarUrl)
            .placeholder(ShimmerAnimation.runShimmerAnimation())
            .into(holder.binding.imgUser)
        holder.itemView.setOnClickListener {
            val moveToDetail = Intent(holder.itemView.context, UserDetailActivity::class.java)
            moveToDetail.putExtra(UserDetailActivity.EXTRA_USERNAME,loading.login)
            holder.itemView.context.startActivity(moveToDetail)
        }

    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<Users> =
            object : DiffUtil.ItemCallback<Users>() {
                override fun areItemsTheSame(oldUser: Users, newUser: Users): Boolean {
                    return oldUser.login == newUser.login
                }

                @SuppressLint("DiffUtilEquals")
                override fun areContentsTheSame(oldUser: Users, newUser: Users): Boolean {
                    return oldUser == newUser
                }
            }
    }
}