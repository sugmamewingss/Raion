<p align="center">
  <img src="app/src/main/res/drawable/icon_app.png" width="120" alt="BinGo Logo"/>
</p>

<h1 align="center">🦖 BinGo - Bin & Go Green!</h1>

<p align="center">
  <b>Aplikasi edukasi lingkungan berbasis gamifikasi untuk anak-anak usia Sekolah Dasar.</b><br/>
  Bersama maskot dinosaurus <i>Gobi</i>, anak-anak belajar memilah sampah, menyelesaikan misi harian, membaca cerita lingkungan, dan mengerjakan kuis - sambil mengumpulkan XP, koin, dan naik level!
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Platform-Android-3DDC84?logo=android&logoColor=white" alt="Android"/>
  <img src="https://img.shields.io/badge/Language-Kotlin-7F52FF?logo=kotlin&logoColor=white" alt="Kotlin"/>
  <img src="https://img.shields.io/badge/UI-Jetpack_Compose-4285F4?logo=jetpackcompose&logoColor=white" alt="Jetpack Compose"/>
  <img src="https://img.shields.io/badge/Backend-Supabase-3FCF8E?logo=supabase&logoColor=white" alt="Supabase"/>
  <img src="https://img.shields.io/badge/Min_SDK-26-blue" alt="Min SDK 26"/>
  <img src="https://img.shields.io/badge/Target_SDK-36-blue" alt="Target SDK 36"/>
</p>

---

## 📋 Daftar Isi

- [Fitur-Fitur Aplikasi](#-fitur-fitur-aplikasi)
- [Tech Stack](#-tech-stack)
- [Arsitektur Aplikasi](#-arsitektur-aplikasi)
- [Cakupan Platform](#-cakupan-platform)
- [Struktur Project](#-struktur-project)
- [Cara Menjalankan](#-cara-menjalankan)

---

## ✨ Fitur-Fitur Aplikasi

### 1. Onboarding `MVP`
Halaman perkenalan aplikasi dengan 3 slide interaktif yang mengenalkan konsep menjaga lingkungan bersama maskot Gobi. Dilengkapi animasi transisi **fade**, page indicator, dan tombol **Lewati/Lanjut/Mulai**. Status onboarding disimpan secara lokal via DataStore agar hanya tampil sekali.

### 2. Autentikasi (Login & Register) `MVP`
Sistem autentikasi lengkap menggunakan **Supabase Auth** dengan fitur:
- **Register** - Input nama lengkap, tanggal lahir (date picker Indonesia), username (cek ketersediaan real-time via RPC), dan password dengan validasi kekuatan.
- **Login** - Login via username & password, dengan opsi **"Ingat Saya"** yang tersimpan di DataStore.
- **Loading Screen** - Setelah registrasi, ditampilkan layar loading edukatif dengan *fun fact* lingkungan acak dan animasi pulsing.
- Latar belakang dekoratif menggunakan komponen **WaveBackground** kustom.

### 3. Beranda / Home Dashboard `MVP`
Dashboard utama yang menampilkan rangkuman lengkap aktivitas pengguna:
- **Profil ringkas** - Avatar, nama, level, XP progress bar, koin, streak status.
- **Kartu Misi Aktif** - Progress misi harian saat ini (`x/5 sampah`), dengan tombol aksi langsung.
- **Quick Nav Menu** - Akses cepat ke Detail Misi, Buku Harian, dan Kuis.
- **Carousel Edukasi** - Artikel edukatif lingkungan dari database (diambil dari tabel `edu_articles`).
- **Leaderboard** - Peringkat 3 pemain teratas berdasarkan XP.
- **Point Shop Preview** - Tampilan singkat item toko yang tersedia.

### 4. Navigasi 4-Tab (Bottom Navigation) `MVP`
Navigasi utama menggunakan **HorizontalPager** dengan 4 tab yang dapat digeser horizontal:

| Tab | Nama | Deskripsi |
|-----|------|-----------|
| 🏠 | **Beranda** | Dashboard, misi aktif, leaderboard, artikel edukasi |
| 📖 | **Cerita** | Baca cerita lingkungan per chapter & episode |
| 🛒 | **Toko** | Beli & pakai item avatar dengan koin |
| 👤 | **Profil** | Info profil, streak, badge, edit profil, logout |

### 5. Misi Harian - Buang Sampah `MVP`
Fitur inti gamifikasi berupa **wizard 7 langkah** untuk mencatat aksi buang sampah:

| Langkah | Layar | Deskripsi |
|---------|-------|-----------|
| 1 | **Journey** | Landing page: profil, progress misi, tombol *"Mulai Kumpulkan!"* |
| 2 | **Intro** | Motivasi dari Gobi: *"Ayo ceritakan aksi barumu!"* |
| 3 | **Pilih Jenis** | Organik atau Daur Ulang |
| 4 | **Pilih Sub-Tipe** | Kategori detail (buah, sayur, kaleng, dll) dari tabel `waste_categories` |
| 5 | **Pilih Lokasi** | Kantin / Ruang Kelas / Halaman / Toilet |
| 6 | **Pilih Jumlah** | 1-4 Buah + Lainnya |
| 7 | **Hasil** | XP & koin yang didapat, opsi lanjut atau selesai |

- **Reward System** - Setiap entri memberikan XP + koin via Supabase RPC (`log_waste_entry`). Bonus +50 XP & +10 koin saat misi selesai.
- **Optimistic UI** - Hasil misi langsung ter-update di Home tanpa menunggu sync backend.
- **Fun Fact Carousel** - Setelah menyelesaikan 5/5 misi, ditampilkan carousel fakta menarik lingkungan.

### 6. Detail Misi
Layar detail yang menampilkan statistik misi pengguna secara menyeluruh:
- Total misi diselesaikan, total XP, total koin
- Profil ringkas (avatar, nama, level)
- Status misi hari ini & tombol mulai misi

### 7. Mission Reward / Mystery Box
Layar reward setelah menyelesaikan seluruh misi harian dengan hadiah spesial.

### 8. Tantangan Jenius (Quiz System) `MVP`
Sistem kuis interaktif dengan struktur hierarkis:
- **Daftar Bab** (`quiz_chapters`) - Bab-bab kuis yang terbuka secara sequential (bab 2 terkunci sampai bab 1 selesai).
- **Daftar Episode** (`quiz_episodes`) - Setiap bab punya beberapa episode kuis.
- **Persiapan Kuis** - Layar pre-quiz dengan informasi episode dan reward yang bisa didapat.
- **Soal Kuis** (`quiz_questions`) - Soal pilihan ganda dengan gambar opsional. 
- **Break Time** - Jeda istirahat di tengah kuis (animasi bernafas).
- **Hasil Kuis** - Jumlah jawaban benar, XP & koin yang didapat (dihitung via RPC `submit_quiz_result_v2`).
- **Pembahasan** - Penjelasan jawaban benar untuk setiap soal.
- **Tracking Progress** - Progress per episode dan per chapter tersimpan di `user_quiz_progress` & `user_chapter_progress`.

### 9. Cerita Lingkungan (Story) `MVP`
Fitur membaca cerita edukatif tentang lingkungan:
- **Daftar Chapter & Episode** - Ditampilkan dalam format horizontal scroll card per chapter.
- **Locking System** - Episode harus dibaca berurutan; belum tersedia ditandai *"COMING SOON"*.
- **Detail Cerita** - Tampilan full-screen dengan navigasi antar episode (Sebelum/Selanjutnya/Selesai).
- **Reward & History** - Setiap penyelesaian episode memberikan +35 XP, +10 koin. Tercatat di `user_story_history` untuk diary.
- **Maskot Ngintip** - Animasi Gobi yang mengintip dari samping layar.

### 10. Buku Harian / Diary `MVP`
Fitur tracking harian dengan kalender interaktif:
- **Streak Display** - Angka streak besar dengan ikon api.
- **Kalender Streak** - Kalender bulat interaktif yang menampilkan hari-hari aktif streak. Hari aktif ditandai warna kuning, hari terakhir streak ditandai ikon api.
- **Rekap Harian** - Popup detail saat tap tanggal streak: jumlah misi selesai, kuis selesai, cerita dibaca, XP & koin diperoleh hari itu.
- **Start Action Popup** - Saat tap hari ini (belum streak), muncul popup pilihan mulai Kuis atau Cerita.

### 11. Toko (Point Shop)
Toko in-app untuk membeli dan memakai item avatar:
- **Character Showcase** - Preview avatar real-time dengan latar alam.
- **Kategori Tab** - Filter item berdasarkan kategori (ikon dinamis dari database).
- **Grid Item** - Tampilan grid 4 kolom, item yang terkunci ditampilkan "Buka di Lv. X".
- **Sistem Beli & Pakai** - Dialog konfirmasi pembelian/pemakaian dengan **Optimistic UI** (perubahan langsung terasa tanpa delay).
- **Auto-Equip** - Otomatis equip setelah beli via RPC `buy_shop_item` + `equip_shop_item`.

### 12. Profil Pengguna
Halaman profil lengkap:
- **Avatar Section** - Tampilan besar avatar saat ini, shortcut ke Toko (Wardrobe).
- **Info Profil** - Nama lengkap, tanggal lahir, level, XP progress.
- **Streak Retention Card** - Kartu motivasi streak, status misi hari ini.
- **Monthly Badges** - Koleksi badge bulanan (placeholder).
- **Edit Profil** - Ubah nama panggilan, nama lengkap, tanggal lahir. Update ke Auth metadata + tabel `profiles`.
- **Logout** - Logout dari Supabase Auth, reset "Ingat Saya".

### 13. Leaderboard
Sistem peringkat pemain berdasarkan total XP:
- Tampilan 3 pemain teratas di Beranda.
- Peringkat pengguna ditampilkan di halaman Quiz.

### 14. Splash Screen
Layar pembuka dengan animasi logo dan pengecekan otomatis:
- Cek sesi aktif Supabase Auth.
- Cek preferensi "Ingat Saya" dari DataStore.
- Redirect otomatis: ke Home (sudah login) / Onboarding (pertama kali) / Auth Selection (pernah buka).

---

## 🛠 Tech Stack

### Bahasa & Framework
| Teknologi | Versi | Keterangan |
|-----------|-------|------------|
| **Kotlin** | 2.1.20 | Bahasa utama pengembangan |
| **Jetpack Compose** | BOM 2024.09.00 | Toolkit UI deklaratif modern |
| **Material 3** | Latest (via BOM) | Design system & komponen UI |
| **Android Gradle Plugin** | 9.0.1 | Build system |

### Backend & Networking
| Teknologi | Versi | Keterangan |
|-----------|-------|------------|
| **Supabase** | 3.0.3 | Backend-as-a-Service (Auth + Postgrest) |
| **Ktor Client** | 3.0.1 | HTTP client untuk Supabase SDK |
| **Kotlinx Serialization** | 1.6.3 | JSON serialization/deserialization |

### Dependency Injection
| Teknologi | Versi | Keterangan |
|-----------|-------|------------|
| **Dagger Hilt** | 2.59.2 | Dependency injection framework |
| **Hilt Navigation Compose** | 1.2.0 | Integrasi Hilt dengan Navigation Compose |
| **KSP** | 2.1.20-2.0.1 | Kotlin Symbol Processing untuk code generation |

### UI & Media
| Teknologi | Versi | Keterangan |
|-----------|-------|------------|
| **Navigation Compose** | 2.7.7 | Navigasi antar layar |
| **Coil Compose** | 2.6.0 | Image loading asinkron dari URL |
| **Lottie Compose** | 6.3.0 | Animasi Lottie (JSON-based) |
| **Google Fonts** | 1.6.2 | Tipografi kustom dari Google Fonts |
| **Material Icons Extended** | Latest (via BOM) | Koleksi ikon Material lengkap |
| **Core Splash Screen** | 1.0.1 | Android 12+ splash screen API |

### Penyimpanan Lokal
| Teknologi | Versi | Keterangan |
|-----------|-------|------------|
| **DataStore Preferences** | 1.0.0 | Penyimpanan key-value reaktif (pengganti SharedPreferences) |

---

## 🏛 Arsitektur Aplikasi

Aplikasi ini menggunakan arsitektur **MVVM (Model-View-ViewModel)** dengan **Repository Pattern** dan **Dependency Injection** via Hilt.

```
┌─────────────────────────────────────────────────────────────┐
│                        UI LAYER                             │
│  ┌──────────────────────────────────────────────────────┐   │
│  │              Jetpack Compose Screens                  │   │
│  │  (Splash, Onboarding, Auth, Home, Mission, Quiz,     │   │
│  │   Story, Diary, Shop, Profile, Edit Profile)         │   │
│  └──────────────────┬───────────────────────────────────┘   │
│                     │ observes StateFlow                    │
│  ┌──────────────────▼───────────────────────────────────┐   │
│  │                  ViewModels                           │   │
│  │  (HomeVM, LoginVM, RegisterVM, MissionVM, QuizVM,    │   │
│  │   StoryVM, DiaryVM, EditProfileVM, SplashVM,         │   │
│  │   OnboardingVM)                                      │   │
│  └──────────────────┬───────────────────────────────────┘   │
├─────────────────────┼───────────────────────────────────────┤
│                DATA LAYER                                   │
│  ┌──────────────────▼───────────────────────────────────┐   │
│  │               Repositories                            │   │
│  │  ┌──────────────┬────────────────┬───────────────┐   │   │
│  │  │ AuthRepo     │  HomeRepo      │  StoryRepo    │   │   │
│  │  │ (Auth,       │  (Profile,     │  (Chapters,   │   │   │
│  │  │  Profile,    │   Missions,    │   Episodes,   │   │   │
│  │  │  Streak)     │   Quiz, Shop,  │   Progress,   │   │   │
│  │  │              │   Leaderboard, │   Rewards)    │   │   │
│  │  │              │   Diary, Waste)│               │   │   │
│  │  └──────────────┴────────────────┴───────────────┘   │   │
│  └──────────────────┬───────────────────────────────────┘   │
│                     │                                       │
│  ┌──────────────────▼───────────────────────────────────┐   │
│  │            Data Sources                               │   │
│  │  ┌─────────────────────┐  ┌───────────────────────┐  │   │
│  │  │   Supabase Client   │  │   DataStore            │  │   │
│  │  │   (Auth + Postgrest │  │   (UserPreferences)    │  │   │
│  │  │    + RPC Functions) │  │                        │  │   │
│  │  └─────────────────────┘  └───────────────────────┘  │   │
│  └──────────────────────────────────────────────────────┘   │
├─────────────────────────────────────────────────────────────┤
│                   DI LAYER                                  │
│  ┌──────────────────────────────────────────────────────┐   │
│  │   Hilt Modules (SupabaseModule)                       │   │
│  │   @Singleton SupabaseClient → Auth + Postgrest       │   │
│  └──────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────┘
```

### Penjelasan Arsitektur

#### UI Layer
- **Composable Screens** - Setiap layar dibangun dengan Jetpack Compose sebagai fungsi `@Composable`.
- **ViewModels** - Mengelola state UI menggunakan `StateFlow` dan `MutableStateFlow`. Di-inject via `@HiltViewModel`.
- **Navigation** - Dikelola oleh `AppNavigation.kt` menggunakan `NavHost` dengan custom transition animations (fade, slide).

#### Data Layer
- **Repository Pattern** - 3 repository utama (`AuthRepository`, `HomeRepository`, `StoryRepository`) yang meng-abstraksi akses data dari UI.
- **Supabase Client** - Berkomunikasi dengan backend Supabase untuk Auth (login/register/session), Postgrest (CRUD tabel), dan RPC (fungsi database kustom).
- **DataStore** - Menyimpan preferensi lokal (status onboarding, "Ingat Saya").

#### DI Layer
- **Hilt** - `SupabaseModule` menyediakan singleton `SupabaseClient` yang di-inject ke semua repository.
- **KSP** - Digunakan untuk code generation Hilt.

### Tabel Database Supabase

| Tabel | Deskripsi |
|-------|-----------|
| `profiles` | Data profil pemain (XP, level, koin, streak, avatar) |
| `daily_missions` | Tracker misi harian per hari |
| `waste_entries` | Log entri sampah per misi |
| `waste_categories` | Kategori & sub-kategori sampah |
| `edu_articles` | Artikel edukasi lingkungan |
| `point_shop` | Item toko (avatar, kostum) |
| `shop_categories` | Kategori toko |
| `user_inventory` | Item yang dimiliki pengguna |
| `quiz_chapters` | Bab-bab kuis |
| `quiz_episodes` | Episode per bab kuis |
| `quiz_questions` | Soal-soal kuis (pilihan ganda) |
| `user_quiz_progress` | Progress kuis per episode |
| `user_chapter_progress` | Progress kuis per chapter |
| `user_quiz_history` | Riwayat pengerjaan kuis (untuk diary) |
| `story_chapters` | Bab-bab cerita |
| `story_episodes` | Episode cerita per bab |
| `user_story_progress` | Progress cerita pengguna |
| `user_story_history` | Riwayat baca cerita (untuk diary) |
| `master_tasks` | Daftar master task aktif |
| `user_tasks` | Task yang sudah diselesaikan pengguna |

### RPC Functions (Server-Side Logic)

| Fungsi | Deskripsi |
|--------|-----------|
| `check_username_available` | Cek ketersediaan username saat registrasi |
| `update_daily_streak` | Update streak harian pengguna |
| `log_waste_entry` | Log entri sampah + hitung reward (XP, koin) |
| `buy_shop_item` | Proses pembelian item toko (validasi koin + level) |
| `equip_shop_item` | Equip item avatar ke profil pengguna |
| `submit_quiz_result_v2` | Submit hasil kuis + hitung reward |
| `log_story_history` | Log riwayat baca cerita + reward |

---

## 📱 Cakupan Platform

| Platform | Status | Keterangan |
|----------|--------|------------|
| **Android** | ✅ Didukung | Platform utama. Min SDK 26 (Android 8.0), Target SDK 36. |
| **iOS** | ❌ Tidak tersedia | Proyek ini adalah native Android, bukan multiplatform. |
| **Web** | ❌ Tidak tersedia | Proyek ini tidak menggunakan Compose Multiplatform. |

> **Catatan:** Aplikasi ini dikembangkan sebagai **Native Android** menggunakan Kotlin + Jetpack Compose - **bukan** Flutter maupun Kotlin Multiplatform. Oleh karena itu, hanya tersedia untuk platform **Android**.

---

## 📁 Struktur Project

```
app/src/main/java/com/example/raion/
├── MainActivity.kt                 # Activity utama (entry point)
├── RaionApplication.kt             # @HiltAndroidApp Application class
│
├── data/
│   ├── local/
│   │   └── UserPreferences.kt      # DataStore preferences (onboarding, remember me)
│   ├── model/
│   │   ├── HomeModels.kt           # Model: Mission, Waste, Article, Leaderboard, Shop
│   │   ├── MissionModels.kt        # Model: MissionStep enum, MissionUiState
│   │   ├── StoryModels.kt          # Model: Story chapters, episodes, progress
│   │   ├── UserProfile.kt          # Model: User profile (XP, level, coins, streak)
│   │   ├── TaskModels.kt           # Model: Master tasks, user tasks
│   │   ├── DailyHistory.kt         # Model: Daily recap for diary
│   │   ├── DailyMissionEntry.kt    # Model: Daily mission DB row
│   │   └── quiz/
│   │       └── QuizModels.kt       # Model: Quiz chapters, episodes, questions, progress
│   └── repository/
│       ├── AuthRepository.kt       # Auth, profile CRUD, streak, trash disposal
│       ├── HomeRepository.kt       # Dashboard data, missions, quiz, shop, diary
│       └── StoryRepository.kt      # Story chapters, episodes, progress, rewards
│
├── di/
│   └── SupabaseModule.kt           # Hilt module: Supabase client provider
│
├── navigation/
│   └── AppNavigation.kt            # NavHost dengan 15+ routes + transitions
│
└── ui/
    ├── features/
    │   ├── auth/                    # Login, Register, AuthSelection
    │   │   ├── components/          # WaveBackground, AuthButtons, RegisterComponents
    │   │   └── register/            # RegisterScreen + RegisterViewModel
    │   ├── diary/                   # DiaryScreen + DiaryViewModel (streak calendar)
    │   ├── home/                    # HomeScreen, HomeComponents, HomeViewModel
    │   ├── mission/                 # MissionScreen (wizard), MissionDetail, MissionReward
    │   ├── onboarding/              # OnboardingScreen + components (PageIndicator, Buttons)
    │   ├── profile/                 # ProfileScreen, EditProfile, ProfileComponents
    │   ├── quiz/                    # QuizScreen, Episode, Prep, Question, Break, Complete, Explanation
    │   ├── shop/                    # ShopScreen (category tabs, item grid, buy/equip dialog)
    │   ├── splash/                  # SplashScreen, LoadingScreen (educational trivia)
    │   └── story/                   # StoryScreen, StoryDetailScreen, StoryViewModel
    ├── theme/
    │   ├── Color.kt                 # Definisi warna
    │   ├── DesignTokens.kt          # Design tokens (Colors, Dimensions)
    │   ├── Theme.kt                 # Material 3 theme
    │   └── Type.kt                  # Tipografi
    └── util/                        # Utility functions (formatCompactNumber, dll)
```

---

## 🚀 Cara Menjalankan

### Prasyarat
- **Android Studio** Ladybug (2024.2.x) atau lebih baru
- **JDK 11** atau lebih tinggi
- **Android SDK** dengan API Level 26+
- Akun **Supabase** dengan project yang sudah disiapkan

### Setup

1. **Clone repository**
   ```bash
   git clone https://github.com/sugmamewingss/Raion.git
   cd Raion
   ```

2. **Konfigurasi Supabase**
   
   Buat/edit file `local.properties` di root project dan tambahkan:
   ```properties
   SUPABASE_URL=https://your-project.supabase.co
   SUPABASE_ANON_KEY=your-anon-key-here
   ```

3. **Sync & Build**
   - Buka project di Android Studio
   - Tunggu Gradle sync selesai
   - Jalankan di emulator atau device fisik (min API 26)

---

<p align="center">
  <i>Dibuat dengan 💚 oleh Tim BinGo - Kelompok 5 Intern Raion</i>
</p>
