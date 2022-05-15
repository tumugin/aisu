create table users
(
  `id`                      integer primary key auto_increment not null,
  `name`                    varchar(255) not null,
  `email`                   varchar(255) unique,
  `password`                varchar(255),
  `email_verified_at`       datetime,
  `force_logout_generation` integer default 0,
  `created_at`              datetime     not null,
  `updated_at`              datetime     not null
);
