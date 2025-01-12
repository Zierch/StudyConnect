package com.example.studyconnect.ui.home;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.studyconnect.data.StudyRepository;
import com.example.studyconnect.data.response.ClassResponse;
import com.example.studyconnect.data.response.RegisterResponse;
import com.example.studyconnect.utils.Result;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import dagger.hilt.android.qualifiers.ApplicationContext;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@HiltViewModel
public class HomeViewModel extends ViewModel {
    private final StudyRepository repository;

    @Inject
    @ApplicationContext
    Context context;

    @Inject
    public HomeViewModel(StudyRepository repository) {
        this.repository = repository;
    }

    private final MutableLiveData<List<ClassResponse>> classes = new MutableLiveData<>();
    private final MutableLiveData<List<ClassResponse>> archivedClasses = new MutableLiveData<>();
    private final MutableLiveData<RegisterResponse> user = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private final MutableLiveData<String> errorMsg = new MutableLiveData<>();

    public LiveData<List<ClassResponse>> getClasses() {
        return classes;
    }

    public LiveData<List<ClassResponse>> getArchivedClasses() {
        return archivedClasses;
    }

    public LiveData<RegisterResponse> getUser(int id) {
        fetchUser(id);
        return user;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<String> getErrorMsg() {
        return errorMsg;
    }

    public void fetchActiveClasses() {
        isLoading.postValue(true);
        LiveData<Result<List<ClassResponse>>> activeClassesLiveData = repository.getClasses();
        activeClassesLiveData.observeForever(result -> {
            if (result != null) {
                if (result instanceof Result.Loading) {
                    isLoading.postValue(true);
                } else if (result instanceof Result.Success) {
                    isLoading.postValue(false);
                    Result.Success<List<ClassResponse>> successResult = (Result.Success<List<ClassResponse>>) result;
                    classes.postValue(successResult.getData());
                } else if (result instanceof Result.Error<?>) {
                    isLoading.postValue(false);
                    Result.Error<?> errorResult = (Result.Error<?>) result;
                    errorMsg.postValue(errorResult.getError());
                }
            }
        });
    }

    public void fetchArchivedClasses() {
        isLoading.postValue(true);
        LiveData<Result<List<ClassResponse>>> archivedClassesLiveData = repository.getArchivedClasses();
        archivedClassesLiveData.observeForever(result -> {
            if (result != null) {
                if (result instanceof Result.Loading) {
                    isLoading.postValue(true);
                } else if (result instanceof Result.Success) {
                    isLoading.postValue(false);
                    Result.Success<List<ClassResponse>> successResult = (Result.Success<List<ClassResponse>>) result;
                    archivedClasses.postValue(successResult.getData());
                } else if (result instanceof Result.Error<?>) {
                    isLoading.postValue(false);
                    Result.Error<?> errorResult = (Result.Error<?>) result;
                    errorMsg.postValue(errorResult.getError());
                }
            }
        });
    }

    public void fetchUser(int id) {
        isLoading.postValue(true);
        LiveData<Result<RegisterResponse>> userLiveData = repository.getUser(id);
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

    public void joinClass(int id) {
        isLoading.postValue(true);
        LiveData<Result<ResponseBody>> joinClassLiveData = repository.joinClass(id);
        joinClassLiveData.observeForever(result -> {
            if (result != null) {
                if (result instanceof Result.Loading) {
                    isLoading.postValue(true);
                } else if (result instanceof Result.Success) {
                    isLoading.postValue(false);
                    errorMsg.postValue("Berhasil bergabung ke kelas");
                    fetchActiveClasses(); // Refresh the list of active classes
                } else if (result instanceof Result.Error<?>) {
                    isLoading.postValue(false);
                    Result.Error<?> errorResult = (Result.Error<?>) result;
                    errorMsg.postValue("Gagal bergabung ke kelas: " + errorResult.getError());
                }
            }
        });
    }

    public void leaveClass(int id) {
        isLoading.postValue(true);
        LiveData<Result<ResponseBody>> leaveClassLiveData = repository.leaveClass(id);
        leaveClassLiveData.observeForever(result -> {
            if (result != null) {
                if (result instanceof Result.Loading) {
                    isLoading.postValue(true);
                } else if (result instanceof Result.Success) {
                    isLoading.postValue(false);
                    errorMsg.postValue("Berhasil meninggalkan kelas");
                    fetchActiveClasses(); // Refresh the list of active classes
                } else if (result instanceof Result.Error<?>) {
                    isLoading.postValue(false);
                    Result.Error<?> errorResult = (Result.Error<?>) result;
                    errorMsg.postValue("Gagal meninggalkan kelas: " + errorResult.getError());
                }
            }
        });
    }

    public void deleteClass(int id) {
        isLoading.postValue(true);
        LiveData<Result<ResponseBody>> deleteClassLiveData = repository.deleteClass(id);
        deleteClassLiveData.observeForever(result -> {
            if (result != null) {
                if (result instanceof Result.Loading) {
                    isLoading.postValue(true);
                } else if (result instanceof Result.Success) {
                    isLoading.postValue(false);
                    errorMsg.postValue("Kelas berhasil dihapus");
                    fetchArchivedClasses(); // Refresh the list of archived classes
                } else if (result instanceof Result.Error<?>) {
                    isLoading.postValue(false);
                    Result.Error<?> errorResult = (Result.Error<?>) result;
                    errorMsg.postValue("Gagal menghapus kelas: " + errorResult.getError());
                }
            }
        });
    }

    public void archiveClass(int id) {
        isLoading.postValue(true);
        LiveData<Result<ResponseBody>> archiveClassLiveData = repository.archiveClass(id);
        archiveClassLiveData.observeForever(result -> {
            if (result != null) {
                if (result instanceof Result.Loading) {
                    isLoading.postValue(true);
                } else if (result instanceof Result.Success) {
                    isLoading.postValue(false);
                    errorMsg.postValue("Kelas berhasil diarsipkan");
                    fetchActiveClasses(); // Refresh the list of active classes
                } else if (result instanceof Result.Error<?>) {
                    isLoading.postValue(false);
                    Result.Error<?> errorResult = (Result.Error<?>) result;
                    errorMsg.postValue("Gagal mengarsipkan kelas: " + errorResult.getError());
                }
            }
        });
    }

    public void restoreClass(int id) {
        isLoading.postValue(true);
        LiveData<Result<ResponseBody>> restoreClassLiveData = repository.restoreClass(id);
        restoreClassLiveData.observeForever(result -> {
            if (result != null) {
                if (result instanceof Result.Loading) {
                    isLoading.postValue(true);
                } else if (result instanceof Result.Success) {
                    isLoading.postValue(false);
                    errorMsg.postValue("Kelas berhasil dipulihkan");
                    fetchArchivedClasses(); // Refresh the list of archived classes
                } else if (result instanceof Result.Error<?>) {
                    isLoading.postValue(false);
                    Result.Error<?> errorResult = (Result.Error<?>) result;
                    errorMsg.postValue("Gagal memulihkan kelas: " + errorResult.getError());
                }
            }
        });
    }
}