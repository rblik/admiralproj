create table frontal_message
(
  id serial not null
    constraint frontal_message_pkey
    primary key,
  color varchar(255),
  content varchar(255),
  date bigint,
  header varchar(255)
)
;