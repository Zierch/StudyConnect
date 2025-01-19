package com.example.studyconnect.utils;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.provider.OpenableColumns;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;

import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.widget.AppCompatRadioButton;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.FitCenter;
import com.example.studyconnect.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;
import java.util.TimeZone;

public class Helper {
    public static final int REQUEST_CODE_PICK_FILE = 1001;
    public static final int REQUEST_CODE_PICK_IMAGE = 1002;
    public static final int REQUEST_CODE_CAPTURE_IMAGE = 1003;
    private static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyz0123456789";
    private static final int LENGTH = 8;

    public static void showLoading(FrameLayout progressBar, Boolean isLoading) {
        if (isLoading) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }

    public static File createImageFile(Context context) {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmm", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        try {
            return File.createTempFile(
                    imageFileName,  /* prefix */
                    ".jpg",         /* suffix */
                    storageDir      /* directory */
            );
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getFileName(String attachment_path) {
        if (attachment_path == null || attachment_path.isEmpty()) {
            return "";
        }

        int lastSlashIndex = attachment_path.lastIndexOf('/');

        if (lastSlashIndex == -1) {
            return attachment_path;
        }

        return attachment_path.substring(lastSlashIndex + 1);
    }

    public static File copyFileToAppDirectory(Context context, Uri uri) {
        String fileName = getFileNameFromUri(context, uri);
        if (fileName == null) {
            fileName = "temp_file";
        }

        File outputFile = new File(context.getCacheDir(), fileName);

        try (InputStream inputStream = context.getContentResolver().openInputStream(uri);
             FileOutputStream outputStream = new FileOutputStream(outputFile)) {
            if (inputStream != null) {
                byte[] buffer = new byte[1024];
                int length;
                while ((length = inputStream.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, length);
                }
                return outputFile;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @SuppressLint("Range")
    public static String getFileNameFromUri(Context context, Uri uri) {
        String fileName = null;

        if (Objects.equals(uri.getScheme(), "content")) {
            try (Cursor cursor = context.getContentResolver().query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    fileName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            }
        }
        if (fileName == null) {
            String path = uri.getPath();

            if (path != null) {
                int cut = path.lastIndexOf('/');

                if (cut != -1) {
                    fileName = path.substring(cut + 1);
                }
            }
        }

        return fileName;
    }

    public static String formatDateYMDHI(String inputDate) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        inputFormat.setTimeZone(TimeZone.getTimeZone("GMT+7"));

        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());

        try {
            Date date = inputFormat.parse(inputDate);

            assert date != null;
            return outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String formatDate(String textStartDate, String inputDate) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
        inputFormat.setTimeZone(TimeZone.getTimeZone("GMT+7"));

        SimpleDateFormat outputFormat =
                new SimpleDateFormat(
                        " dd MMM 'jam' HH:mm",
                        new Locale("id", "ID")
                );

        try {
            Date date = inputFormat.parse(inputDate);

            assert date != null;
            return textStartDate + outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return "Format tanggal tidak valid";
        }
    }

    public static String formatDateYMDHI(String textStartDate, String inputDate) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        inputFormat.setTimeZone(TimeZone.getTimeZone("GMT+7"));

        SimpleDateFormat outputFormat =
                new SimpleDateFormat(
                        " dd MMM 'jam' HH:mm",
                        new Locale("id", "ID")
                );

        try {
            Date date = inputFormat.parse(inputDate);

            assert date != null;
            return textStartDate + outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return "Format tanggal tidak valid";
        }
    }

    public static boolean hasNetworkConnection(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnected();
    }

    public static void startActivity(Class<?> targetActivity, Context context) {
        Intent intent = new Intent(context, targetActivity);
        context.startActivity(intent);
    }

    public static void startFlagedActivity(Class<?> targetActivity, Context context) {
        Intent intent = new Intent(context, targetActivity);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static String generateRandomString() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder(LENGTH);
        for (int i = 0; i < LENGTH; i++) {
            int randomIndex = random.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(randomIndex));
        }
        return sb.toString();
    }

    public static void showTimePickerDialog(
            Context context, int selectedYear, int selectedMonth, int selectedDay,
            StringCallback callback
    ) {
        Calendar timeCalendar = Calendar.getInstance();
        int hour = timeCalendar.get(Calendar.HOUR_OF_DAY);
        int minute = timeCalendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(
                context,
                (timeView, selectedHour, selectedMinute) -> {
                    String pickDate = String.format(
                            Locale.getDefault(),
                            "%04d-%02d-%02d %02d:%02d",
                            selectedYear,
                            selectedMonth + 1,
                            selectedDay,
                            selectedHour,
                            selectedMinute
                    );

                    callback.onStringSelected(pickDate);
                },
                hour, minute, true
        );

        timePickerDialog.show();
    }

    public static void showDatePickerDialog(Context context, DateCallback dateCallback) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(
                context,
                (view, selectedYear, selectedMonth, selectedDay) ->
                        dateCallback.onDateSelected(selectedYear, selectedMonth, selectedDay),
                year, month, day
        );
        dialog.show();
    }

    public static void loadImageWithGlide(Context context, String imageUrl, ImageView imageView) {
        String newImageUrl = "https://studyconnect-api-production.up.railway.app/storage/"+imageUrl;
        Glide.with(context)
                .load(newImageUrl)
                .optionalCenterCrop()
                .into(imageView);
    }

    public static void loadImageWithGlide(Context context, File imageFile, ImageView imageView) {
        Glide.with(context)
                .load(imageFile)
                .optionalCenterCrop()
                .into(imageView);
    }

    public static String getFileNameFromPath(String attachmentPath) {
        return attachmentPath.substring(attachmentPath.lastIndexOf("_") + 1);
    }

    public static void addRadioButtonsFromJsonArray(Context context, RadioGroup radioGroup, String jsonArrayString) {
        try {
            // Parsing JSON array
            JSONArray optionsArray = new JSONArray(jsonArrayString);

            // Menambahkan RadioButton ke RadioGroup
            for (int i = 0; i < optionsArray.length(); i++) {
                String option = optionsArray.getString(i);

                // Membuat RadioButton baru
                AppCompatRadioButton radioButton = new AppCompatRadioButton(context);
                radioButton.setText(option);
                radioButton.setTextColor(context.getResources().getColor(R.color.black));
                radioButton.setPadding(10, 0, 0, 0);
                // Mengatur LayoutParams untuk RadioButton
                RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(
                        RadioGroup.LayoutParams.MATCH_PARENT, // Lebar match_parent
                        RadioGroup.LayoutParams.WRAP_CONTENT  // Tinggi wrap_content
                );
                radioButton.setLayoutParams(params);

                // Menambahkan RadioButton ke RadioGroup
                radioGroup.addView(radioButton);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void setSelectedRadioButton(RadioGroup radioGroup, String answer) {
        if (radioGroup == null || answer == null || answer.isEmpty()) {
            return;
        }

        for (int i = 0; i < radioGroup.getChildCount(); i++) {
            View view = radioGroup.getChildAt(i);
            if (view instanceof RadioButton) {
                RadioButton radioButton = (RadioButton) view;

                if (radioButton.getText().toString().equals(answer)) {
                    radioButton.setChecked(true);
                    break;
                }
            }
        }
    }

    public static void showFullScreenImageDialog(Context context, String imageUrl) {
        if (imageUrl.isEmpty()){
            return;
        }
        // Buat dialog
        String newImageUrl = "https://studyconnect-api-production.up.railway.app/storage/"+imageUrl;
        final Dialog dialog = new Dialog(context, android.R.style.Theme_DeviceDefault_NoActionBar_Fullscreen);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.fullscreen_image_dialog);

        // Inisialisasi view
        ImageView fullscreenImageView = dialog.findViewById(R.id.fullscreenImageView);
        ImageView closeButton = dialog.findViewById(R.id.closeButton);

        // Gunakan Glide untuk memuat gambar
        Glide.with(context)
                .load(newImageUrl)
                .fitCenter()
                .into(fullscreenImageView);

        // Tutup dialog saat tombol close diklik
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        // Tampilkan dialog
        dialog.show();
    }
}
