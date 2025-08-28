    DROP TABLE IF EXISTS loan_applications;
    DROP TABLE IF EXISTS loan_types;
    DROP TABLE IF EXISTS statuses;

    CREATE TABLE statuses (
        id BIGSERIAL PRIMARY KEY,
        name VARCHAR(100) NOT NULL
    );

    CREATE TABLE loan_types (
        id BIGSERIAL PRIMARY KEY,
        name VARCHAR(255) NOT NULL,
        interest_rate NUMERIC(5, 2) NOT NULL
    );

    CREATE TABLE loan_applications (
        id BIGSERIAL PRIMARY KEY,
        user_id VARCHAR(255) NOT NULL,
        loan_type_id BIGINT NOT NULL REFERENCES loan_types(id),
        status_id BIGINT NOT NULL REFERENCES statuses(id),
        amount NUMERIC(15, 2) NOT NULL,
        term INT NOT NULL,
        application_date TIMESTAMP NOT NULL
    );

    INSERT INTO statuses (id, name) VALUES (1, 'PENDING_REVIEW');
    INSERT INTO statuses (id, name) VALUES (2, 'APPROVED');
    INSERT INTO statuses (id, name) VALUES (3, 'REJECTED');

    INSERT INTO loan_types (id, name, interest_rate) VALUES (1, 'Personal Loan', 12.50);
    INSERT INTO loan_types (id, name, interest_rate) VALUES (2, 'Mortgage Loan', 5.75);
    INSERT INTO loan_types (id, name, interest_rate) VALUES (3, 'Car Loan', 8.20);
