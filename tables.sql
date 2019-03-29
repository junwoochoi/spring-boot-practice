drop table account if exists;
drop table account_roles if exists;
drop table comment if exists;
drop table follow if exists;
drop table post if exists;
drop table upload_image if exists;
create table account (id bigint generated by default as identity, created_at timestamp, modified_at timestamp, diplay_name varchar(255) not null, email varchar(255) not null, password varchar(255) not null, primary key (id));
create table account_roles (account_id bigint not null, roles varchar(255));
create table comment (id bigint generated by default as identity, created_at timestamp, modified_at timestamp, comment_text varchar(1000), created_by bigint, post_id bigint, primary key (id));
create table follow (following_user bigint not null, followed_user bigint not null, primary key (following_user, followed_user));
create table post (id bigint generated by default as identity, created_at timestamp, modified_at timestamp, post_text varchar(255), created_by bigint not null, upload_image_id bigint not null, primary key (id));
create table upload_image (id bigint generated by default as identity, created_at timestamp, modified_at timestamp, image_extension varchar(255) not null, image_name varchar(255) not null, image_path varchar(255) not null, original_name varchar(255) not null, created_by bigint, primary key (id));
alter table account add constraint UK_q0uja26qgu1atulenwup9rxyr unique (email);
alter table post add constraint UK_gydilfp3lorh1su47a54ddc2l unique (upload_image_id);
alter table account_roles add constraint FKtp61eta5i06bug3w1qr6286uf foreign key (account_id) references account;
alter table comment add constraint FKn84amaib0wclgo6g2wl9u23ta foreign key (created_by) references account;
alter table comment add constraint FKs1slvnkuemjsq2kj4h3vhx7i1 foreign key (post_id) references post;
alter table follow add constraint FKnqq3xjedg0qimqikkjsqovpkp foreign key (followed_user) references account;
alter table follow add constraint FKejcf1etc956f0whxn0u7r4hqp foreign key (following_user) references account;
alter table post add constraint FKjyhc0gx1ytuna4v8lue1hryrc foreign key (created_by) references account on delete cascade;
alter table post add constraint FK11fqfr2drpeqtyw5esdc7asfp foreign key (upload_image_id) references upload_image;
alter table upload_image add constraint FKk5tykc5yxq0y8jtv6li7aj1ei foreign key (created_by) references account on delete cascade