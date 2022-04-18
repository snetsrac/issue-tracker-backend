CREATE TYPE issue_status AS ENUM ('OPEN', 'IN_PROGRESS', 'MORE_INFO_NEEDED', 'RESOLVED');

CREATE TYPE issue_priority AS ENUM ('HIGH', 'MEDIUM', 'LOW');

CREATE TABLE issue (
    id INTEGER PRIMARY KEY GENERATED BY DEFAULT AS IDENTITY,
    title VARCHAR(128) NOT NULL,
    description TEXT NOT NULL,
    status issue_status NOT NULL,
    priority issue_priority NOT NULL,
    submitter_id VARCHAR(128)
);
