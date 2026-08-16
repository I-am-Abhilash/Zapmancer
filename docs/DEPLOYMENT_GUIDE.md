# Zapmancer Deployment & Hosting Guide

This guide provides step-by-step instructions for building, hosting, and deploying all three major targets of the **Zapmancer** platform:
1. **Ktor Backend Server** (`:server`)
2. **React Web Application** (`app/webApp`)
3. **Android Mobile Application** (`:app:androidApp`)

---

## 1. Hosting the Backend Server (`server/`)

The backend is built with Ktor JVM, Exposed ORM, and PostgreSQL.

### Option A: Docker Deployment (Recommended)

#### Step 1: Build the Server Installation Artifact
```bash
./gradlew :server:installDist
```

#### Step 2: Build the Docker Image
```bash
docker build -t zapmancer-backend .
```

#### Step 3: Run the Docker Container Locally
```bash
docker run -p 8080:8080 \
  -e PORT=8080 \
  -e DATABASE_URL="jdbc:postgresql://host.docker.internal:5432/zapmancer" \
  -e DB_USER="postgres" \
  -e DB_PASSWORD="your_password" \
  zapmancer-backend
```

### Option B: Deploying to Cloud Providers

| Cloud Provider | Type | Deployment Steps |
| :--- | :--- | :--- |
| **Render** | Web Service | 1. Connect GitHub Repository<br/>2. Environment: `Docker`<br/>3. Build Command: `./gradlew :server:installDist`<br/>4. Attach Managed PostgreSQL database |
| **Railway** | Docker Container | 1. Import repository into Railway<br/>2. Add PostgreSQL plugin<br/>3. Railway automatically detects `Dockerfile` and deploys on port 8080 |
| **AWS App Runner / ECS**| Container Registry | 1. Push image to AWS ECR: `docker push <aws_account>.dkr.ecr.<region>.amazonaws.com/zapmancer-backend`<br/>2. Create App Runner service listening on port 8080 |

---

## 2. Hosting the Web Application (`app/webApp`)

The web application is built with React, Vite, and Tailwind CSS, consuming the compiled KMP `sharedLogic` module.

### Step 1: Compile KMP Shared Logic
```bash
./gradlew :app:sharedLogic:build
```

### Step 2: Configure Environment Variables
Create or update `app/webApp/.env.production`:
```env
VITE_API_BASE_URL=https://api.yourdomain.com
VITE_WS_BASE_URL=wss://api.yourdomain.com/ws
```

### Step 3: Build Production Assets
```bash
cd app/webApp
npm run build
```
This generates optimized static HTML/JS/CSS assets inside `app/webApp/dist/`.

### Step 4: Host Static Bundle

| Hosting Provider | Command / Method |
| :--- | :--- |
| **Vercel** | Run `vercel --prod` inside `app/webApp` (Root Directory: `app/webApp`, Output Directory: `dist`) |
| **Netlify** | Connect GitHub repo, set Base directory: `app/webApp`, Build command: `npm run build`, Publish directory: `app/webApp/dist` |
| **Cloudflare Pages**| Direct upload of `app/webApp/dist` folder or GitHub Integration |
| **Nginx (VPS)** | Copy `dist/` contents to `/var/www/html` and configure Nginx `try_files $uri $uri/ /index.html;` for React Router |

---

## 3. Building & Distributing the Android App (`app/androidApp`)

### Option A: Build Debug APK (For Immediate Testing)

```bash
./gradlew :app:androidApp:assembleDebug
```
The output APK file will be generated at:
`app/androidApp/build/outputs/apk/debug/app-debug.apk`

You can install it directly onto any connected Android device using `adb`:
```bash
adb install app/androidApp/build/outputs/apk/debug/app-debug.apk
```

### Option B: Build Signed Release App Bundle (Play Store)

1. Configure keystore variables in `gradle.properties`:
   ```properties
   MYAPP_RELEASE_STORE_FILE=my-release-key.keystore
   MYAPP_RELEASE_KEY_ALIAS=my-key-alias
   MYAPP_RELEASE_STORE_PASSWORD=*****
   MYAPP_RELEASE_KEY_PASSWORD=*****
   ```
2. Build Android App Bundle (`.aab`):
   ```bash
   ./gradlew :app:androidApp:bundleRelease
   ```
3. Upload `.aab` output from `app/androidApp/build/outputs/bundle/release/` to **Google Play Console** or **Firebase App Distribution**.

---

## 4. Connecting Everything: CORS & Network Checklist

To ensure the hosted Web App and Mobile App can communicate with your hosted Backend:

1. **Configure CORS in Ktor Server**:
   Ensure `server/src/main/kotlin/.../Application.kt` permits your web domain:
   ```kotlin
   install(CORS) {
       allowHost("your-web-app.vercel.app", schemes = listOf("https"))
       allowHeader(HttpHeaders.Authorization)
       allowHeader(HttpHeaders.ContentType)
   }
   ```
2. **WebSocket Support**:
   If using WebSockets for messaging/notifications, ensure your server proxy (e.g. Nginx, Cloudflare) has WebSocket Upgrade headers enabled (`Upgrade: websocket`, `Connection: Upgrade`).
