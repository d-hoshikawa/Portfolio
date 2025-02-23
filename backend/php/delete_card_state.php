<?php
header("Content-Type:application/Json;charset=utf-8");
$recv = json_decode(file_get_contents('php://input'), true);
$s = new PDO("mysql:host=localhost;dbname=origincard", "root", "root");
$a1 = $recv["id"];
$re = $s->query("delete from mycardtable where id = $a1");
$re2 = $s->query("delete from decktable where card1 = $a1 or card3 = $a1 or card3 = $a1");
?>