CREATE TABLE piece
(
    id   int primary key not null auto_increment,
    type varchar(20)     not null,
    camp varchar(20)     not null
);

CREATE TABLE game
(
    id          int primary key not null auto_increment,
    state       varchar(20)     not null default 'WHITE_TURN',
    winner_camp varchar(20)
);

CREATE TABLE board
(
    game_id  int         not null,
    square   varchar(20) not null,
    piece_id int         not null,
    constraint primary key (game_id, square),
    constraint foreign key (piece_id) references piece (id),
    constraint foreign key (game_id) references game (id)
);
