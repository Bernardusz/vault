CREATE TABLE IF NOT EXISTS users(
    id SERIAL PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS credentials(
    id SERIAL PRIMARY KEY,
    user_id INT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    service_name VARCHAR(255) NOT NULL,
    username VARCHAR(255) NOT NULL,
    description VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL
)
