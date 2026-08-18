-- Migration V7: Proposals and lifecycle updates
ALTER TABLE proposals ADD COLUMN IF NOT EXISTS status VARCHAR(30) DEFAULT 'PENDING';
ALTER TABLE projects ADD COLUMN IF NOT EXISTS status VARCHAR(30) DEFAULT 'OPEN';
