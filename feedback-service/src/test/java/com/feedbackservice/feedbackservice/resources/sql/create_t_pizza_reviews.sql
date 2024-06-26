create table t_pizza_reviews (
    id serial not null primary key,
    c_review varchar(500),
    c_rating int not null,
    c_pizza_id bigint not null,
    created_at timestamp,
    updated_at timestamp
);