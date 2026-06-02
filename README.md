# Customer Manager & WhatsApp App

A professional Android application for managing customers and sending WhatsApp messages with complete license activation system powered by Firebase.

## 🎯 Features

### 1. **License Activation System** 🔐
- Firebase-based license validation
- Device-locked licenses (Device ID)
- Remote enable/disable/block
- License expiration support
- Control licenses from anywhere

### 2. **Customer Management** 👥
- Add, edit, delete customers
- Store: Name, Phone Number, Bill Number
- Country code picker (8 countries)
- Auto-clean phone numbers
- Auto-save to phone contacts
- No duplicate contacts

### 3. **WhatsApp Integration** 💬
- Standard WhatsApp
- Business WhatsApp option
- Pre-written Arabic messages
- One-click send
- Message history tracking

### 4. **Order Management** 📋
- Active orders list
- Send/Edit buttons per order
- Auto-move to history after sending
- Persistent history with timestamps

### 5. **Multi-Language Support** 🌍
- English & Arabic UI
- RTL support for Arabic
- Instant language switching
- Saved language preference

### 6. **Data Persistence** 💾
- Room Database (local storage)
- Firebase integration
- Automatic sync
- Never loses data

## 🛠 Tech Stack

- **Language:** Kotlin
- **Architecture:** MVVM + LiveData
- **Database:** Room + Firebase
- **Backend:** Firebase Firestore
- **UI:** Material Design 3
- **Minimum SDK:** Android 8.0+ (API 26)

## 📋 Requirements

- Android 8.0+ (API 26)
- WhatsApp installed
- Internet connection for license validation
- Firebase project configured

## 🚀 Getting Started

1. Clone this repository
2. Open in Android Studio
3. Build and run on device/emulator

## 🔑 License Validation

The app validates licenses against Firebase Firestore. Create a license document with:
- Document ID: Your license key
- Fields: licenseKey, isActive, expiresAt, blockedReason, allowedDevices, createdAt, businessName

## 📝 Firebase Configuration

Update `google-services.json` with your Firebase credentials:
- Project ID: customermanagerapp-87ecb
- Project number: 981617594819

## 👨‍💻 Developer

Created with ❤️ for efficient business management