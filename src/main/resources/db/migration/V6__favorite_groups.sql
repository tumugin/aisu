create table `favorite_groups`
(
  `id`         bigint unsigned primary key auto_increment not null,
  `user_id`    bigint unsigned                            not null,
  `group_id`   bigint unsigned                            not null,
  `created_at` datetime                                   not null,
  `updated_at` datetime                                   not null,
  foreign key (`user_id`) references `users` (`id`) on delete cascade,
  foreign key (`group_id`) references `groups` (`id`) on delete cascade
)
