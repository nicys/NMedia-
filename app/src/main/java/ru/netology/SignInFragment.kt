package ru.netology

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import kotlinx.coroutines.ExperimentalCoroutinesApi
import ru.netology.databinding.AuthenFragmentBinding
import ru.netology.viewmodel.AuthViewModel

class SignInFragment : Fragment() {
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
        val binding = AuthenFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }
}