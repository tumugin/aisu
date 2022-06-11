create table `group_idols`
(
  `id`         bigint unsigned primary key auto_increment not null,
  `group_id`   bigint unsigned                            not null,
  `idol_id`    bigint unsigned                            not null,
  `created_at` datetime                                   not null,
  `updated_at` datetime                                   not null,
  foreign key (`group_id`) references `groups` (`id`) on delete cascade,
  foreign key (`idol_id`) references `idols` (`id`) on delete cascade,
  unique (`group_id`, `idol_id`)
)
