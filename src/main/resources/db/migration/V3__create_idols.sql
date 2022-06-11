create table `idols`
(
  `id`         bigint unsigned primary key auto_increment not null,
  `user_id`    bigint unsigned,
  `name`       varchar(255)                               not null,
  `status`     varchar(255)                               not null,
  `created_at` datetime                                   not null,
  `updated_at` datetime                                   not null,
  foreign key (`user_id`) references `users` (`id`) on delete set null
)
