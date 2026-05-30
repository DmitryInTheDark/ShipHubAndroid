package com.app.shiphub.ui.main.common

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import coil.load
import com.app.base.BaseFragment
import com.app.base.BaseState
import com.app.base.BaseViewModel
import com.app.shiphub.databinding.FragmentPhotoViewBinding
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

class PhotoViewState : BaseState()

@HiltViewModel
class PhotoViewViewModel @Inject constructor() : BaseViewModel<PhotoViewState>(PhotoViewState())

@AndroidEntryPoint
class PhotoViewFragment : BaseFragment<FragmentPhotoViewBinding, PhotoViewState, PhotoViewViewModel>() {

    override val viewModel: PhotoViewViewModel by viewModels()
    private val args: PhotoViewFragmentArgs by navArgs()

    override fun initializeBinding() = FragmentPhotoViewBinding.inflate(layoutInflater)

    override fun setupListeners() {
        binding.ibBack.setOnClickListener {
            navigateBack()
        }
    }

    override fun setupUI() {
        binding.ivPhoto.load(args.photoUrl) {
            crossfade(true)
        }
    }

    override fun handleState(state: PhotoViewState) {}
}
