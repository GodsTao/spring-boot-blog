-- 创建数据库
create table blog_user
(
  id       bigint auto_increment
    primary key,
  username varchar(20)  not null,
  password varchar(100) not null,
  email    varchar(50)  not null,
  avatar   varchar(200) null
);

create table blog_blog
(
  id           bigint auto_increment
    primary key,
  comment_size int          null,
  content      longtext     not null,
  create_time  datetime     not null,
  html_content longtext     not null,
  read_size    int          null,
  summary      varchar(300) not null,
  tags         varchar(100) null,
  title        varchar(50)  not null,
  vote_size    int          null,
  user_id      bigint       null,
  catalog_id   bigint       null
)
  engine = MyISAM;

create index FKajrrhg3j1ltc7f52n577khrpb
  on blog_blog (catalog_id);

create index FKspc1h2fxr2k6dnrx8puig0pr4
  on blog_blog (user_id);
  
  
create table blog_authority
(
  id   bigint auto_increment
    primary key,
  name varchar(20) not null
);


create table blog_catalog
(
  id      bigint auto_increment
    primary key,
  name    varchar(30) not null,
  user_id bigint      null
)
  engine = MyISAM;

create index FKgfv89d8dlj4xqso1l7u6pkr04
  on blog_catalog (user_id);


create table blog_comment
(
  id          bigint auto_increment
    primary key,
  content     varchar(500) not null,
  create_time datetime     not null,
  user_id     bigint       null
)
  engine = MyISAM;

create index FK3am81ig361vr2bpjen8rmxq4u
  on blog_comment (user_id);
  
  

create table blog_vote
(
  id          bigint auto_increment
    primary key,
  create_time datetime not null,
  user_id     bigint   null
)
  engine = MyISAM;

create index FKasvb3wss1julbmodnfl6bewj3
  on blog_vote (user_id);



create table blog_vote
(
  id          bigint auto_increment
    primary key,
  create_time datetime not null,
  user_id     bigint   null
)
  engine = MyISAM;

create index FKasvb3wss1julbmodnfl6bewj3
  on blog_vote (user_id);



create table blog_vote
(
  id          bigint auto_increment
    primary key,
  create_time datetime not null,
  user_id     bigint   null
)
  engine = MyISAM;

create index FKasvb3wss1julbmodnfl6bewj3
  on blog_vote (user_id);

