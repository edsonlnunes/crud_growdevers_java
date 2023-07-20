CREATE TABLE growdevers_skills(
    id UUID primary key,
    name varchar(50) not null,
    growdever_id UUID not null references growdevers(id),
    created_at timestamp NOT NULL DEFAULT now(),
    updated_at timestamp NULL
);