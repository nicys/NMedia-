package ru.netology

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import kotlinx.coroutines.ExperimentalCoroutinesApi
import ru.netology.databinding.FragmentAuthenBinding
import ru.netology.dto.Post
import ru.netology.util.PostArg
import ru.netology.util.StringArg
import ru.netology.viewmodel.AuthViewModel
import ru.netology.viewmodel.PostViewModel

class FragmentAuthen : Fragment() {
//    companion object {
//        var Bundle.postPhoto: String? by StringArg
//        var Bundle.postData: Post? by PostArg
//    }

    @ExperimentalCoroutinesApi
    private val viewModel: AuthViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentAuthenBinding.inflate(inflater, container, false)
        return binding.root
    }
}