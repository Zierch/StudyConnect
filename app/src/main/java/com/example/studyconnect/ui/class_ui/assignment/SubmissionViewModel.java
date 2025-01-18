package com.example.studyconnect.ui.class_ui.assignment;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.studyconnect.data.StudyRepository;
import com.example.studyconnect.data.response.ClassAssignmentResponse;
import com.example.studyconnect.data.response.SubmissionResponse;
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
public class SubmissionViewModel extends ViewModel {
    private final StudyRepository repository;

    @Inject
    public SubmissionViewModel(StudyRepository repository) {
        this.repository = repository;
    }
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private final MutableLiveData<String> errorMsg = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isSuccess = new MutableLiveData<>();
    private final MutableLiveData<ClassAssignmentResponse> assignment = new MutableLiveData<>();
    private final MutableLiveData<List<SubmissionResponse>> submissions = new MutableLiveData<>();
    private final MutableLiveData<SubmissionResponse> submissionData = new MutableLiveData<>();

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<Boolean> getIsSuccess() {
        return isSuccess;
    }

    public LiveData<String> getErrorMsg() {
        return errorMsg;
    }

    public LiveData<ClassAssignmentResponse> getAssignment(int id) {
        fetchAssignmentById(id);
        return assignment;
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

    public void fetchSubmissions(int assignmentId) {
        isLoading.postValue(true);

        LiveData<Result<List<SubmissionResponse>>> liveData = repository.getSubmissions(assignmentId);
        liveData.observeForever(result -> {
            if (result != null) {
                if (result instanceof Result.Loading) {
                    isLoading.postValue(true);
                } else if (result instanceof Result.Success) {
                    Result.Success<List<SubmissionResponse>> successResult = (Result.Success<List<SubmissionResponse>>) result;
                    submissions.postValue(successResult.getData());
                    isLoading.postValue(false);
                } else if (result instanceof Result.Error<?>) {
                    Result.Error<?> errorResult = (Result.Error<?>) result;
                    errorMsg.postValue(errorResult.getError());
                    isLoading.postValue(false);
                }
            }
        });
    }

    public LiveData<SubmissionResponse> getSubmissionById(int submissionId) {
        fetchSubmissionById(submissionId);
        return submissionData;
    }

    public void fetchSubmissionById(int submissionId) {
        isLoading.postValue(true);

        LiveData<Result<SubmissionResponse>> liveData = repository.getSubmissionById(submissionId);
        liveData.observeForever(result -> {
            if (result != null) {
                if (result instanceof Result.Loading) {
                    isLoading.postValue(true);
                } else if (result instanceof Result.Success) {
                    Result.Success<SubmissionResponse> successResult = (Result.Success<SubmissionResponse>) result;
                    submissionData.postValue(successResult.getData());
                    isLoading.postValue(false);
                } else if (result instanceof Result.Error<?>) {
                    Result.Error<?> errorResult = (Result.Error<?>) result;
                    errorMsg.postValue(errorResult.getError());
                    isLoading.postValue(false);
                }
            }
        });
    }

    public void createSubmission(int assignmentId, String answer, File attachmentFile) {
        isLoading.postValue(true);
        isSuccess.postValue(false);

        MultipartBody.Part filePart = null;
        if (attachmentFile != null) {
            RequestBody fileRequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), attachmentFile);
            filePart = MultipartBody.Part.createFormData("attachment", attachmentFile.getName(), fileRequestBody);
        }

        RequestBody answerBody = RequestBody.create(MediaType.parse("text/plain"), answer);

        LiveData<Result<ResponseBody>> liveData = repository.createSubmission(assignmentId, answerBody, filePart);
        liveData.observeForever(result -> {
            if (result != null) {
                if (result instanceof Result.Loading) {
                    isLoading.postValue(true);
                } else if (result instanceof Result.Success) {
                    errorMsg.postValue("Submission berhasil dibuat");
                    isSuccess.postValue(true);
                    isLoading.postValue(false);
                } else if (result instanceof Result.Error<?>) {
                    Result.Error<?> errorResult = (Result.Error<?>) result;
                    errorMsg.postValue("Gagal membuat submission: " + errorResult.getError());
                    isLoading.postValue(false);
                }
            }
        });
    }

    public void scoreSubmission(int submissionId, int score) {
        isLoading.postValue(true);
        isSuccess.postValue(false);

        RequestBody scoreBody = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(score));

        LiveData<Result<ResponseBody>> liveData = repository.scoreSubmission(submissionId, scoreBody);
        liveData.observeForever(result -> {
            if (result != null) {
                if (result instanceof Result.Loading) {
                    isLoading.postValue(true);
                } else if (result instanceof Result.Success) {
                    errorMsg.postValue("Nilai berhasil diupdate");
                    isSuccess.postValue(true);
                    isLoading.postValue(false);
                } else if (result instanceof Result.Error<?>) {
                    Result.Error<?> errorResult = (Result.Error<?>) result;
                    errorMsg.postValue("Gagal mengupdate nilai: " + errorResult.getError());
                    isLoading.postValue(false);
                }
            }
        });
    }

public boolean isAssignmentAnswered(int assignmentId, int studentId) {
    if (submissions.getValue() != null) {
        for (SubmissionResponse submission : submissions.getValue()) {
            if (submission.getAssignment_id() == assignmentId && submission.getStudent_id() == studentId) {
                return true;
            }
        }
    }
    return false;
}
}
