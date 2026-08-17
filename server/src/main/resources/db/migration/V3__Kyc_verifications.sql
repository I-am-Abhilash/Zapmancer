-- V3: KYC Identity Verification Tables
CREATE TABLE IF NOT EXISTS kyc_verifications (
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

CREATE INDEX IF NOT EXISTS idx_kyc_verifications_user_id ON kyc_verifications(user_id);
CREATE INDEX IF NOT EXISTS idx_kyc_verifications_status ON kyc_verifications(status);
