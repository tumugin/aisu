create table `chekis`
(
  `id`            bigint unsigned primary key auto_increment not null,
  `user_id`       bigint unsigned                            not null,
  `idol_id`       bigint unsigned,
  `regulation_id` bigint unsigned,
  `quantity`      integer                                    not null,
  `shot_at`       datetime                                   not null,
  `created_at`    datetime                                   not null,
  `updated_at`    datetime                                   not null,
  foreign key (`user_id`) references `users` (`id`) on delete cascade,
  foreign key (`idol_id`) references `idols` (`id`) on delete set null,
  foreign key (`regulation_id`) references `regulations` (`id`) on delete set null
)
