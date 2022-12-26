package com.bestDate.presentation.main.anotherProfile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import com.bestDate.presentation.base.BaseViewModel
import com.bestDate.data.model.LikesBody
import com.bestDate.data.model.ProfileImage
import com.bestDate.db.entity.UserDB
import com.bestDate.presentation.main.InvitationUseCase
import com.bestDate.presentation.main.userProfile.likesList.LikesListUseCase
import com.hadilq.liveevent.LiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AnotherProfileViewModel @Inject constructor(
    private val anotherProfileUseCase: AnotherProfileUseCase,
    private val invitationUseCase: InvitationUseCase,
    private val likesUseCase: LikesListUseCase
) : BaseViewModel() {

    var user: MutableLiveData<UserDB> = anotherProfileUseCase.user
    var photos:  MutableLiveData<MutableList<ProfileImage>?> = anotherProfileUseCase.photos
    var invitations = invitationUseCase.invitations.asLiveData()

    private var _blockLiveData: LiveEvent<Boolean> = LiveEvent()
    var blockLiveData: LiveEvent<Boolean> = _blockLiveData

    private var _sendInvitationLiveData: LiveEvent<Boolean> = LiveEvent()
    var sendInvitationLiveData: LiveEvent<Boolean> = _sendInvitationLiveData

    var likeLiveData: MutableLiveData<ProfileImage?> = likesUseCase.photoMainResult

    fun getUserById(id: Int?) {
        doAsync {
            anotherProfileUseCase.getUserById(id)
        }
    }

    fun clearUserData() {
        anotherProfileUseCase.user.postValue(null)
    }

    fun blockUser(id: Int?) {
        doAsync {
            anotherProfileUseCase.blockUser(id)
            anotherProfileUseCase.getUserById(id)
            blockLiveData.postValue(true)
        }
    }

    fun unlockUser(id: Int?) {
        doAsync {
            anotherProfileUseCase.unlockUser(id)
            anotherProfileUseCase.getUserById(id)
            blockLiveData.postValue(false)
        }
    }

    fun refreshInvitations() {
        doAsync {
            invitationUseCase.refreshInvitations()
        }
    }

    fun sendInvitation(userId: Int?, invitationId: Int) {
        doAsync {
            invitationUseCase.sendInvitation(userId, invitationId)
            _sendInvitationLiveData.postValue(true)
        }
    }

    fun like(photo_id: Int) {
        doAsync {
            likesUseCase.like(LikesBody(photo_id))
        }
    }
}