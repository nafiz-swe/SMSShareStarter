# SMS Share Starter (User + Admin APK)

This is a starter project for building two Android APKs with GitHub Actions:
- `user` APK: user manually pastes/selects one SMS and taps **Share to Admin**.
- `admin` APK: admin signs in and views messages shared by users, searchable by sender/phone number.

**Privacy by design:** this starter does not request READ_SMS or RECEIVE_SMS permission and does not silently read a phone's inbox. Users must deliberately paste/select a message and tap Share.

## Build
1. Create a GitHub repository and upload this folder's contents.
2. In GitHub, open **Actions** and enable workflows.
3. Run **Build Android APKs** (or push to `main`).
4. Download `user-debug.apk` and `admin-debug.apk` from the workflow's Artifacts.

## Backend configuration required
The apps need a backend URL. In `app/build.gradle.kts`, replace `https://YOUR_BACKEND_HOST` with your HTTPS API base URL before building.
This starter's Android screens are a UI scaffold; connect the API calls to your authenticated backend before using it with real messages. Do not publish it with a public/no-auth message endpoint.

## Suggested API contract
Implement an HTTPS backend with authentication and per-user access control:
- `POST /api/messages` (User auth): `{ "senderNumber": "...", "messageText": "...", "consent": true }`
- `GET /api/admin/messages?search=...` (Admin auth only): returns messages grouped/labelled by registered user.
- `POST /api/auth/login`: returns a short-lived access token and role.
- `DELETE /api/messages/{id}` (authorized owner/admin according to policy).

Recommended protections: TLS, hashed passwords, short-lived tokens, rate limits, audit logs, user deletion/export, retention limits, and server-side admin-role enforcement. Never put admin credentials in the APK.
