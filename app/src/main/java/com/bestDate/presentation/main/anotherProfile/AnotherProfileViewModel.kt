package com.bestDate.presentation.main.anotherProfile

import androidx.lifecycle.LiveData
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

    private var _blockLiveData: MutableLiveData<Boolean> = MutableLiveData()
    var blockLiveData: LiveData<Boolean> = _blockLiveData

    private var _complainLiveData: MutableLiveData<Boolean> = MutableLiveData()
    var complainLiveData: LiveData<Boolean> = _complainLiveData

    private var _sendInvitationLiveData: MutableLiveData<Boolean> = LiveEvent()
    var sendInvitationLiveData: LiveData<Boolean> = _sendInvitationLiveData

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
            _blockLiveData.postValue(true)
        }
    }

    fun unlockUser(id: Int?) {
        doAsync {
            anotherProfileUseCase.unlockUser(id)
            anotherProfileUseCase.getUserById(id)
            _blockLiveData.postValue(false)
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

    fun complain(userId: Int?) {
        doAsync {
            anotherProfileUseCase.complain(userId)
            _complainLiveData.postValue(true)
        }
    }
}