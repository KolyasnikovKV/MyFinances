create table person(
                     id serial primary key not null,
                     e_mail varchar(60) not null,
                     password varchar(15) not null,
                     nick_name varchar(25),
                     full_name varchar(60)
);

create table categorie (
                         id serial primary key not null,
                         description varchar(25) not null
);

create table currency (
                        id serial primary key not null,
                        name_of_currency varchar(20) not null
);


create table account (
                       id serial primary key not null,
                       number_account integer,
                       person_id int not null references person(id),
                       balance money not null,
                       currency_id int not null references currency(id),
                       description varchar(60)
);

create table transaction (
                           id serial primary key not null,
                           account_id int not null references account(id),
                           sum money not null,
                           date date not null,
                           categorie_id int not null references categorie(id)
);




create index name_mail
  on person (e_mail);

create index account_index
  on transaction (account_id);

