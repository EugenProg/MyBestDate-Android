package com.bestDate.presentation.main.userProfile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.bestDate.R
import com.bestDate.base.BaseVMFragment
import com.bestDate.data.model.Image
import com.bestDate.data.model.ProfileImage
import com.bestDate.databinding.FragmentUserProfileBinding
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserProfileFragment : BaseVMFragment<FragmentUserProfileBinding, UserProfileViewModel>() {
    override val onBinding: (LayoutInflater, ViewGroup?, Boolean) -> FragmentUserProfileBinding =
        { inflater, parent, attach ->
            FragmentUserProfileBinding.inflate(inflater, parent, attach)
        }
    override val viewModelClass: Class<UserProfileViewModel> = UserProfileViewModel::class.java
    override val statusBarLight = true
    override val statusBarColor = R.color.white

    private lateinit var adapter: ImageLineAdapter

    override fun onInit() {
        super.onInit()

        adapter = ImageLineAdapter()
        binding.imagesCarousel.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.imagesCarousel.adapter = adapter
    }

    override fun onViewLifecycle() {
        super.onViewLifecycle()
        viewModel.user.observe(viewLifecycleOwner) {
            it.let { user ->
                user?.photos?.firstOrNull { it.main == true }.let { image ->
                    setMainImage(image)
                }
                binding.name.text = user?.name
                binding.birthdate.text = user?.getFormattedBirthday()

                adapter.submitList(getImageList(user?.photos))
            }
        }
    }

    private fun getImageList(images: MutableList<ProfileImage>?): MutableList<ProfileImage> {
        images?.add(ProfileImage(id = -1))
        return images ?: mutableListOf()
    }

    private fun setMainImage(image: ProfileImage?) {
        if (image == null) return
        makeStatusBarTransparent(binding.scroll)

        Glide.with(requireContext())
            .load(image.thumb_url)
            .circleCrop()
            .into(binding.avatar)

        Glide.with(requireContext())
            .load(image.full_url)
            .into(binding.imageBack)
    }
}