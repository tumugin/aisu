create table `regulations`
(
  `id`         bigint unsigned primary key auto_increment not null,
  `group_id`   bigint unsigned                            not null,
  `user_id`    bigint unsigned,
  `name`       varchar(255)                               not null,
  `comment`    text                                       not null,
  `unit_price` integer                                    not null,
  `status`     varchar(255)                               not null,
  `created_at` datetime                                   not null,
  `updated_at` datetime                                   not null,
  foreign key (`group_id`) references `groups` (`id`) on delete cascade,
  foreign key (`user_id`) references `users` (`id`) on delete set null
);
