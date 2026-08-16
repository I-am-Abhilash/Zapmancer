# Azure Deployment Guide: Hosting Backend, Database & WebApp

This guide explains step-by-step how to host **Zapmancer Backend**, **PostgreSQL Database**, and **React Web App** on **Microsoft Azure**, and connect your local Android App to the live cloud server.

---

## Architecture Overview on Azure

```mermaid
flowchart TD
    subgraph Azure ["Microsoft Azure Cloud"]
        Web ["Azure Static Web Apps<br/>(React WebApp)"]
        Backend ["Azure App Service (Linux Container)<br/>(Ktor Server on port 8080)"]
        DB [("PostgreSQL on Ubuntu 24.04 VM<br/>OR Azure Managed Postgres")]
    end

    subgraph Local ["Local Machine"]
        Android ["Local Android App / Emulator"]
    end

    Web -->|HTTP / WebSockets| Backend
    Android -->|HTTP / WebSockets| Backend
    Backend -->|SQL Queries| DB
```

---

## Step 1: Database Setup Options

You have two choices for hosting PostgreSQL on Azure:

- **Option A (Custom / Budget Friendly)**: Self-host PostgreSQL on an **Ubuntu 24.04 LTS Azure Virtual Machine**. (Cheapest option, total control).
- **Option B (Fully Managed)**: Use **Azure Database for PostgreSQL (Flexible Server)**. (Managed backups & patches).

---

### Option A: PostgreSQL on Ubuntu 24.04 LTS VM (Recommended for Control & Budget)

#### 1. Create Ubuntu 24.04 Virtual Machine on Azure
1. Go to [Azure Portal](https://portal.azure.com) $\rightarrow$ Search **Virtual Machines** $\rightarrow$ Click **Create**.
2. Settings:
   - **Resource Group**: `zapmancer-rg`
   - **Virtual machine name**: `zapmancer-db-vm`
   - **Image**: **Ubuntu Server 24.04 LTS - x64 Gen2**
   - **Size**: `Standard_B1s` or `Standard_B2s` (low cost / eligible for free tier)
   - **Authentication**: SSH Public Key or Password.
3. In **Networking**, ensure port `22` (SSH) is enabled.

#### 2. Install PostgreSQL on Ubuntu 24.04
SSH into your Ubuntu VM:
```bash
ssh azureuser@<YOUR_VM_PUBLIC_IP>
```

Run installation commands:
```bash
# 1. Update packages & install PostgreSQL 16
sudo apt update && sudo apt upgrade -y
sudo apt install -y postgresql postgresql-contrib

# 2. Start and enable PostgreSQL service
sudo systemctl enable postgresql
sudo systemctl start postgresql
```

#### 3. Create Database & User
```bash
# Switch to postgres user and open psql prompt
sudo -u postgres psql
```

Inside the `psql` console, run:
```sql
CREATE DATABASE zapmancer;
CREATE USER zapadmin WITH ENCRYPTED PASSWORD 'your_secure_password';
GRANT ALL PRIVILEGES ON DATABASE zapmancer TO zapadmin;
\q
```

#### 4. Configure Remote Access on Ubuntu 24.04
Allow remote connections from Azure App Service:

1. Edit `postgresql.conf`:
   ```bash
   sudo nano /etc/postgresql/16/main/postgresql.conf
   ```
   Find `listen_addresses` and change it to:
   ```conf
   listen_addresses = '*'
   ```

2. Edit `pg_hba.conf` to grant access:
   ```bash
   sudo nano /etc/postgresql/16/main/pg_hba.conf
   ```
   Add the following line at the end:
   ```conf
   host    zapmancer       zapadmin        0.0.0.0/0               md5
   ```

3. Restart PostgreSQL:
   ```bash
   sudo systemctl restart postgresql
   ```

4. **In Azure Portal**: Go to your VM $\rightarrow$ **Networking** $\rightarrow$ **Network security group** $\rightarrow$ Add Inbound Security Rule for Port `5432` (PostgreSQL).

---

### Option B: Azure Database for PostgreSQL (Flexible Server - Managed)

1. Search for **Azure Database for PostgreSQL flexible servers** $\rightarrow$ Click **Create**.
2. Workload: *Development*, Set admin username (`zapadmin`) and password.
3. Allow public access from Azure services.

---

## Step 2: Deploy Backend Server to Azure App Service

Azure App Service allows you to run your Docker container with zero server configuration.

### Deployment via Azure CLI & Docker

#### 1. Install Azure CLI & Log In
```bash
az login
```

#### 2. Create Azure Container Registry (ACR)
```bash
az acr create --resource-group zapmancer-rg --name zapmancerregistry --sku Basic
az acr login --name zapmancerregistry
```

#### 3. Build & Push Docker Image
```bash
# 1. Build Ktor server distribution
./gradlew :server:installDist

# 2. Tag and push Docker image
docker build -t zapmancerregistry.azurecr.io/backend:v1 .
docker push zapmancerregistry.azurecr.io/backend:v1
```

#### 4. Create App Service Plan & Web App
```bash
az appservice plan create --name zapmancer-plan --resource-group zapmancer-rg --sku B1 --is-linux

az webapp create \
  --resource-group zapmancer-rg \
  --plan zapmancer-plan \
  --name zapmancer-backend \
  --deployment-container-image-name zapmancerregistry.azurecr.io/backend:v1
```

#### 5. Configure Environment Variables
If using **Ubuntu 24.04 VM** for database, use your Ubuntu VM's IP address:
```bash
az webapp config appsettings set \
  --resource-group zapmancer-rg \
  --name zapmancer-backend \
  --settings \
    WEBSITES_PORT=8080 \
    PORT=8080 \
    DATABASE_URL="jdbc:postgresql://<YOUR_UBUNTU_VM_IP>:5432/zapmancer" \
    DB_USER="zapadmin" \
    DB_PASSWORD="your_secure_password"
```

Your backend URL will be: `https://zapmancer-backend.azurewebsites.net`

---

## Step 3: Deploy Web App to Azure Static Web Apps

### Deployment via GitHub Integration

1. Push code to GitHub repository.
2. In Azure Portal, search for **Static Web Apps** $\rightarrow$ Click **Create**.
3. Select GitHub Repository & Branch (`main`).
4. Build Preset: **Custom**:
   - **App location**: `app/webApp`
   - **Output location**: `dist`
5. Click **Create**.

Your web app URL will be: `https://<random-name>.azurestaticapps.net`

---

## Step 4: Connecting Local Android App to Hosted Azure Backend

Open [`core/src/commonMain/kotlin/com/smach/zapmancer/core/network/ktor/NetworkConstants.kt`](file:///home/xe23/IdeaProjects/Zapmancer/core/src/commonMain/kotlin/com/smach/zapmancer/core/network/ktor/NetworkConstants.kt) and update the base URL:

```kotlin
object NetworkConstants {
    const val BASE_URL = "https://zapmancer-backend.azurewebsites.net"
    const val WS_URL = "wss://zapmancer-backend.azurewebsites.net/ws"
}
```

Run Android app on your local device/emulator:
```bash
./gradlew :app:androidApp:assembleDebug
```
