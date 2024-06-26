create table t_favourite_pizza (
    id serial not null primary key,
    c_pizza_id bigint not null,
    created_at timestamp,
    updated_at timestamp
);