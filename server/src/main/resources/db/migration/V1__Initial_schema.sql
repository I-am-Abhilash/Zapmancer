-- ============================================================
-- Zapmancer Unified Database Schema & Initial Data
-- ============================================================

-- 1. Users Table
CREATE TABLE users (
    id VARCHAR(128) PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password_hash VARCHAR(255),
    avatar_url VARCHAR(255),
    role VARCHAR(20) NOT NULL DEFAULT 'FREELANCER',  -- FREELANCER | CLIENT | ADMIN
    phone_number VARCHAR(30),
    is_email_verified BOOLEAN NOT NULL DEFAULT FALSE,
    is_phone_verified BOOLEAN NOT NULL DEFAULT FALSE,
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

-- 3. OTP Sessions (for forgot-password & email verification flow)
CREATE TABLE otp_sessions (
    id SERIAL PRIMARY KEY,
    email VARCHAR(100) NOT NULL,
    code VARCHAR(10) NOT NULL,
    expires_at TIMESTAMP NOT NULL,
    is_used BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 4. Phone OTP Sessions (SMS verification)
CREATE TABLE phone_otp_sessions (
    id SERIAL PRIMARY KEY,
    user_id VARCHAR(128) NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    phone_number VARCHAR(30) NOT NULL,
    code VARCHAR(10) NOT NULL,
    expires_at TIMESTAMP NOT NULL,
    is_used BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 5. User Profiles (extended public profile info, 1:1 with users)
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

-- 6. Profile Skills (many per user)
CREATE TABLE profile_skills (
    user_id VARCHAR(128) NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    skill VARCHAR(50) NOT NULL,
    PRIMARY KEY (user_id, skill)
);

-- 7. Portfolio Items
CREATE TABLE portfolio_items (
    id SERIAL PRIMARY KEY,
    user_id VARCHAR(128) NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    title VARCHAR(150) NOT NULL,
    description TEXT,
    image_url VARCHAR(255)
);

-- 8. Reviews (client reviews on freelancers)
CREATE TABLE reviews (
    id SERIAL PRIMARY KEY,
    subject_id VARCHAR(128) NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    author_id VARCHAR(128) NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    content TEXT NOT NULL,
    rating INT NOT NULL DEFAULT 5,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 9. Projects
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
    status VARCHAR(30) NOT NULL DEFAULT 'OPEN',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 10. Project Skills (many per project)
CREATE TABLE project_skills (
    project_id VARCHAR(128) NOT NULL REFERENCES projects(id) ON DELETE CASCADE,
    skill VARCHAR(50) NOT NULL,
    PRIMARY KEY (project_id, skill)
);

-- 11. Project Deliverables (ordered list per project)
CREATE TABLE project_deliverables (
    id SERIAL PRIMARY KEY,
    project_id VARCHAR(128) NOT NULL REFERENCES projects(id) ON DELETE CASCADE,
    deliverable TEXT NOT NULL
);

-- 12. Saved Projects (freelancer bookmarks)
CREATE TABLE saved_projects (
    user_id VARCHAR(128) NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    project_id VARCHAR(128) NOT NULL REFERENCES projects(id) ON DELETE CASCADE,
    PRIMARY KEY (user_id, project_id)
);

-- 13. Project Applications (quick apply tracking)
CREATE TABLE project_applications (
    user_id VARCHAR(128) NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    project_id VARCHAR(128) NOT NULL REFERENCES projects(id) ON DELETE CASCADE,
    applied_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (user_id, project_id)
);

-- 14. Proposals
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
    status VARCHAR(30) NOT NULL DEFAULT 'PENDING',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 15. Conversations (1:1 chat threads)
CREATE TABLE conversations (
    id VARCHAR(128) PRIMARY KEY,
    user1_id VARCHAR(128) NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    user2_id VARCHAR(128) NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 16. Messages
CREATE TABLE messages (
    id VARCHAR(128) PRIMARY KEY,
    conversation_id VARCHAR(128) NOT NULL REFERENCES conversations(id) ON DELETE CASCADE,
    sender_id VARCHAR(128) NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    text TEXT NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'SENT',  -- SENT | DELIVERED | READ
    attachment_url VARCHAR(255),
    attachment_type VARCHAR(50),
    attachment_name VARCHAR(150),
    attachment_size_bytes BIGINT,
    reply_to_message_id VARCHAR(128) REFERENCES messages(id) ON DELETE SET NULL,
    is_edited BOOLEAN NOT NULL DEFAULT FALSE,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    read_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 17. Message Reactions
CREATE TABLE message_reactions (
    id SERIAL PRIMARY KEY,
    message_id VARCHAR(128) NOT NULL REFERENCES messages(id) ON DELETE CASCADE,
    user_id VARCHAR(128) NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    emoji VARCHAR(32) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_message_user_emoji UNIQUE (message_id, user_id, emoji)
);

-- 18. Notifications
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

-- 19. Notification Actions
CREATE TABLE notification_actions (
    id SERIAL PRIMARY KEY,
    notification_id INT NOT NULL REFERENCES notifications(id) ON DELETE CASCADE,
    label VARCHAR(100) NOT NULL,
    is_primary BOOLEAN NOT NULL DEFAULT FALSE,
    is_error BOOLEAN NOT NULL DEFAULT FALSE
);

-- 20. KYC Identity Verification
CREATE TABLE kyc_verifications (
    id VARCHAR(128) PRIMARY KEY,
    user_id VARCHAR(128) NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    status VARCHAR(30) NOT NULL DEFAULT 'PENDING',
    document_type VARCHAR(50) NOT NULL,
    document_front_url VARCHAR(255) NOT NULL,
    document_back_url VARCHAR(255),
    selfie_url VARCHAR(255) NOT NULL,
    extracted_name VARCHAR(150),
    extracted_dob VARCHAR(50),
    extracted_doc_number VARCHAR(100),
    extracted_expiry VARCHAR(50),
    face_similarity_score DOUBLE PRECISION,
    liveness_score DOUBLE PRECISION,
    liveness_passed BOOLEAN NOT NULL DEFAULT FALSE,
    is_name_matched BOOLEAN NOT NULL DEFAULT FALSE,
    receipt_signature TEXT,
    receipt_hash VARCHAR(64),
    reviewer_notes TEXT,
    reviewed_by VARCHAR(128) REFERENCES users(id) ON DELETE SET NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- ============================================================
-- Indexes
-- ============================================================

CREATE UNIQUE INDEX idx_users_email_lower ON users (LOWER(email));
CREATE INDEX idx_projects_client_id ON projects(client_id);
CREATE INDEX idx_projects_category ON projects(category);
CREATE INDEX idx_projects_created ON projects(created_at DESC);
CREATE INDEX idx_project_skills_project_id ON project_skills(project_id);
CREATE INDEX idx_project_deliverables_project_id ON project_deliverables(project_id);
CREATE INDEX idx_proposals_project_id ON proposals(project_id);
CREATE INDEX idx_proposals_freelancer_id ON proposals(freelancer_id);
CREATE INDEX idx_saved_projects_user_id ON saved_projects(user_id);
CREATE INDEX idx_saved_projects_project_id ON saved_projects(project_id);
CREATE INDEX idx_conversations_user1 ON conversations(user1_id);
CREATE INDEX idx_conversations_user2 ON conversations(user2_id);
CREATE INDEX idx_messages_conversation ON messages(conversation_id, created_at ASC);
CREATE INDEX idx_messages_conversation_created ON messages(conversation_id, created_at DESC);
CREATE INDEX idx_message_reactions_message_id ON message_reactions(message_id);
CREATE INDEX idx_notifications_user_id ON notifications(user_id, created_at DESC);
CREATE INDEX idx_notifications_unread ON notifications(user_id, is_read);
CREATE INDEX idx_otp_sessions_email ON otp_sessions(email, is_used);
CREATE INDEX idx_phone_otp_user ON phone_otp_sessions(user_id, is_used);
CREATE INDEX idx_phone_otp_phone ON phone_otp_sessions(phone_number, is_used);
CREATE INDEX idx_reviews_subject ON reviews(subject_id);
CREATE INDEX idx_kyc_verifications_user_id ON kyc_verifications(user_id);
CREATE INDEX idx_kyc_verifications_status ON kyc_verifications(status);

-- Search Indexes (FTS & Trigram)
CREATE EXTENSION IF NOT EXISTS pg_trgm;
CREATE INDEX idx_projects_title_trgm ON projects USING gin (title gin_trgm_ops);
CREATE INDEX idx_projects_scope_trgm ON projects USING gin (project_scope gin_trgm_ops);
CREATE INDEX idx_projects_title_fts ON projects USING gin (to_tsvector('english', title));
CREATE INDEX idx_projects_scope_fts ON projects USING gin (to_tsvector('english', project_scope));

-- ============================================================
-- Rich Mock Seed Data
-- ============================================================

-- 1. Users
-- All users share the password: 'password' (BCrypt hash with 12 rounds)
INSERT INTO users (id, username, email, password_hash, avatar_url, role, is_email_verified, is_phone_verified, created_at) VALUES
('user_freelancer_1', 'alex_freelance', 'freelancer@zapmancer.com', '$2a$12$R9h/lIPzMRgFXhY3GgbOYeeCG6H30JcDEqy8wGTynW1.V/M1N8WdC', 'https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=150', 'FREELANCER', TRUE, TRUE, CURRENT_TIMESTAMP - INTERVAL '30 days'),
('user_client_1', 'sarah_client', 'client@zapmancer.com', '$2a$12$R9h/lIPzMRgFXhY3GgbOYeeCG6H30JcDEqy8wGTynW1.V/M1N8WdC', 'https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=150', 'CLIENT', TRUE, TRUE, CURRENT_TIMESTAMP - INTERVAL '30 days'),
('user_freelancer_2', 'designer_dan', 'dan@zapmancer.com', '$2a$12$R9h/lIPzMRgFXhY3GgbOYeeCG6H30JcDEqy8wGTynW1.V/M1N8WdC', 'https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=150', 'FREELANCER', TRUE, FALSE, CURRENT_TIMESTAMP - INTERVAL '25 days'),
('user_freelancer_3', 'backend_bob', 'bob@zapmancer.com', '$2a$12$R9h/lIPzMRgFXhY3GgbOYeeCG6H30JcDEqy8wGTynW1.V/M1N8WdC', 'https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=150', 'FREELANCER', FALSE, FALSE, CURRENT_TIMESTAMP - INTERVAL '20 days'),
('user_client_2', 'company_corp', 'admin@companycorp.com', '$2a$12$R9h/lIPzMRgFXhY3GgbOYeeCG6H30JcDEqy8wGTynW1.V/M1N8WdC', 'https://images.unsplash.com/photo-1472099645785-5658abf4ff4e?w=150', 'CLIENT', TRUE, TRUE, CURRENT_TIMESTAMP - INTERVAL '15 days'),
('user_admin_1', 'admin_zap', 'admin@zapmancer.com', '$2a$12$R9h/lIPzMRgFXhY3GgbOYeeCG6H30JcDEqy8wGTynW1.V/M1N8WdC', 'https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=150', 'ADMIN', TRUE, TRUE, CURRENT_TIMESTAMP - INTERVAL '30 days');

-- 2. User Settings
INSERT INTO user_settings (user_id, is_two_factor_enabled, is_dark_mode_enabled, is_email_notifications_enabled, is_client_mode_enabled, organization) VALUES
('user_freelancer_1', FALSE, TRUE, TRUE, FALSE, NULL),
('user_client_1', FALSE, FALSE, TRUE, TRUE, 'Zapmancer Labs'),
('user_freelancer_2', FALSE, TRUE, TRUE, FALSE, NULL),
('user_freelancer_3', FALSE, TRUE, TRUE, FALSE, NULL),
('user_client_2', FALSE, FALSE, TRUE, TRUE, 'Acme Corporation'),
('user_admin_1', TRUE, TRUE, TRUE, TRUE, 'Zapmancer Headquarters');

-- 3. User Profiles
INSERT INTO user_profiles (user_id, role_title, location, ranking, is_top_rated, projects_count, rating, experience, about) VALUES
('user_freelancer_1', 'Senior Kotlin Multiplatform Developer', 'Remote / San Francisco', 'Top Rated Plus', TRUE, 42, 4.95, '8+ years', 'Specialist in Compose Multiplatform, Ktor backend development, and performance optimization across iOS, Android, and Desktop.'),
('user_client_1', 'Product Director', 'Austin, TX', 'Premium Client', FALSE, 12, 4.80, '10 years', 'Managing product development at Zapmancer Labs. We focus on building cutting-edge creator tools, dashboards, and automated marketplaces.'),
('user_freelancer_2', 'Lead UI/UX & Brand Designer', 'London, UK', 'Top Rated', TRUE, 25, 4.90, '5 years', 'I create clean, user-centric interfaces. Expert in Figma component structures, Design Tokens, responsive layout systems, and modern CSS.'),
('user_freelancer_3', 'Senior Distributed Systems Architect', 'Berlin, Germany', 'Expert', FALSE, 18, 4.70, '12 years', 'Focused on building highly concurrent, microservice-based backends using Ktor, Spring Boot, Go, and PostgreSQL.'),
('user_admin_1', 'System Administrator', 'San Francisco, CA', 'Platform Admin', TRUE, 0, 5.00, '15 years', 'Core platform operations and KYC compliance oversight.');

-- 4. Profile Skills
INSERT INTO profile_skills (user_id, skill) VALUES
('user_freelancer_1', 'Kotlin'),
('user_freelancer_1', 'Compose Multiplatform'),
('user_freelancer_1', 'Ktor'),
('user_freelancer_1', 'PostgreSQL'),
('user_freelancer_1', 'Android'),
('user_freelancer_1', 'iOS'),
('user_freelancer_2', 'UI/UX Design'),
('user_freelancer_2', 'Figma'),
('user_freelancer_2', 'Design Systems'),
('user_freelancer_2', 'TailwindCSS'),
('user_freelancer_3', 'Kotlin'),
('user_freelancer_3', 'Ktor'),
('user_freelancer_3', 'PostgreSQL'),
('user_freelancer_3', 'Docker'),
('user_freelancer_3', 'Kubernetes');

-- 5. Portfolio Items
INSERT INTO portfolio_items (user_id, title, description, image_url) VALUES
('user_freelancer_1', 'EcoTrack Mobile App', 'Built a full Compose Multiplatform application tracking carbon footprints across iOS and Android.', 'https://images.unsplash.com/photo-1542601906990-b4d3fb778b09?w=300'),
('user_freelancer_1', 'Web3 Analytics Dashboard', 'Created a real-time web portal displaying network statistics and transaction throughput using Ktor WebSockets.', 'https://images.unsplash.com/photo-1551288049-bebda4e38f71?w=300'),
('user_freelancer_2', 'ShopVibe Storefront Redesign', 'Complete Figma design prototype and design tokens layout for a high-traffic e-commerce brand.', 'https://images.unsplash.com/photo-1460925895917-afdab827c52f?w=300');

-- 6. Reviews
INSERT INTO reviews (subject_id, author_id, content, rating, created_at) VALUES
('user_freelancer_1', 'user_client_2', 'Alex is an exceptional developer. Delivered our backend with perfect test coverage and great communication.', 5, CURRENT_TIMESTAMP - INTERVAL '10 days'),
('user_freelancer_1', 'user_client_1', 'Incredibly professional developer. The app was delivered ahead of schedule and code quality was top notch!', 5, CURRENT_TIMESTAMP - INTERVAL '5 days');

-- 7. Projects
INSERT INTO projects (id, category, title, posted_time, location, is_payment_verified, budget_range, project_type, project_scope, timeline, est_start, client_id, is_identity_verified, is_phone_verified, is_client_active, status, created_at) VALUES
('proj_kmp_dashboard', 'DEVELOPMENT', 'Compose Multiplatform Admin Dashboard', '2 hours ago', 'Remote', TRUE, '$8,000 - $12,000', 'Fixed Price', 'We are looking for an expert KMP developer to build a responsive admin dashboard that works on iOS, Android, and Desktop.', '2 months', 'Immediate', 'user_client_1', TRUE, TRUE, TRUE, 'OPEN', CURRENT_TIMESTAMP - INTERVAL '2 hours'),
('proj_brand_design', 'DESIGN', 'Complete Brand Identity & Design System', '5 hours ago', 'Remote / US', TRUE, '$3,500 - $5,000', 'Fixed Price', 'Need a full rebrand including logo suite, color palette, typography guidelines, and Figma component design system.', '3 weeks', 'Within 1 week', 'user_client_2', TRUE, TRUE, TRUE, 'OPEN', CURRENT_TIMESTAMP - INTERVAL '5 hours'),
('proj_mobile_refactor', 'DEVELOPMENT', 'Kotlin Multiplatform Migration from Legacy Code', '1 day ago', 'Remote', TRUE, '$50 - $75 / hr', 'Hourly', 'Refactoring an existing Android native application to share business logic and view models using Kotlin Multiplatform.', '1-3 months', 'Flexible', 'user_client_1', TRUE, TRUE, TRUE, 'OPEN', CURRENT_TIMESTAMP - INTERVAL '1 day');

-- 8. Project Skills
INSERT INTO project_skills (project_id, skill) VALUES
('proj_kmp_dashboard', 'Kotlin'),
('proj_kmp_dashboard', 'Compose Multiplatform'),
('proj_kmp_dashboard', 'Ktor'),
('proj_brand_design', 'Figma'),
('proj_brand_design', 'Brand Identity'),
('proj_brand_design', 'Design Tokens'),
('proj_mobile_refactor', 'Kotlin'),
('proj_mobile_refactor', 'Android'),
('proj_mobile_refactor', 'KMP');

-- 9. Project Deliverables
INSERT INTO project_deliverables (project_id, deliverable) VALUES
('proj_kmp_dashboard', 'Figma layout implementation for Desktop and Mobile'),
('proj_kmp_dashboard', 'Real-time WebSocket data integration'),
('proj_kmp_dashboard', 'Full unit and UI test suite'),
('proj_brand_design', 'Logo suite in SVG and PNG formats'),
('proj_brand_design', 'Design system token export for CSS and Compose'),
('proj_brand_design', 'Brand guidelines PDF');

-- 10. Saved Projects
INSERT INTO saved_projects (user_id, project_id) VALUES
('user_freelancer_1', 'proj_kmp_dashboard'),
('user_freelancer_2', 'proj_brand_design');

-- 11. Project Applications
INSERT INTO project_applications (user_id, project_id, applied_at) VALUES
('user_freelancer_1', 'proj_kmp_dashboard', CURRENT_TIMESTAMP - INTERVAL '1 hour'),
('user_freelancer_2', 'proj_brand_design', CURRENT_TIMESTAMP - INTERVAL '3 hours');

-- 12. Proposals
INSERT INTO proposals (project_id, freelancer_id, freelancer_name, freelancer_role, pitch_content, budget, timeline_days, project_type, status, created_at) VALUES
('proj_kmp_dashboard', 'user_freelancer_1', 'Alex Rivera', 'Senior KMP Developer', 'I have built over 10 production KMP applications and can deliver this dashboard with optimal performance and clean architecture.', '$9,500', '45', 'Fixed Price', 'PENDING', CURRENT_TIMESTAMP - INTERVAL '1 hour');

-- 13. Conversations
INSERT INTO conversations (id, user1_id, user2_id, created_at) VALUES
('conv_alex_sarah', 'user_freelancer_1', 'user_client_1', CURRENT_TIMESTAMP - INTERVAL '3 days'),
('conv_dan_corp', 'user_freelancer_2', 'user_client_2', CURRENT_TIMESTAMP - INTERVAL '2 days');

-- 14. Messages
INSERT INTO messages (id, conversation_id, sender_id, text, status, created_at) VALUES
('msg_1', 'conv_alex_sarah', 'user_client_1', 'Hi Alex, I saw your proposal on the KMP Dashboard project. Would you be available for a brief sync tomorrow?', 'READ', CURRENT_TIMESTAMP - INTERVAL '2 days'),
('msg_2', 'conv_alex_sarah', 'user_freelancer_1', 'Hi Sarah! Absolutely. I am available anytime after 2 PM EST. Looking forward to discussing the milestones.', 'READ', CURRENT_TIMESTAMP - INTERVAL '2 days' + INTERVAL '1 hour'),
('msg_3', 'conv_dan_corp', 'user_client_2', 'Hey Dan, could you send over a couple of brand identity references you recently completed?', 'DELIVERED', CURRENT_TIMESTAMP - INTERVAL '1 day');

-- 15. Notifications
INSERT INTO notifications (user_id, type, title, description, timestamp, section, is_read, created_at) VALUES
('user_freelancer_1', 'MILESTONE', 'Milestone Approved', 'Your milestone "API Integration" on EcoTrack Mobile App was approved and payment released.', '10m ago', 'Today', FALSE, CURRENT_TIMESTAMP - INTERVAL '10 minutes'),
('user_freelancer_1', 'MESSAGE', 'New Message from Sarah', 'Sarah sent you a message regarding the Compose Multiplatform Admin Dashboard project.', '1h ago', 'Today', FALSE, CURRENT_TIMESTAMP - INTERVAL '1 hour'),
('user_client_1', 'COLLABORATOR', 'New Proposal Received', 'Alex Rivera submitted a proposal for "Compose Multiplatform Admin Dashboard".', '1h ago', 'Today', TRUE, CURRENT_TIMESTAMP - INTERVAL '1 hour');

-- 16. Notification Actions
INSERT INTO notification_actions (notification_id, label, is_primary, is_error) VALUES
(1, 'VIEW_CONTRACT', TRUE, FALSE),
(1, 'DISMISS', FALSE, FALSE),
(2, 'REPLY', TRUE, FALSE);
