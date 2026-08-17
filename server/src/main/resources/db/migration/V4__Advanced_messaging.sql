-- V4: Advanced Messaging System (Attachments, Reactions, Threads, Read Receipts, Edit/Delete)

ALTER TABLE messages ADD COLUMN IF NOT EXISTS attachment_url VARCHAR(255);
ALTER TABLE messages ADD COLUMN IF NOT EXISTS attachment_type VARCHAR(50);
ALTER TABLE messages ADD COLUMN IF NOT EXISTS attachment_name VARCHAR(150);
ALTER TABLE messages ADD COLUMN IF NOT EXISTS attachment_size_bytes BIGINT;
ALTER TABLE messages ADD COLUMN IF NOT EXISTS reply_to_message_id VARCHAR(128) REFERENCES messages(id) ON DELETE SET NULL;
ALTER TABLE messages ADD COLUMN IF NOT EXISTS is_edited BOOLEAN NOT NULL DEFAULT FALSE;
ALTER TABLE messages ADD COLUMN IF NOT EXISTS is_deleted BOOLEAN NOT NULL DEFAULT FALSE;
ALTER TABLE messages ADD COLUMN IF NOT EXISTS read_at TIMESTAMP;

CREATE TABLE IF NOT EXISTS message_reactions (
    id SERIAL PRIMARY KEY,
    message_id VARCHAR(128) NOT NULL REFERENCES messages(id) ON DELETE CASCADE,
    user_id VARCHAR(128) NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    emoji VARCHAR(32) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_message_user_emoji UNIQUE (message_id, user_id, emoji)
);

CREATE INDEX IF NOT EXISTS idx_message_reactions_message_id ON message_reactions(message_id);
CREATE INDEX IF NOT EXISTS idx_messages_conversation_created ON messages(conversation_id, created_at DESC);
