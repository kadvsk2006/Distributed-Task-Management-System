CREATE TABLE task (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(100) NOT NULL,
    description VARCHAR(500),
    status VARCHAR(20),
    priority VARCHAR(20),
    due_date DATE,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    assigned_to BIGINT REFERENCES users(id)
);
