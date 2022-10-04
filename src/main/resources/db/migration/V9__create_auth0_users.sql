create table `auth0_users`
(
  `id`            bigint unsigned primary key auto_increment not null,
  `user_id`       bigint unsigned                            not null unique,
  `auth0_user_id` varchar(255)                               not null unique,
  `created_at`    datetime                                   not null,
  `updated_at`    datetime                                   not null,
  foreign key (`user_id`) references `users` (`id`) on delete cascade
)
