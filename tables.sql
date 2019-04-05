    
    create table account (
       id  bigserial not null,
        created_at timestamp,
        modified_at timestamp,
        display_name varchar(255) not null,
        email varchar(255) not null,
        password varchar(255) not null,
        primary key (id)
    );
    
    create table account_roles (
       account_id int8 not null,
        roles varchar(255)
    );
    
    create table comment (
       id  bigserial not null,
        created_at timestamp,
        modified_at timestamp,
        comment_text varchar(1000),
        created_by int8,
        post_id int8,
        primary key (id)
    );
    
    create table follow (
       following_user int8 not null,
        followed_user int8 not null,
        primary key (following_user, followed_user)
    );
    
    create table post (
       id  bigserial not null,
        created_at timestamp,
        modified_at timestamp,
        post_text varchar(255),
        created_by int8 not null,
        upload_image_id int8 not null,
        primary key (id)
    );
    
    create table uploaded_image (
       id  bigserial not null,
        created_at timestamp,
        modified_at timestamp,
        image_extension varchar(255) not null,
        image_name varchar(255) not null,
        image_path varchar(255) not null,
        original_name varchar(255) not null,
        created_by int8 not null,
        primary key (id)
    );
    
    alter table account 
       add constraint UK_q0uja26qgu1atulenwup9rxyr unique (email);
    
    alter table post 
       add constraint UK_gydilfp3lorh1su47a54ddc2l unique (upload_image_id);
    
    alter table uploaded_image 
       add constraint UK_16al7582nw4160ww1ojwpr101 unique (image_name);
    
    alter table account_roles 
       add constraint FKtp61eta5i06bug3w1qr6286uf 
       foreign key (account_id) 
       references account;
    
    alter table comment 
       add constraint FKn84amaib0wclgo6g2wl9u23ta 
       foreign key (created_by) 
       references account;
    
    alter table comment 
       add constraint FKs1slvnkuemjsq2kj4h3vhx7i1 
       foreign key (post_id) 
       references post;
    
    alter table follow 
       add constraint FKnqq3xjedg0qimqikkjsqovpkp 
       foreign key (followed_user) 
       references account;
    
    alter table follow 
       add constraint FKejcf1etc956f0whxn0u7r4hqp 
       foreign key (following_user) 
       references account;
    
    alter table post 
       add constraint FKjyhc0gx1ytuna4v8lue1hryrc 
       foreign key (created_by) 
       references account 
       on delete cascade;
    
    alter table post 
       add constraint FKe43h7qukda6h6m6my7aef7x5p 
       foreign key (upload_image_id) 
       references uploaded_image 
       on delete cascade;
    
    alter table uploaded_image 
       add constraint FKejt82iohfwn217pe25k0xsa0a 
       foreign key (created_by) 
       references account 
       on delete cascade;