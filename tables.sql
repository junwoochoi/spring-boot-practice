create table account (id  bigserial not null, created_at timestamp, modified_at timestamp, diplay_name varchar(255) not null, email varchar(255) not null, password varchar(255) not null, primary key (id));
create table account_roles (account_id int8 not null, roles varchar(255));
create table comment (id  bigserial not null, created_at timestamp, modified_at timestamp, comment_text varchar(1000), created_by_id int8, primary key (id));
create table post (id  bigserial not null, created_at timestamp, modified_at timestamp, post_text varchar(255), account_id int8 not null, uploaded_image_id int8 not null, primary key (id));
create table post_comments (post_id int8 not null, comments_id int8 not null);
create table uploaded_image (id  bigserial not null, created_at timestamp, modified_at timestamp, image_extension varchar(255) not null, image_name varchar(255) not null, image_path varchar(255) not null, original_name varchar(255) not null, primary key (id));
alter table if exists account add constraint UK_q0uja26qgu1atulenwup9rxyr unique (email);
alter table if exists post add constraint UK_oddg2dfm740d7nssfjpi0fne9 unique (uploaded_image_id);
alter table if exists post_comments add constraint UK_gq9be62nx9c9hc0uyhakey771 unique (comments_id);
alter table if exists account_roles add constraint FKtp61eta5i06bug3w1qr6286uf foreign key (account_id) references account;
alter table if exists comment add constraint FK2trh03u71xfea8bmpqxbqa4h9 foreign key (created_by_id) references account;
alter table if exists post add constraint FKe5hjewhnd6trrdgt8i6uapkhy foreign key (account_id) references account on delete cascade;
alter table if exists post add constraint FK8p6u2v4hqdc4semaj39tnxj7q foreign key (uploaded_image_id) references uploaded_image;
alter table if exists post_comments add constraint FKrvgf8o4dg5kamt01me5gjqodf foreign key (comments_id) references comment;
alter table if exists post_comments add constraint FKmws3vvhl5o4t7r7sajiqpfe0b foreign key (post_id) references post