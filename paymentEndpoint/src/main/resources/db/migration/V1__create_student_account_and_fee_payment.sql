-- Create StudentAccount table

CREATE TABLE student_accounts (
    id BIGSERIAL PRIMARY KEY,
    student_number CHARACTER VARYING(255) UNIQUE,
    initial_balance NUMERIC(15,2) NOT NULL DEFAULT 0,
    current_balance NUMERIC(15,2) NOT NULL DEFAULT 0,
    next_due_date CHARACTER VARYING(50)
);

-- Create FeePayment table

CREATE TABLE fee_payments (
    id BIGSERIAL PRIMARY KEY,
    student_account_id BIGINT NOT NULL,
    student_number CHARACTER VARYING(255) NOT NULL,
    payment_amount NUMERIC(15,2) NOT NULL,
    incentive_rate NUMERIC(5,2),
    incentive_amount NUMERIC(15,2),
    payment_date CHARACTER VARYING(50) NOT NULL,
    next_payment_due_date CHARACTER VARYING(100) NOT NULL,
    previous_balance NUMERIC(15,2),
    new_balance NUMERIC(15,2),
    CONSTRAINT fk_student FOREIGN KEY (student_account_id)
        REFERENCES student_accounts (id)
);
