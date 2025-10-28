CREATE USER 'aiuser'@'%' IDENTIFIED BY '1234#';

create schema company;
create schema library;
create schema restaurant;

grant all on company.* to 'aiuser'@'%';
grant all on library.* to 'aiuser'@'%';
grant all on restaurant.* to 'aiuser'@'%';

flush privileges;