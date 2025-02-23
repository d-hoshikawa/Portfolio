<?php
header("Content-Type:application/Json;charset=utf-8");
$recv = json_decode(file_get_contents('php://input'), true);
$s = new PDO("mysql:host=localhost;dbname=origincard", "root", "root");
$a1 = $recv["d_id"];
$re = $s->query("delete from decktable where id = $a1");
?>