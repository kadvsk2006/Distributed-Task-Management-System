CREATE TABLE public.users (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE
);

ALTER TABLE public.users
    OWNER TO postgres;
