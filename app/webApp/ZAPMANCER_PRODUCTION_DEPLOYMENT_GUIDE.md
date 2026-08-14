# Zapmancer Production Launch & Deployment Guide

A step-by-step technical and operational playbook for launching **Zapmancer** into production.

---

## 🚀 5 Steps to Launch Zapmancer in Production

### Step 1: Frontend Web App Build & CDN Deployment
1. Navigate to the web application directory:
   ```bash
   cd app/webApp
   ```
2. Build the production client bundle:
   ```bash
   npm run build
   ```
   This generates an optimized `dist/` directory (static HTML, JS, CSS).
3. Connect the repository to **Vercel**, **Netlify**, or **Cloudflare Pages**:
   - **Framework Preset**: Vite / React
   - **Build Command**: `npm run build`
   - **Output Directory**: `dist`
   - **Custom Domain**: `https://zapmancer.io`

---

### Step 2: Backend Infrastructure & Docker Deployment
Deploy the backend services via `docker-compose.yml` to an AWS EC2 instance, Hetzner Cloud, or DigitalOcean Droplet:

```bash
# Clone the repository on your production Linux server
git clone https://github.com/I-am-Abhilash/Zapmancer.git
cd Zapmancer

# Launch PostgreSQL, Ktor Server, Gorse AI, and RustFS Storage
docker compose -f docker-compose.yml up -d --build
```

---

### Step 3: Production Environment Variables (.env)
Configure the production `.env` file in the root directory:

```env
# SERVER & SECURITY
PORT=8080
JWT_SECRET=your_super_secret_production_jwt_key_2026
ENVIRONMENT=production

# DATABASE
POSTGRES_HOST=postgres
POSTGRES_PORT=5432
POSTGRES_DB=zapmancer_db
POSTGRES_USER=zapmancer_admin
POSTGRES_PASSWORD=your_strong_db_password

# STRIPE CONNECT (ESCROW PAYMENTS)
STRIPE_SECRET_KEY=sk_live_...
STRIPE_WEBHOOK_SECRET=whsec_...
STRIPE_CONNECT_CLIENT_ID=ca_...

# WEB3 USDC WALLET (POLYGON / SOLANA)
WEB3_USDC_CONTRACT_ADDRESS=0x2791Bca1f2de4661ED88A30C99A7a9449Aa84174
WEB3_ESCROW_PRIVATE_KEY=0x...

# GORSE AI RECOMMENDER
GORSE_SERVER_ENTRY=gorse-master:8086
GORSE_API_KEY=your_gorse_api_key
```

---

### Step 4: Stripe Connect Gateway Onboarding
1. Register a **Stripe Connect** business account at [dashboard.stripe.com](https://dashboard.stripe.com).
2. Enable **Express/Custom Connect Accounts** to allow 0% fee freelancer bank deposits.
3. Configure the Webhook listener pointing to:
   `https://api.zapmancer.io/v1/webhooks/stripe`
   - Event triggers: `payment_intent.succeeded`, `account.updated`, `transfer.created`.

---

### Step 5: Domain Name, Nginx Reverse Proxy & SSL Setup
1. Configure DNS A records:
   - `zapmancer.io` -> CDN / Vercel IP
   - `api.zapmancer.io` -> Production Server IP
2. Install Nginx & Certbot for SSL encryption:
   ```bash
   sudo apt update && sudo apt install -y nginx certbot python3-certbot-nginx
   sudo certbot --nginx -d api.zapmancer.io
   ```

---

## 🔒 Production Security Checklist

- [ ] JWT secret changed from default to a 64-character random string.
- [ ] PostgreSQL port `5432` firewall-restricted to internal Docker network only.
- [ ] CORS headers configured in Ktor to accept requests only from `https://zapmancer.io`.
- [ ] Stripe Live API keys activated and verified.
- [ ] SSL HTTPS enforced on all endpoints.
