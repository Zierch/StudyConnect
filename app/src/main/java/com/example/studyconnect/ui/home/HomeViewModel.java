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
import com.example.studyconnect.utils.SharedPreferencesManager;

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
    private final MutableLiveData<List<ClassResponse>> classes = new MutableLiveData<>();
    private final MutableLiveData<RegisterResponse> user = new MutableLiveData<>();

    @Inject
    @ApplicationContext
    Context context;

    @Inject
    public HomeViewModel(StudyRepository repository) {
        this.repository = repository;
        fetchClasses();
    }

    public LiveData<List<ClassResponse>> getClasses() {
        return classes;
    }

    public LiveData<RegisterResponse> getUser(int id) {
        fetchUser(id);
        return user;
    }

    public void fetchClasses() {
        repository.getClasses().enqueue(new Callback<List<ClassResponse>>() {
            @Override
            public void onResponse(Call<List<ClassResponse>> call, Response<List<ClassResponse>> response) {
                if (response.isSuccessful()) {
                    classes.postValue(response.body());
                    Log.d("KELAS", "onResponse: " + response.body());
                }
            }

            @Override
            public void onFailure(Call<List<ClassResponse>> call, Throwable t) {
                Toast.makeText(context, "Failed to fetch classes: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchUser(int id) {
        repository.getUser(id).enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                if (response.isSuccessful()) {
                    user.postValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                Toast.makeText(context, "Failed to fetch user: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void joinClass(int id) {
        repository.joinClass(id).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(context, "Berhasil bergabung ke kelas", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("joinClass", "onFailure: " + t.getMessage());
                Toast.makeText(context, "Gagal bergabung ke kelas", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void leaveClass(int id) {
        repository.leaveClass(id).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(context, "Berhasil meninggalkan kelas", Toast.LENGTH_SHORT).show();
                    fetchClasses();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("leaveClass", "onFailure: " + t.getMessage());
                Toast.makeText(context, "Gagal meninggalkan kelas", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void deleteClass(int id) {
        repository.deleteClass(id).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(context, "Kelas berhasil dihapus", Toast.LENGTH_SHORT).show();
                    fetchClasses();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("deleteClass", "onFailure: " + t.getMessage());
                Toast.makeText(context, "Gagal menghapus kelas", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
