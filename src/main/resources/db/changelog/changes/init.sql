CREATE TABLE users (
                       id UUID PRIMARY KEY,
                       name TEXT,
                       login TEXT UNIQUE,
                       password_hash TEXT,
                       role TEXT NOT NULL DEFAULT 'USER'
);

CREATE TABLE paintings (
                           id SERIAL PRIMARY KEY,
                           title VARCHAR(255) NOT NULL,
                           style VARCHAR(255),
                           year_created INT,
                           version INT DEFAULT 0,
                           user_id UUID REFERENCES users(id) ON DELETE CASCADE
);