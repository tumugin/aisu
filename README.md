# aisu
aisu is a cheki management application backend made with Kotlin.

## Requirements
- JDK 27 for development
- PostgreSQL 15 or newer
- Redis 7.0.0 or newer

## Upgrading the Compose database to PostgreSQL 19

If you have an existing Compose database, back it up while it is still running
on PostgreSQL 18. Restore the backup into PostgreSQL 19 or migrate the cluster
with [pg_upgrade](https://www.postgresql.org/docs/19/pgupgrade.html).
The PostgreSQL 19 container uses a separate data directory within the volume.
