package com.example.studyconnect.ui.class_ui.assignment;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.studyconnect.data.StudyRepository;
import com.example.studyconnect.data.response.ClassAssignmentResponse;
import com.example.studyconnect.data.response.RegisterResponse;
import com.example.studyconnect.utils.Result;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import dagger.hilt.android.qualifiers.ApplicationContext;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

@HiltViewModel
public class ClassAssignmentViewModel extends ViewModel {
    private final StudyRepository repository;

    @Inject
    @ApplicationContext
    Context context;

    @Inject
    public ClassAssignmentViewModel(StudyRepository repository) {
        this.repository = repository;
    }

    private final MutableLiveData<List<ClassAssignmentResponse>> classAssignment = new MutableLiveData<>();
    private final MutableLiveData<ClassAssignmentResponse> assignment = new MutableLiveData<>();
    private final MutableLiveData<RegisterResponse> user = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private final MutableLiveData<String> errorMsg = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isSuccess = new MutableLiveData<>();

    public LiveData<List<ClassAssignmentResponse>> getClassAssignment() {
        return classAssignment;
    }

    public LiveData<ClassAssignmentResponse> getAssignment() {
        return assignment;
    }

    public LiveData<RegisterResponse> getUser() {
        return user;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<Boolean> getIsSuccess() {
        return isSuccess;
    }

    public LiveData<String> getErrorMsg() {
        return errorMsg;
    }

    public void fetchClassAssignment(int classId) {
        isLoading.postValue(true);

        LiveData<Result<List<ClassAssignmentResponse>>> classAssignmentLiveData = repository.getClassAssignment(classId);
        classAssignmentLiveData.observeForever(result -> {
            if (result != null) {
                if (result instanceof Result.Loading) {
                    isLoading.postValue(true);
                } else if (result instanceof Result.Success) {
                    Result.Success<List<ClassAssignmentResponse>> successResult = (Result.Success<List<ClassAssignmentResponse>>) result;
                    classAssignment.postValue(successResult.getData());

                    isLoading.postValue(false);
                } else if (result instanceof Result.Error<?>) {
                    Result.Error<?> errorResult = (Result.Error<?>) result;
                    errorMsg.postValue(errorResult.getError());

                    isLoading.postValue(false);
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
                    Result.Success<RegisterResponse> successResult = (Result.Success<RegisterResponse>) result;
                    user.postValue(successResult.getData());

                    isLoading.postValue(false);
                } else if (result instanceof Result.Error<?>) {
                    Result.Error<?> errorResult = (Result.Error<?>) result;
                    errorMsg.postValue(errorResult.getError());

                    isLoading.postValue(false);
                }
            }
        });
    }

    public void fetchAssignmentById(int id) {
        isLoading.postValue(true);

        LiveData<Result<ClassAssignmentResponse>> liveData = repository.getAssignmentById(id);
        liveData.observeForever(result -> {
            if (result != null) {
                if (result instanceof Result.Loading) {
                    isLoading.postValue(true);
                } else if (result instanceof Result.Success) {
                    Result.Success<ClassAssignmentResponse> successResult =
                            (Result.Success<ClassAssignmentResponse>) result;
                    assignment.postValue(successResult.getData());

                    isLoading.postValue(false);
                } else if (result instanceof Result.Error<?>) {
                    Result.Error<?> errorResult = (Result.Error<?>) result;
                    errorMsg.postValue(errorResult.getError());

                    isLoading.postValue(false);
                }
            }
        });
    }

    public void createAssignment(
            int classId, String title, String description, String dueDate,
            String scheduleAt, String points, String fileUrl, int type
    ) {
        isLoading.postValue(true);
        isSuccess.postValue(false);

        String typeAssignment = "";
        String options = "[\n" +
                "  \"A\",\n" +
                "  \"B\",\n" +
                "  \"C\",\n" +
                "  \"D\"\n" +
                "]";

        switch (type) {
            case 1:
                typeAssignment = "essay";
                break;
            case 2:
                typeAssignment = "multiple_choice";
                break;
            case 3:
                typeAssignment = "with_file";
                break;
            default:
                break;
        }

        RequestBody body = new FormBody.Builder()
                .add("title", title)
                .add("description", description != null ? description : "")
                .add("attachment_path", fileUrl != null ? fileUrl : "")
                .add("due_date", dueDate != null ? dueDate : "")
                .add("max_score", points)
                .add("class_id", String.valueOf(classId))
                .add("status", scheduleAt != null ? "scheduleAt" : "published")
                .add("type", typeAssignment)
                .add("options", options)
                .build();

        LiveData<Result<ClassAssignmentResponse>> liveData =
                repository.createAssignment(classId, body);
        liveData.observeForever(result -> {
            if (result != null) {
                if (result instanceof Result.Loading) {
                    isLoading.postValue(true);
                } else if (result instanceof Result.Success) {
                    errorMsg.postValue("Tugas berhasil dibuat");

                    isSuccess.postValue(true);
                    isLoading.postValue(false);
                } else if (result instanceof Result.Error<?>) {
                    Result.Error<?> errorResult = (Result.Error<?>) result;
                    errorMsg.postValue("Gagal membuat tugas: " + errorResult.getError());

                    isLoading.postValue(false);
                }
            }
        });
    }

    public void updateAssignment(
            int assignmentId, int classId, String title, String description, String dueDate,
            String scheduleAt, String points, String fileUrl, int type
    ) {
        isLoading.postValue(true);
        isSuccess.postValue(false);

        String typeAssignment = "";
        String options = "[\n" +
                "  \"A\",\n" +
                "  \"B\",\n" +
                "  \"C\",\n" +
                "  \"D\"\n" +
                "]";

        switch (type) {
            case 1:
                typeAssignment = "essay";
                break;
            case 2:
                typeAssignment = "multiple_choice";
                break;
            case 3:
                typeAssignment = "with_file";
                break;
            default:
                break;
        }

        RequestBody body = new FormBody.Builder()
                .add("title", title)
                .add("description", description != null ? description : "")
                .add("attachment_path", fileUrl != null ? fileUrl : "")
                .add("due_date", dueDate != null ? dueDate : "")
                .add("max_score", points)
                .add("class_id", String.valueOf(classId))
                .add("status", scheduleAt != null ? "scheduleAt" : "published")
                .add("type", typeAssignment)
                .add("options", options)
                .build();

        LiveData<Result<ClassAssignmentResponse>> liveData =
                repository.updateAssignment(assignmentId, body);
        liveData.observeForever(result -> {
            if (result != null) {
                if (result instanceof Result.Loading) {
                    isLoading.postValue(true);
                } else if (result instanceof Result.Success) {
                    errorMsg.postValue("Tugas berhasil diubah");

                    isSuccess.postValue(true);
                    isLoading.postValue(false);
                } else if (result instanceof Result.Error<?>) {
                    Result.Error<?> errorResult = (Result.Error<?>) result;
                    errorMsg.postValue("Gagal mengubah tugas: " + errorResult.getError());

                    isLoading.postValue(false);
                }
            }
        });
    }

    public void deleteAssignment(int id, int classId) {
        isLoading.postValue(true);

        LiveData<Result<ResponseBody>> deleteAssignmentLiveData = repository.deleteAssignment(id);
        deleteAssignmentLiveData.observeForever(result -> {
            if (result != null) {
                if (result instanceof Result.Loading) {
                    isLoading.postValue(true);
                } else if (result instanceof Result.Success) {
                    errorMsg.postValue("Tugas berhasil dihapus");
                    fetchClassAssignment(classId);

                    isLoading.postValue(false);
                } else if (result instanceof Result.Error<?>) {
                    Result.Error<?> errorResult = (Result.Error<?>) result;
                    errorMsg.postValue("Gagal menghapus tugas: " + errorResult.getError());

                    isLoading.postValue(false);
                }
            }
        });
    }
}
