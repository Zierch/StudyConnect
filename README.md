# StudyConnect 📱

Aplikasi mobile **StudyConnect** — tugas akhir SMK. Aplikasi untuk mengelola kelas,
tugas, dan forum diskusi antara guru dan siswa.

> 🔗 API backend-nya ada di repo terpisah: **[StudyConnect-API](https://github.com/Zierch/StudyConnect-API)** (PHP)

## Fitur

- Daftar kelas & siswa
- Tugas (assignment) — pilihan ganda, esai, dan lampiran
- Forum diskusi per kelas (teks & gambar)

## Tech stack

| Bagian | Teknologi |
|---|---|
| Bahasa | Java |
| UI | XML + ViewBinding, Material Design, Navigation Component |
| Dependency injection | Dagger Hilt |
| HTTP client | Retrofit + Gson |
| IDE | Android Studio |

## Struktur

```
app/src/main/java/com/example/studyconnect/
├── data/            # Retrofit API service
├── di/              # Modul Dependency Injection (Hilt)
├── ui/class_ui/     # Fragment kelas, tugas, forum, daftar siswa
└── utils/           # SharedPreferences manager
```

## Catatan

Aplikasi ini terhubung ke API di `http://127.0.0.1:8000/api/` (development).
Untuk menjalankannya, nyalakan server [StudyConnect-API](https://github.com/Zierch/StudyConnect-API)
terlebih dahulu, lalu sesuaikan `baseUrl` di `AppModule.java`.
