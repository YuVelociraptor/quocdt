create table books  (
    id SERIAL PRIMARY KEY,
    title VARCHAR(100) NOT NULL,
    price DECIMAL(10, 0) NOT NULL,
    publication_status boolean NOT NULL
);

create table authors  (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    birth_date DATE NOT NULL
);

create table book_authors  (
    id SERIAL PRIMARY KEY,
    book_id INT NOT NULL,
    author_id INT NOT NULL,
    UNIQUE (book_id, author_id),
    FOREIGN KEY (book_id) REFERENCES books(id),
    FOREIGN KEY (author_id) REFERENCES authors(id)
);