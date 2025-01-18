package com.example.studyconnect.ui.class_ui.assignment;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.studyconnect.data.StudyRepository;
import com.example.studyconnect.data.model.ListTugasKelas;
import com.example.studyconnect.data.response.ClassAssignmentResponse;
import com.example.studyconnect.data.response.MaterialResponse;
import com.example.studyconnect.data.response.RegisterResponse;
import com.example.studyconnect.utils.Result;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import dagger.hilt.android.qualifiers.ApplicationContext;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
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
    private final MutableLiveData<List<MaterialResponse>> materialItems = new MutableLiveData<>();
    private final MutableLiveData<List<ListTugasKelas>> tugasClassList = new MutableLiveData<>();
    private final MutableLiveData<ClassAssignmentResponse> assignment = new MutableLiveData<>();
    private final MutableLiveData<MaterialResponse> material = new MutableLiveData<>();
    private final MutableLiveData<RegisterResponse> user = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private final MutableLiveData<String> errorMsg = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isSuccess = new MutableLiveData<>();

    public void setTugasClassList(List<ListTugasKelas> tugasClassList) {
        this.tugasClassList.postValue(tugasClassList);
    }

    public LiveData<List<ClassAssignmentResponse>> getClassAssignment() {
        return classAssignment;
    }

    public LiveData<ClassAssignmentResponse> getAssignment(int id) {
        fetchAssignmentById(id);
        return assignment;
    }

    public LiveData<List<MaterialResponse>> getMaterialItems() {
        return materialItems;
    }

    public LiveData<MaterialResponse> getMaterial(int id) {
        fetchMaterialById(id);
        return material;
    }

    public LiveData<List<ListTugasKelas>> getTugasClassList() {
        return tugasClassList;
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

    public void fetchMaterialById(int id) {
        isLoading.postValue(true);

        LiveData<Result<MaterialResponse>> liveData = repository.getMaterialById(id);
        liveData.observeForever(result -> {
            if (result != null) {
                if (result instanceof Result.Loading) {
                    isLoading.postValue(true);
                } else if (result instanceof Result.Success) {
                    Result.Success<MaterialResponse> successResult =
                            (Result.Success<MaterialResponse>) result;
                    material.postValue(successResult.getData());

                    isLoading.postValue(false);
                } else if (result instanceof Result.Error<?>) {
                    Result.Error<?> errorResult = (Result.Error<?>) result;
                    errorMsg.postValue(errorResult.getError());

                    isLoading.postValue(false);
                }
            }
        });
    }

    public void createMaterial(
            int classId, String title, String description, File attachmentFile,
            String teacherId
    ) {
        isLoading.postValue(true);
        isSuccess.postValue(false);

        MultipartBody.Part filePart = null;
        if (attachmentFile != null) {
            RequestBody fileRequestBody = RequestBody.create(
                    MediaType.parse("multipart/form-data"),
                    attachmentFile
            );
            filePart = MultipartBody.Part.createFormData(
                    "attachment",
                    attachmentFile.getName(),
                    fileRequestBody
            );
        }

        RequestBody titleBody = RequestBody.create(MediaType.parse("text/plain"), title);
        RequestBody descriptionBody = RequestBody.create(
                MediaType.parse("text/plain"), description != null ? description : ""
        );
        RequestBody teacherIdBody = RequestBody.create(
                MediaType.parse("text/plain"), teacherId
        );

        LiveData<Result<MaterialResponse>> liveData =
                repository.createMaterial(
                        classId, titleBody, descriptionBody, filePart, teacherIdBody
                );
        liveData.observeForever(result -> {
            if (result != null) {
                if (result instanceof Result.Loading) {
                    isLoading.postValue(true);
                } else if (result instanceof Result.Success) {
                    errorMsg.postValue("Materi berhasil dibuat");

                    isSuccess.postValue(true);
                    isLoading.postValue(false);
                } else if (result instanceof Result.Error<?>) {
                    Result.Error<?> errorResult = (Result.Error<?>) result;
                    errorMsg.postValue("Gagal membuat materi: " + errorResult.getError());

                    isLoading.postValue(false);
                }
            }
        });
    }

    public void updateMaterial(
            int classId, int id, String title, String description, File attachmentFile,
            String teacherId
    ) {
        isLoading.postValue(true);
        isSuccess.postValue(false);

        MultipartBody.Part filePart = null;
        if (attachmentFile != null) {
            RequestBody fileRequestBody = RequestBody.create(
                    MediaType.parse("multipart/form-data"),
                    attachmentFile
            );
            filePart = MultipartBody.Part.createFormData(
                    "attachment",
                    attachmentFile.getName(),
                    fileRequestBody
            );
        }

        RequestBody titleBody = RequestBody.create(MediaType.parse("text/plain"), title);
        RequestBody descriptionBody = RequestBody.create(
                MediaType.parse("text/plain"), description != null ? description : ""
        );
        RequestBody teacherIdBody = RequestBody.create(
                MediaType.parse("text/plain"), teacherId
        );

        LiveData<Result<MaterialResponse>> liveData =
                repository.updateMaterial(
                        classId, id, titleBody, descriptionBody, filePart, teacherIdBody
                );
        liveData.observeForever(result -> {
            if (result != null) {
                if (result instanceof Result.Loading) {
                    isLoading.postValue(true);
                } else if (result instanceof Result.Success) {
                    errorMsg.postValue("Materi berhasil diubah");

                    isSuccess.postValue(true);
                    isLoading.postValue(false);
                } else if (result instanceof Result.Error<?>) {
                    Result.Error<?> errorResult = (Result.Error<?>) result;
                    errorMsg.postValue("Gagal mengubah materi: " + errorResult.getError());

                    isLoading.postValue(false);
                }
            }
        });
    }

    public void fetchMaterials(int classId) {
        isLoading.postValue(true);

        LiveData<Result<List<MaterialResponse>>> liveData = repository.getMaterialByClassId(classId);
        liveData.observeForever(result -> {
            if (result != null) {
                if (result instanceof Result.Loading) {
                    isLoading.postValue(true);
                } else if (result instanceof Result.Success) {
                    Result.Success<List<MaterialResponse>> successResult =
                            (Result.Success<List<MaterialResponse>>) result;
                    materialItems.postValue(successResult.getData());
                    List<ListTugasKelas> combinedList = new ArrayList<>();
                    successResult.getData().forEach(item -> {
                        ListTugasKelas tugas = new ListTugasKelas(
                                item.getId(),
                                item.getTitle(),
                                item.getClass_id(),
                                "none", // type tidak ada di materials'
                                item.getStatus(),
                                item.getCreated_at(),
                                true // isMateri = true
                        );
                        combinedList.add(tugas);
                    });

                    List<ListTugasKelas> tugas = tugasClassList.getValue();

                    if (tugas != null) {
                        combinedList.addAll(tugas);
                    }

                    tugasClassList.postValue(combinedList);

                    isLoading.postValue(false);
                } else if (result instanceof Result.Error<?>) {
                    Result.Error<?> errorResult = (Result.Error<?>) result;
                    errorMsg.postValue(errorResult.getError());

                    isLoading.postValue(false);
                }
            }
        });
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
                    List<ListTugasKelas> combinedList = new ArrayList<>();
                    successResult.getData().forEach(item -> {
                        ListTugasKelas tugas = new ListTugasKelas(
                                item.getId(),
                                item.getTitle(),
                                item.getClass_id(),
                                item.getType(),
                                item.getStatus(),
                                item.getCreated_at(),
                                false
                        );

                        combinedList.add(tugas);
                    });

                    List<ListTugasKelas> tugas = tugasClassList.getValue();

                    if (tugas != null) {
                        combinedList.addAll(tugas);
                    }

                    tugasClassList.postValue(combinedList);

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
            String scheduleAt, String points, File attachmentFile, String type,
            String options
    ) {
        isLoading.postValue(true);
        isSuccess.postValue(false);

        MultipartBody.Part filePart = null;
        if (attachmentFile != null) {
            RequestBody fileRequestBody = RequestBody.create(
                    MediaType.parse("multipart/form-data"),
                    attachmentFile
            );
            filePart = MultipartBody.Part.createFormData(
                    "attachment",
                    attachmentFile.getName(),
                    fileRequestBody
            );
        }

        RequestBody titleBody = RequestBody.create(MediaType.parse("text/plain"), title);
        RequestBody descriptionBody = RequestBody.create(
                MediaType.parse("text/plain"), description != null ? description : ""
        );
        RequestBody dueDateBody = RequestBody.create(
                MediaType.parse("text/plain"), dueDate != null ? dueDate : ""
        );
        RequestBody scheduleAtBody = RequestBody.create(
                MediaType.parse("text/plain"), scheduleAt != null ? scheduleAt : ""
        );
        RequestBody maxScoreBody = RequestBody.create(
                MediaType.parse("text/plain"), String.valueOf(points)
        );
        RequestBody typeAssignmentBody = RequestBody.create(
                MediaType.parse("text/plain"), type
        );
        RequestBody optionsBody = RequestBody.create(MediaType.parse("text/plain"), options);

        LiveData<Result<ClassAssignmentResponse>> liveData =
                repository.createAssignment(
                        classId, titleBody, descriptionBody, filePart, dueDateBody, maxScoreBody,
                        typeAssignmentBody, optionsBody, scheduleAtBody
                );
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
            int id, String title, String description, String dueDate, String points,
            File attachmentFile, String type, String options
    ) {
        isLoading.postValue(true);
        isSuccess.postValue(false);

        MultipartBody.Part filePart = null;
        if (attachmentFile != null) {
            RequestBody fileRequestBody = RequestBody.create(
                    MediaType.parse("multipart/form-data"),
                    attachmentFile
            );
            filePart = MultipartBody.Part.createFormData(
                    "attachment",
                    attachmentFile.getName(),
                    fileRequestBody
            );
        }

        RequestBody titleBody = RequestBody.create(MediaType.parse("text/plain"), title);
        RequestBody descriptionBody = RequestBody.create(
                MediaType.parse("text/plain"), description != null ? description : ""
        );
        RequestBody dueDateBody = RequestBody.create(
                MediaType.parse("text/plain"), dueDate != null ? dueDate : ""
        );
        RequestBody maxScoreBody = RequestBody.create(
                MediaType.parse("text/plain"), String.valueOf(points)
        );
        RequestBody typeAssignmentBody = RequestBody.create(
                MediaType.parse("text/plain"), type
        );
        RequestBody optionsBody = RequestBody.create(MediaType.parse("text/plain"), options);

        LiveData<Result<ClassAssignmentResponse>> liveData =
                repository.updateAssignment(
                        id, titleBody, descriptionBody, filePart, dueDateBody, maxScoreBody,
                        typeAssignmentBody, optionsBody
                );
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
                    setTugasClassList(null);
                    fetchMaterials(classId);
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

    public void deleteMaterial(int id, int classId) {
        isLoading.postValue(true);

        LiveData<Result<ResponseBody>> liveData = repository.deleteMaterial(id);
        liveData.observeForever(result -> {
            if (result != null) {
                if (result instanceof Result.Loading) {
                    isLoading.postValue(true);
                } else if (result instanceof Result.Success) {
                    errorMsg.postValue("Materi berhasil dihapus");
                    setTugasClassList(null);
                    fetchMaterials(classId);
                    fetchClassAssignment(classId);

                    isLoading.postValue(false);
                } else if (result instanceof Result.Error<?>) {
                    Result.Error<?> errorResult = (Result.Error<?>) result;
                    errorMsg.postValue("Gagal menghapus materi: " + errorResult.getError());

                    isLoading.postValue(false);
                }
            }
        });
    }
}
