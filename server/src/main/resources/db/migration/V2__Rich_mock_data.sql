-- ============================================================
-- Zapmancer Freelancer Marketplace - Rich Mock Data
-- ============================================================

-- 1. Users
-- All users share the password: 'password' (BCrypt hash with 12 rounds)
INSERT INTO users (id, username, email, password_hash, avatar_url, role, created_at) VALUES
('user_freelancer_1', 'alex_freelance', 'freelancer@zapmancer.com', '$2a$12$R9h/lIPzMRgFXhY3GgbOYeeCG6H30JcDEqy8wGTynW1.V/M1N8WdC', 'https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=150', 'FREELANCER', CURRENT_TIMESTAMP - INTERVAL '30 days'),
('user_client_1', 'sarah_client', 'client@zapmancer.com', '$2a$12$R9h/lIPzMRgFXhY3GgbOYeeCG6H30JcDEqy8wGTynW1.V/M1N8WdC', 'https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=150', 'CLIENT', CURRENT_TIMESTAMP - INTERVAL '30 days'),
('user_freelancer_2', 'designer_dan', 'dan@zapmancer.com', '$2a$12$R9h/lIPzMRgFXhY3GgbOYeeCG6H30JcDEqy8wGTynW1.V/M1N8WdC', 'https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=150', 'FREELANCER', CURRENT_TIMESTAMP - INTERVAL '25 days'),
('user_freelancer_3', 'backend_bob', 'bob@zapmancer.com', '$2a$12$R9h/lIPzMRgFXhY3GgbOYeeCG6H30JcDEqy8wGTynW1.V/M1N8WdC', 'https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=150', 'FREELANCER', CURRENT_TIMESTAMP - INTERVAL '20 days'),
('user_client_2', 'company_corp', 'admin@companycorp.com', '$2a$12$R9h/lIPzMRgFXhY3GgbOYeeCG6H30JcDEqy8wGTynW1.V/M1N8WdC', 'https://images.unsplash.com/photo-1472099645785-5658abf4ff4e?w=150', 'CLIENT', CURRENT_TIMESTAMP - INTERVAL '15 days');

-- 2. User Settings
INSERT INTO user_settings (user_id, is_two_factor_enabled, is_dark_mode_enabled, is_email_notifications_enabled, is_client_mode_enabled, organization) VALUES
('user_freelancer_1', FALSE, TRUE, TRUE, FALSE, NULL),
('user_client_1', FALSE, FALSE, TRUE, TRUE, 'Zapmancer Labs'),
('user_freelancer_2', FALSE, TRUE, TRUE, FALSE, NULL),
('user_freelancer_3', FALSE, TRUE, TRUE, FALSE, NULL),
('user_client_2', FALSE, FALSE, TRUE, TRUE, 'Acme Corporation');

-- 3. User Profiles
INSERT INTO user_profiles (user_id, role_title, location, ranking, is_top_rated, projects_count, rating, experience, about) VALUES
('user_freelancer_1', 'Senior Kotlin Multiplatform Developer', 'Remote / San Francisco', 'Top Rated Plus', TRUE, 42, 4.95, '8+ years', 'Specialist in Compose Multiplatform, Ktor backend development, and performance optimization across iOS, Android, and Desktop. Let''s build something great together!'),
('user_client_1', 'Product Director', 'Austin, TX', 'Premium Client', FALSE, 12, 4.80, '10 years', 'Managing product development at Zapmancer Labs. We focus on building cutting-edge creator tools, dashboards, and automated marketplaces.'),
('user_freelancer_2', 'Lead UI/UX & Brand Designer', 'London, UK', 'Top Rated', TRUE, 25, 4.90, '5 years', 'I create clean, user-centric interfaces. Expert in Figma component structures, Design Tokens, responsive layout systems, and modern CSS.'),
('user_freelancer_3', 'Senior Distributed Systems Architect', 'Berlin, Germany', 'Expert', FALSE, 18, 4.70, '12 years', 'Focused on building highly concurrent, microservice-based backends using Ktor, Spring Boot, Go, and PostgreSQL. Experienced in Redis caching and Docker architectures.');

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
('user_freelancer_1', 'EcoTrack Mobile App', 'Built a full Compose Multiplatform application tracking carbon footprints across iOS and Android. Featured in Apple App Store Top Free Apps list.', 'https://images.unsplash.com/photo-1542601906990-b4d3fb778b09?w=300'),
('user_freelancer_1', 'Web3 Analytics Dashboard', 'Created a real-time web portal displaying network statistics and transaction throughput using Ktor WebSockets and Kotlin JS.', 'https://images.unsplash.com/photo-1551288049-bebda4e38f71?w=300'),
('user_freelancer_2', 'ShopVibe Storefront Redesign', 'Complete Figma design prototype and design tokens layout for a high-traffic e-commerce brand yielding 35% higher conversions.', 'https://images.unsplash.com/photo-1460925895917-afdab827c52f?w=300');

-- 6. Reviews
INSERT INTO reviews (subject_id, author_id, content, rating, created_at) VALUES
('user_freelancer_1', 'user_client_2', 'Alex is an exceptional developer. He delivered our complex user backend with perfect test coverage and great communication. Highly recommended.', 5, CURRENT_TIMESTAMP - INTERVAL '10 days'),
('user_freelancer_1', 'user_client_1', 'Incredibly professional developer. The eco-tracking app was delivered ahead of schedule and the code quality was top notch! Will hire again.', 5, CURRENT_TIMESTAMP - INTERVAL '5 days');

-- 7. Projects
INSERT INTO projects (id, category, title, posted_time, location, is_payment_verified, budget_range, project_type, project_scope, timeline, est_start, client_id, is_identity_verified, is_phone_verified, is_client_active, created_at) VALUES
('proj_kmp_dashboard', 'Mobile Development', 'Compose Multiplatform Admin Dashboard', '2 hours ago', 'Remote', TRUE, '$8,000 - $12,000', 'Fixed Price', 'We are looking for an expert KMP developer to build a responsive admin dashboard that works on iOS, Android, and Desktop. The app should display real-time usage statistics and connect via Ktor WebSockets.', '2 months', 'Immediate', 'user_client_1', TRUE, TRUE, TRUE, CURRENT_TIMESTAMP - INTERVAL '2 hours'),
('proj_ktor_api', 'Backend Systems', 'Scalable Ktor REST API with PostgreSQL', '1 day ago', 'Remote', TRUE, '$5,000 - $7,500', 'Fixed Price', 'Need a senior Kotlin backend engineer to build our core user service. High availability, JWT auth, integration with pgvector, and thorough unit/integration tests are required.', '1 month', 'In 2 weeks', 'user_client_2', TRUE, TRUE, TRUE, CURRENT_TIMESTAMP - INTERVAL '1 day'),
('proj_figma_redesign', 'UI/UX Design', 'Creative Mobile Landing Page System', '3 days ago', 'Hybrid (Austin)', FALSE, '$3,000 - $4,500', 'Fixed Price', 'Looking for a Figma designer to build a premium landing page system for a creator platform. Needs light/dark mode and responsive layouts for mobile and web screens.', '3 weeks', 'Immediate', 'user_client_1', TRUE, FALSE, TRUE, CURRENT_TIMESTAMP - INTERVAL '3 days');

-- 8. Project Skills
INSERT INTO project_skills (project_id, skill) VALUES
('proj_kmp_dashboard', 'Kotlin'),
('proj_kmp_dashboard', 'Compose Multiplatform'),
('proj_kmp_dashboard', 'Ktor'),
('proj_kmp_dashboard', 'WebSockets'),
('proj_ktor_api', 'Kotlin'),
('proj_ktor_api', 'Ktor'),
('proj_ktor_api', 'PostgreSQL'),
('proj_ktor_api', 'JWT'),
('proj_figma_redesign', 'UI/UX Design'),
('proj_figma_redesign', 'Figma'),
('proj_figma_redesign', 'Design Systems');

-- 9. Project Deliverables
INSERT INTO project_deliverables (project_id, deliverable) VALUES
('proj_kmp_dashboard', '1. Figma mockups review and target design system alignment'),
('proj_kmp_dashboard', '2. Core repository layout setup and WebSockets infrastructure'),
('proj_kmp_dashboard', '3. Multiplatform user interface implementing list-detail navigation layouts'),
('proj_kmp_dashboard', '4. Deployment and staging verification for iOS, Android, and Desktop'),
('proj_ktor_api', '1. Database migrations and connection pooling setup'),
('proj_ktor_api', '2. Authentication endpoints and JWT security filters'),
('proj_ktor_api', '3. Full API documentation and integration test suite');

-- 10. Saved Projects
INSERT INTO saved_projects (user_id, project_id) VALUES
('user_freelancer_1', 'proj_ktor_api'),
('user_freelancer_2', 'proj_figma_redesign');

-- 11. Project Applications
INSERT INTO project_applications (user_id, project_id, applied_at) VALUES
('user_freelancer_1', 'proj_kmp_dashboard', CURRENT_TIMESTAMP - INTERVAL '1 hour');

-- 12. Proposals
INSERT INTO proposals (project_id, freelancer_id, freelancer_name, freelancer_role, pitch_content, budget, timeline_days, project_type, created_at) VALUES
('proj_kmp_dashboard', 'user_freelancer_1', 'Alex Freelance', 'Senior Kotlin Multiplatform Developer', 'Hi Sarah, I would love to build this KMP admin dashboard for you. I have built several multiplatform apps using Compose and Ktor. I have reviewed the requirements and proposed an architecture that uses MVI, state-hoisting, and clean navigation layers to ensure high reliability. Let''s schedule a call to align on details!', '$9,500', '45 days', 'Fixed Price', CURRENT_TIMESTAMP - INTERVAL '1 hour'),
('proj_kmp_dashboard', 'user_freelancer_3', 'Backend Bob', 'Senior Backend Developer', 'I can build the WebSockets architecture and implement the KMP interface. Although I specialize in backend systems, I am highly proficient in Kotlin and multiplatform setups. Let me know if you are interested.', '$11,000', '60 days', 'Fixed Price', CURRENT_TIMESTAMP - INTERVAL '30 minutes');

-- 13. Conversations
INSERT INTO conversations (id, user1_id, user2_id, created_at) VALUES
('conv_alex_sarah', 'user_freelancer_1', 'user_client_1', CURRENT_TIMESTAMP - INTERVAL '2 hours');

-- 14. Messages
INSERT INTO messages (id, conversation_id, sender_id, text, status, created_at) VALUES
('msg_1', 'conv_alex_sarah', 'user_client_1', 'Hi Alex, thanks for submitting your proposal! I really liked your eco-tracking app in your portfolio. Do you have experience with Ktor WebSockets?', 'READ', CURRENT_TIMESTAMP - INTERVAL '2 hours'),
('msg_2', 'conv_alex_sarah', 'user_freelancer_1', 'Hi Sarah! Yes, I built the real-time telemetry screen for the EcoTrack app using Ktor''s WebSockets client. It was handling around 120 messages per second smoothly with structured concurrency.', 'READ', CURRENT_TIMESTAMP - INTERVAL '1 hour 45 minutes'),
('msg_3', 'conv_alex_sarah', 'user_client_1', 'That is exactly what we need. We''re showing server stats and concurrent user counts. When would you be available to start?', 'READ', CURRENT_TIMESTAMP - INTERVAL '1 hour 30 minutes'),
('msg_4', 'conv_alex_sarah', 'user_freelancer_1', 'I can start as early as tomorrow. I''ve already drafted a basic schema setup that aligns with the requirements.', 'DELIVERED', CURRENT_TIMESTAMP - INTERVAL '1 hour');

-- 15. Notifications
INSERT INTO notifications (id, user_id, type, title, description, timestamp, section, code_snippet, is_italic, quick_reply, is_read, created_at) VALUES
(1, 'user_freelancer_1', 'MESSAGE', 'New Message from Sarah', 'Sarah: "That is exactly what we need. We''re showing server stats..."', '1.5 hours ago', 'Today', 'val client = HttpClient { install(WebSockets) }', FALSE, TRUE, FALSE, CURRENT_TIMESTAMP - INTERVAL '1 hour 30 minutes'),
(2, 'user_freelancer_1', 'MILESTONE', 'Milestone Approved', 'Your deliverable for project initialization has been approved. Payment has been released.', '1 day ago', 'Yesterday', NULL, FALSE, FALSE, TRUE, CURRENT_TIMESTAMP - INTERVAL '1 day'),
(3, 'user_freelancer_1', 'ALERT', 'Security Alert', 'A login attempt was made from a new device in Berlin, Germany. Please verify your identity if this was you.', '3 days ago', 'Last Week', NULL, TRUE, FALSE, TRUE, CURRENT_TIMESTAMP - INTERVAL '3 days');

-- 16. Notification Actions
INSERT INTO notification_actions (notification_id, label, is_primary, is_error) VALUES
(1, 'Reply', TRUE, FALSE),
(1, 'Archive', FALSE, FALSE),
(3, 'Secure Account', TRUE, TRUE),
(3, 'Dismiss', FALSE, FALSE);
