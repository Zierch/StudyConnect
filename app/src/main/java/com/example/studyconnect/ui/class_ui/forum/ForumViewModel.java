package com.example.studyconnect.ui.class_ui.forum;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.studyconnect.data.StudyRepository;
import com.example.studyconnect.data.response.AnnouncementResponse;
import com.example.studyconnect.data.response.ClassResponse;
import com.example.studyconnect.data.response.RegisterResponse;
import com.example.studyconnect.utils.Result;

import java.io.File;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

@HiltViewModel
public class ForumViewModel extends ViewModel {
    private final StudyRepository repository;

    @Inject
    public ForumViewModel(StudyRepository repository) {
        this.repository = repository;
    }

    // LiveData untuk class, user, dan status loading/success/error
    private final MutableLiveData<ClassResponse> classData = new MutableLiveData<>();
    private final MutableLiveData<RegisterResponse> user = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isSuccess = new MutableLiveData<>();
    private final MutableLiveData<String> errorMsg = new MutableLiveData<>();
    private final MutableLiveData<List<AnnouncementResponse>> announcements = new MutableLiveData<>();
    private final MutableLiveData<AnnouncementResponse> announcementData = new MutableLiveData<>();

    // Getter untuk LiveData
    public LiveData<Boolean> getIsSuccess() {
        return isSuccess;
    }

    public LiveData<ClassResponse> getClassData() {
        return classData;
    }

    public LiveData<RegisterResponse> getUser(int teacherId) {
        fetchUser(teacherId);
        return user;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<String> getErrorMsg() {
        return errorMsg;
    }

    public LiveData<List<AnnouncementResponse>> getAnnouncements(int classId) {
        fetchAnnouncement(classId);
        return announcements;
    }

    public LiveData<AnnouncementResponse> getAnnouncementData() {
        return announcementData;
    }

    public LiveData<Boolean> getisLoading() {
        return isLoading;
    }

    public LiveData<Boolean> getisSuccess() {
        return isSuccess;
    }

    public LiveData<String> geterrorMsg() {
        return errorMsg;
    }

    // Method untuk mengambil data kelas
    public void fetchClass(int classId) {
        isLoading.postValue(true);

        LiveData<Result<ClassResponse>> classLiveData = repository.getClass(classId);
        classLiveData.observeForever(result -> {
            if (result != null) {
                if (result instanceof Result.Loading) {
                    isLoading.postValue(true);
                } else if (result instanceof Result.Success) {
                    Result.Success<ClassResponse> successResult = (Result.Success<ClassResponse>) result;
                    classData.postValue(successResult.getData());
                    isLoading.postValue(false);
                } else if (result instanceof Result.Error<?>) {
                    Result.Error<?> errorResult = (Result.Error<?>) result;
                    errorMsg.postValue(errorResult.getError());
                    isLoading.postValue(false);
                }
            }
        });
    }

    // Method untuk mengambil data pengguna
    public void fetchUser(int userId) {
        isLoading.postValue(true);

        LiveData<Result<RegisterResponse>> userLiveData = repository.getUser(userId);
        userLiveData.observeForever(result -> {
            if (result != null) {
                if (result instanceof Result.Loading) {
                    isLoading.postValue(true);
                } else if (result instanceof Result.Success) {
                    isLoading.postValue(false);
                    Result.Success<RegisterResponse> successResult = (Result.Success<RegisterResponse>) result;
                    user.postValue(successResult.getData());
                } else if (result instanceof Result.Error<?>) {
                    isLoading.postValue(false);
                    Result.Error<?> errorResult = (Result.Error<?>) result;
                    errorMsg.postValue(errorResult.getError());
                }
            }
        });
    }

    // Method untuk membuat pengumuman
    public void createAnnouncement(int classId, String content, String scheduledAt, File attachmentFile) {
        isLoading.postValue(true);

        // Membuat RequestBody untuk data form
        RequestBody contentBody = RequestBody.create(MediaType.parse("text/plain"), content);
        RequestBody scheduledAtBody = scheduledAt != null ?
                RequestBody.create(MediaType.parse("text/plain"), scheduledAt) :
                null;

        // Membuat MultipartBody.Part untuk file (jika ada)
        MultipartBody.Part filePart = null;
        if (attachmentFile != null) {
            RequestBody fileRequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), attachmentFile);
            filePart = MultipartBody.Part.createFormData("attachment", attachmentFile.getName(), fileRequestBody);
        }

        // Memanggil repository
        LiveData<Result<ResponseBody>> createAnnouncementLiveData = repository.createAnnouncement(classId, contentBody, scheduledAtBody, filePart);
        createAnnouncementLiveData.observeForever(result -> {
            if (result != null) {
                if (result instanceof Result.Success) {
                    isLoading.postValue(false);
                    isSuccess.postValue(true);
                } else if (result instanceof Result.Error<?>) {
                    isLoading.postValue(false);
                    Result.Error<?> errorResult = (Result.Error<?>) result;
                    errorMsg.postValue(errorResult.getError());
                }
            }
        });
    }

    // Method untuk memperbarui pengumuman
    public void updateAnnouncement(int id, String content, File attachmentFile) {
        isLoading.postValue(true);

        // Membuat RequestBody untuk data form
        RequestBody contentBody = RequestBody.create(MediaType.parse("text/plain"), content);

        // Membuat MultipartBody.Part untuk file (jika ada)
        MultipartBody.Part filePart = null;
        if (attachmentFile != null) {
            RequestBody fileRequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), attachmentFile);
            filePart = MultipartBody.Part.createFormData("attachment", attachmentFile.getName(), fileRequestBody);
        }

        // Memanggil repository
        LiveData<Result<ResponseBody>> updateAnnouncementLiveData = repository.updateAnnouncement(id, contentBody, filePart);
        updateAnnouncementLiveData.observeForever(result -> {
            if (result != null) {
                if (result instanceof Result.Success) {
                    isLoading.postValue(false);
                    isSuccess.postValue(true);
                } else if (result instanceof Result.Error<?>) {
                    isLoading.postValue(false);
                    Result.Error<?> errorResult = (Result.Error<?>) result;
                    errorMsg.postValue(errorResult.getError());
                }
            }
        });
    }

    // Method untuk menghapus pengumuman
    public void deleteAnnouncement(int id) {
        isLoading.postValue(true);

        LiveData<Result<ResponseBody>> deleteAnnouncementLiveData = repository.deleteAnnouncement(id);
        deleteAnnouncementLiveData.observeForever(result -> {
            if (result != null) {
                if (result instanceof Result.Success) {
                    isLoading.postValue(false);
                    isSuccess.postValue(true);
                } else if (result instanceof Result.Error<?>) {
                    isLoading.postValue(false);
                    Result.Error<?> errorResult = (Result.Error<?>) result;
                    errorMsg.postValue(errorResult.getError());
                }
            }
        });
    }

    // Method untuk mengambil pengumuman
    public void fetchAnnouncement(int classId)   {
        isLoading.postValue(true);

        LiveData<Result<List<AnnouncementResponse>>> announcementLiveData = repository.getAnnouncements(classId);
        announcementLiveData.observeForever(result -> {
            if (result != null) {
                if (result instanceof Result.Success) {
                    isLoading.postValue(false);
                    Result.Success<List<AnnouncementResponse>> successResult = (Result.Success<List<AnnouncementResponse>>) result;
                    announcements.postValue(successResult.getData());
                } else if (result instanceof Result.Error<?>) {
                    isLoading.postValue(false);
                    Result.Error<?> errorResult = (Result.Error<?>) result;
                    errorMsg.postValue(errorResult.getError());
                }
            }
        });
    }

    // Method untuk mengambil pengumuman dengan id
    public void fetchAnnouncementById(int id)   {
        isLoading.postValue(true);

        LiveData<Result<AnnouncementResponse>> announcementLiveData = repository.getAnnouncementById(id);
        announcementLiveData.observeForever(result -> {
            if (result != null) {
                if (result instanceof Result.Success) {
                    isLoading.postValue(false);
                    Result.Success<AnnouncementResponse> successResult = (Result.Success<AnnouncementResponse>) result;
                    announcementData.postValue(successResult.getData());
                } else if (result instanceof Result.Error<?>) {
                    isLoading.postValue(false);
                    Result.Error<?> errorResult = (Result.Error<?>) result;
                    errorMsg.postValue(errorResult.getError());
                }
            }
        });
    }
}