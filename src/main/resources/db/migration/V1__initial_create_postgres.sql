create table "users"
(
  "id"                      bigserial primary key not null,
  "name"                    varchar(255)          not null,
  "email"                   varchar(255) unique,
  "password"                varchar(255),
  "email_verified_at"       timestamptz,
  "force_logout_generation" integer default 0,
  "created_at"              timestamptz           not null,
  "updated_at"              timestamptz           not null
);

create table "groups"
(
  "id"         bigserial primary key not null,
  "user_id"    bigint,
  "name"       varchar(255)          not null,
  "status"     varchar(255)          not null,
  "created_at" timestamptz           not null,
  "updated_at" timestamptz           not null,
  constraint fk_user_id foreign key ("user_id") references "users" ("id") on delete set null
);

create table "idols"
(
  "id"         bigserial primary key not null,
  "user_id"    bigint,
  "name"       varchar(255)          not null,
  "status"     varchar(255)          not null,
  "created_at" timestamptz           not null,
  "updated_at" timestamptz           not null,
  constraint fk_user_id foreign key ("user_id") references "users" ("id") on delete set null
);

create table "regulations"
(
  "id"         bigserial primary key not null,
  "group_id"   bigint                not null,
  "user_id"    bigint,
  "name"       varchar(255)          not null,
  "comment"    text                  not null,
  "unit_price" integer               not null,
  "status"     varchar(255)          not null,
  "created_at" timestamptz           not null,
  "updated_at" timestamptz           not null,
  constraint fk_group_id foreign key ("group_id") references "groups" ("id") on delete cascade,
  constraint fk_user_id foreign key ("user_id") references "users" ("id") on delete set null
);

create table "chekis"
(
  "id"            bigserial primary key not null,
  "user_id"       bigint                not null,
  "idol_id"       bigint,
  "regulation_id" bigint,
  "quantity"      integer               not null,
  "shot_at"       timestamptz           not null,
  "created_at"    timestamptz           not null,
  "updated_at"    timestamptz           not null,
  constraint fk_user_id foreign key ("user_id") references "users" ("id") on delete cascade,
  constraint fk_idol_id foreign key ("idol_id") references "idols" ("id") on delete set null,
  constraint fk_regulation_id foreign key ("regulation_id") references "regulations" ("id") on delete set null
);

create table "favorite_groups"
(
  "id"         bigserial primary key not null,
  "user_id"    bigint                not null,
  "group_id"   bigint                not null,
  "created_at" timestamptz           not null,
  "updated_at" timestamptz           not null,
  constraint fk_user_id foreign key ("user_id") references "users" ("id") on delete cascade,
  constraint fk_group_id foreign key ("group_id") references "groups" ("id") on delete cascade
);

create table "group_idols"
(
  "id"         bigserial primary key not null,
  "group_id"   bigint                not null,
  "idol_id"    bigint                not null,
  "created_at" timestamptz           not null,
  "updated_at" timestamptz           not null,
  constraint fk_group_id foreign key ("group_id") references "groups" ("id") on delete cascade,
  constraint fk_idol_id foreign key ("idol_id") references "idols" ("id") on delete cascade,
  unique ("group_id", "idol_id")
);

create table "admin_users"
(
  "id"                      bigserial primary key not null,
  "name"                    varchar(255)          not null,
  "email"                   varchar(255) unique,
  "password"                varchar(255),
  "force_logout_generation" integer default 0,
  "created_at"              timestamptz           not null,
  "updated_at"              timestamptz           not null
);

create table "auth0_users"
(
  "id"            bigserial primary key not null,
  "user_id"       bigint                not null unique,
  "auth0_user_id" varchar(255)          not null unique,
  "created_at"    timestamptz           not null,
  "updated_at"    timestamptz           not null,
  constraint fk_user_id foreign key ("user_id") references "users" ("id") on delete cascade
);
