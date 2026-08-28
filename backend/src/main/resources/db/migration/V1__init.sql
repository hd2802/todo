CREATE TABLE tasks (
    id SERIAL PRIMARY KEY,
    title VARCHAR(127),
    description VARCHAR(255),
    completed BOOLEAN,
    dueDate TIMESTAMPTZ
);