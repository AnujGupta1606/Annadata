# 🚀 Annadata – Food Waste Reduction & Donation App

Annadata is a mobile-based platform designed to **reduce food waste and fight hunger** by connecting food donors with nearby NGOs in real-time. The app ensures that surplus food reaches people in need quickly, efficiently, and safely.

---

## 📌 Problem Statement

In many places, **food wastage and hunger exist simultaneously**. Restaurants, households, and events often have surplus food, while many individuals struggle to access even a single meal.

The challenge is not just food availability — it’s **lack of coordination** between donors and receivers.

---

## 💡 Solution

Annadata provides a **smart, real-time platform** that:

* Connects donors with nearby NGOs
* Uses location-based filtering for quick matching
* Tracks donation lifecycle from posting to completion
* Reduces delays using real-time updates

---

## ✨ Features

### 👤 Role-Based System

* **Donor:** Add food donation with details and location
* **NGO:** View nearby donations and take action

### 📍 Location-Aware Matching

* Uses GPS + Haversine formula
* Shows only nearby relevant donations

### ⚡ Real-Time Updates

* Powered by Firebase Firestore
* No manual refresh needed

### 🔄 Donation Lifecycle Tracking

```
Pending → Accepted → Completed
```

### 🤖 AI Chatbot Support

* Integrated using Gemini API
* Helps users navigate and understand the app

---

## 🏗️ System Architecture

```
[Android App (Kotlin)]
        |
        | Firebase Auth
        |
[Cloud Firestore Database]
        |
 Real-time Listeners
        |
[NGO Dashboard Filtering + Matching]
```

---

## 🧠 Key Concepts Used

* Role-based access control
* NoSQL data modeling
* Real-time data synchronization
* Geo-location filtering
* Event-driven architecture

---

## ⚙️ Tech Stack

| Category       | Technology                |
| -------------- | ------------------------- |
| Language       | Kotlin                    |
| Platform       | Android                   |
| UI             | AndroidX, Material Design |
| Authentication | Firebase Auth             |
| Database       | Cloud Firestore           |
| Location       | Android LocationManager   |
| Algorithm      | Haversine Distance        |
| AI Integration | Gemini API                |
| Build Tool     | Gradle (Kotlin DSL)       |

---

## 📊 Data Model

### Users Collection

* userId
* role (Donor / NGO)
* ngoLatitude
* ngoLongitude
* ngoRadiusKm

### Donations Collection

* foodDetails
* donorLatitude
* donorLongitude
* contactInfo
* status
* timestamp

---

## 🔄 Workflow

### 🧑‍🍳 Donor Flow

```
Login → Add Food → Capture Location → Submit → Status = Pending
```

### 🏢 NGO Flow

```
Login → Load Dashboard → Filter by Distance → Accept Donation → Mark Completed
```

---

## 📷 Screenshots

*(Add your app screenshots here)*

* Login Screen
* Donation Screen
* NGO Dashboard
* Chatbot Interface

---

## 🚧 Challenges Faced

* Real-time synchronization handling
* Accurate location-based filtering
* Designing simple yet effective UI
* Managing role-based navigation

---

## 🔮 Future Enhancements

* Push notifications for instant alerts
* Map-based donation tracking
* NGO verification system
* Image upload for food posts
* Rating & feedback system
* Geo-query optimization (Geohash)
* Offline support

---

## 🌍 Impact

Annadata aims to:

* Reduce food wastage
* Improve food accessibility
* Strengthen community support
* Enable fast and reliable food redistribution

---

## 🤝 Contributing

Contributions are welcome!
Feel free to fork the repo and submit a pull request.

---

## 📬 Contact

👤 **Anuj Gupta**
💼 BTech CSE Student
🚀 Passionate about building tech for social impact

---

## ⭐ Show Your Support

If you like this project, give it a ⭐ on GitHub!

---

