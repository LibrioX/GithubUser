package com.example.githubuser.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuser.adapter.UsersAdapter
import com.example.githubuser.databinding.FragmentFollowBinding
import com.example.githubuser.models.Users
import com.example.githubuser.repository.Result
import com.example.githubuser.viewModel.ViewModelFactory
import com.example.githubuser.viewModel.MainViewModel


class FollowFragment : Fragment() {

    private var _binding: FragmentFollowBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFollowBinding.inflate(inflater, container, false)
        return binding.root
    }

        private val mainViewModel by viewModels<MainViewModel>{
            ViewModelFactory.getInstance(requireActivity(),null)
        }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val index = arguments?.getInt(ARG_POSITION, 0)
        val name = arguments?.getString(ARG_USERNAME)

        if(index == 1){
            //digunakan supaya tidak memanggil API lagi saat orientation berubah
            if(mainViewModel.getUsername() != name && name != null){

                mainViewModel.setUsername(name)
                mainViewModel.getFollowers(name)

            }
        }else{
            //digunakan supaya tidak memanggil API lagi saat orientation berubah
            if(mainViewModel.getUsername() != name && name != null){

                mainViewModel.setUsername(name)
                mainViewModel.getFollowing(name)

            }

        }

        mainViewModel.follow.observe(viewLifecycleOwner) { result->
            when(result){
                is Result.Success -> {
                    showLoading(false)
                    setUsersData(result.data)
                }
                is Result.Error -> {
                    showLoading(false)
                    Toast.makeText(
                        requireActivity(),
                        "Terjadi kesalahan" + result.error,
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is Result.Loading -> {
                    showLoading(true)
                }
            }
        }


    }

    private fun setUsersData (users : List<Users>){
        val adapter = UsersAdapter(users)
        binding.rvUser.layoutManager = LinearLayoutManager(requireActivity())
        binding.rvUser.adapter = adapter
    }

    private fun showLoading(isLoading: Boolean) {
        binding.layoutLoading.layoutAllLoading.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val ARG_POSITION = "arg_position"
        const val ARG_USERNAME = "arg_name"
    }

}