-- ============================================================
-- Zapmancer Freelancer Marketplace - Initial Database Schema
-- ============================================================

-- 1. Users Table
CREATE TABLE users (
    id VARCHAR(128) PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password_hash VARCHAR(255),
    avatar_url VARCHAR(255),
    role VARCHAR(20) NOT NULL DEFAULT 'FREELANCER',  -- FREELANCER | CLIENT
    deleted_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 2. User Settings (1:1 with users)
CREATE TABLE user_settings (
    user_id VARCHAR(128) PRIMARY KEY REFERENCES users(id) ON DELETE CASCADE,
    is_two_factor_enabled BOOLEAN NOT NULL DEFAULT FALSE,
    is_dark_mode_enabled BOOLEAN NOT NULL DEFAULT TRUE,
    is_email_notifications_enabled BOOLEAN NOT NULL DEFAULT TRUE,
    is_client_mode_enabled BOOLEAN NOT NULL DEFAULT FALSE,
    organization VARCHAR(100)
);

-- 3. OTP Sessions (for forgot-password flow)
CREATE TABLE otp_sessions (
    id SERIAL PRIMARY KEY,
    email VARCHAR(100) NOT NULL,
    code VARCHAR(10) NOT NULL,
    expires_at TIMESTAMP NOT NULL,
    is_used BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 4. User Profiles (extended public profile info, 1:1 with users)
CREATE TABLE user_profiles (
    user_id VARCHAR(128) PRIMARY KEY REFERENCES users(id) ON DELETE CASCADE,
    role_title VARCHAR(150),
    location VARCHAR(100),
    ranking VARCHAR(50),
    is_top_rated BOOLEAN NOT NULL DEFAULT FALSE,
    projects_count INT NOT NULL DEFAULT 0,
    rating DOUBLE PRECISION NOT NULL DEFAULT 0.0,
    experience VARCHAR(50),
    about TEXT
);

-- 5. Profile Skills (many per user)
CREATE TABLE profile_skills (
    user_id VARCHAR(128) NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    skill VARCHAR(50) NOT NULL,
    PRIMARY KEY (user_id, skill)
);

-- 6. Portfolio Items
CREATE TABLE portfolio_items (
    id SERIAL PRIMARY KEY,
    user_id VARCHAR(128) NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    title VARCHAR(150) NOT NULL,
    description TEXT,
    image_url VARCHAR(255)
);

-- 7. Reviews (client reviews on freelancers)
CREATE TABLE reviews (
    id SERIAL PRIMARY KEY,
    subject_id VARCHAR(128) NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    author_id VARCHAR(128) NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    content TEXT NOT NULL,
    rating INT NOT NULL DEFAULT 5,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 8. Projects
CREATE TABLE projects (
    id VARCHAR(128) PRIMARY KEY,
    category VARCHAR(50) NOT NULL,
    title VARCHAR(255) NOT NULL,
    posted_time VARCHAR(50) NOT NULL,
    location VARCHAR(100) NOT NULL DEFAULT 'Remote',
    is_payment_verified BOOLEAN NOT NULL DEFAULT FALSE,
    budget_range VARCHAR(100) NOT NULL,
    project_type VARCHAR(50) NOT NULL DEFAULT 'Fixed Price',
    project_scope TEXT,
    timeline VARCHAR(50),
    est_start VARCHAR(50),
    client_id VARCHAR(128) NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    is_identity_verified BOOLEAN NOT NULL DEFAULT FALSE,
    is_phone_verified BOOLEAN NOT NULL DEFAULT FALSE,
    is_client_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 9. Project Skills (many per project)
CREATE TABLE project_skills (
    project_id VARCHAR(128) NOT NULL REFERENCES projects(id) ON DELETE CASCADE,
    skill VARCHAR(50) NOT NULL,
    PRIMARY KEY (project_id, skill)
);

-- 10. Project Deliverables (ordered list per project)
CREATE TABLE project_deliverables (
    id SERIAL PRIMARY KEY,
    project_id VARCHAR(128) NOT NULL REFERENCES projects(id) ON DELETE CASCADE,
    deliverable TEXT NOT NULL
);

-- 11. Saved Projects (freelancer bookmarks)
CREATE TABLE saved_projects (
    user_id VARCHAR(128) NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    project_id VARCHAR(128) NOT NULL REFERENCES projects(id) ON DELETE CASCADE,
    PRIMARY KEY (user_id, project_id)
);

-- 12. Project Applications (quick apply tracking)
CREATE TABLE project_applications (
    user_id VARCHAR(128) NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    project_id VARCHAR(128) NOT NULL REFERENCES projects(id) ON DELETE CASCADE,
    applied_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (user_id, project_id)
);

-- 13. Proposals
CREATE TABLE proposals (
    id SERIAL PRIMARY KEY,
    project_id VARCHAR(128) NOT NULL REFERENCES projects(id) ON DELETE CASCADE,
    freelancer_id VARCHAR(128) NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    freelancer_name VARCHAR(100) NOT NULL,
    freelancer_role VARCHAR(150) NOT NULL,
    pitch_content TEXT NOT NULL,
    budget VARCHAR(50) NOT NULL,
    timeline_days VARCHAR(20) NOT NULL,
    project_type VARCHAR(50) NOT NULL DEFAULT 'Fixed Price',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 14. Conversations (1:1 chat threads)
CREATE TABLE conversations (
    id VARCHAR(128) PRIMARY KEY,
    user1_id VARCHAR(128) NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    user2_id VARCHAR(128) NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 15. Messages
CREATE TABLE messages (
    id VARCHAR(128) PRIMARY KEY,
    conversation_id VARCHAR(128) NOT NULL REFERENCES conversations(id) ON DELETE CASCADE,
    sender_id VARCHAR(128) NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    text TEXT NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'SENT',  -- SENT | DELIVERED | READ
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 16. Notifications
CREATE TABLE notifications (
    id SERIAL PRIMARY KEY,
    user_id VARCHAR(128) NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    type VARCHAR(50) NOT NULL,  -- MILESTONE | MESSAGE | ALERT | GENERAL | COLLABORATOR
    title VARCHAR(150) NOT NULL,
    description TEXT NOT NULL,
    timestamp VARCHAR(50) NOT NULL,
    section VARCHAR(50) NOT NULL DEFAULT 'Today',
    code_snippet TEXT,
    is_italic BOOLEAN NOT NULL DEFAULT FALSE,
    quick_reply BOOLEAN NOT NULL DEFAULT FALSE,
    is_read BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 17. Notification Actions
CREATE TABLE notification_actions (
    id SERIAL PRIMARY KEY,
    notification_id INT NOT NULL REFERENCES notifications(id) ON DELETE CASCADE,
    label VARCHAR(100) NOT NULL,
    is_primary BOOLEAN NOT NULL DEFAULT FALSE,
    is_error BOOLEAN NOT NULL DEFAULT FALSE
);

-- ============================================================
-- Indexes
-- ============================================================

CREATE INDEX idx_projects_client ON projects(client_id);
CREATE INDEX idx_projects_created ON projects(created_at DESC);
CREATE INDEX idx_proposals_project ON proposals(project_id);
CREATE INDEX idx_proposals_freelancer ON proposals(freelancer_id);
CREATE INDEX idx_messages_conversation ON messages(conversation_id, created_at ASC);
CREATE INDEX idx_notifications_user ON notifications(user_id, created_at DESC);
CREATE INDEX idx_otp_email ON otp_sessions(email);
CREATE INDEX idx_reviews_subject ON reviews(subject_id);

-- Search Indexes (FTS & Trigram)
CREATE EXTENSION IF NOT EXISTS pg_trgm;
CREATE INDEX idx_projects_title_trgm ON projects USING gin (title gin_trgm_ops);
CREATE INDEX idx_projects_scope_trgm ON projects USING gin (project_scope gin_trgm_ops);
CREATE INDEX idx_projects_title_fts ON projects USING gin (to_tsvector('english', title));
CREATE INDEX idx_projects_scope_fts ON projects USING gin (to_tsvector('english', project_scope));
