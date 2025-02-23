drop database origincard;
create database origincard;
use origincard;
create table mycardtable
(id int primary key auto_increment, name text, attack int ,hp int, speed int, cost int, ability int, monster int, back int, crank int);
create table compcardtable
(id int primary key auto_increment, name text, attack int ,hp int, speed int, cost int, ability int, monster int, back int, crank int);
INSERT INTO compCardTable(name, attack, hp, speed, cost, ability, monster, back, crank) VALUES ('カード10-1 スピードブースト', 3, 4, 4, 10, 0, 5, 1, 1);
create table decktable
(id int primary key auto_increment, name text, card1 int, card2 int, card3 int);
insert into myCardTable(name, attack, hp, speed, cost, ability, monster, back, crank)values('スライム',1,7,1,10,3,3,0,1);
insert into myCardTable(name, attack, hp, speed, cost, ability, monster, back, crank)values('ダークエルフ',1,6,8,15,2,4,0,2);
insert into myCardTable(name, attack, hp, speed, cost, ability, monster, back, crank)values('鬼',8,1,2,20,4,2,0,3);
insert into decktable(name, card1, card2, card3)values('初期デッキ', 1, 2, 3);