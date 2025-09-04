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
        min_amount NUMERIC(12, 2) NOT NULL,
        max_amount NUMERIC(12, 2) NOT NULL,
        interest_rate NUMERIC(5, 2) NOT NULL,
        automatic_validation BOOLEAN NOT NULL DEFAULT false
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

    INSERT INTO statuses (id, name) VALUES (1, 'PENDIENTE_REVISION');
    INSERT INTO statuses (id, name) VALUES (2, 'APROBADO');
    INSERT INTO statuses (id, name) VALUES (3, 'RECHAZADO');

    INSERT INTO loan_types (id, name, min_amount, max_amount, interest_rate, automatic_validation) VALUES (1, 'Personal Loan', 1000.00, 50000.00, 12.50, true);
    INSERT INTO loan_types (id, name, min_amount, max_amount, interest_rate, automatic_validation) VALUES (2, 'Mortgage Loan', 50001.00, 500000.00, 5.75, false);
    INSERT INTO loan_types (id, name, min_amount, max_amount, interest_rate, automatic_validation) VALUES (3, 'Car Loan', 5000.00, 80000.00, 8.20, true);
