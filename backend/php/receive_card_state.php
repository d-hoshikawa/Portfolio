<?php
header("Content-Type:application/Json;charset=utf-8");
$recv = json_decode(file_get_contents('php://input'), true);
$s = new PDO("mysql:host=localhost;dbname=origincard", "root", "root");
$a1 = $recv["name"];
$a2 = $recv["attack"];
$a3 = $recv["hp"];
$a4 = $recv["speed"];
$a5 = $recv["cost"];
$a6 = $recv["ability"];
$a7 = $recv["monster"];
$a8 = $recv["back"];
$a9 = $recv["crank"];
$a10 = $recv["overRide"];
$a11 = $recv["id"];
if ($a10 == "on") {
    $re = $s->query("UPDATE mycardtable SET name = '$a1', attack = $a2, hp = $a3, speed = $a4, cost = $a5, ability = $a6, monster = $a7, back = $a8, crank = $a9 WHERE id = $a11;");
} else {
    $re = $s->query("insert into mycardtable (name, attack, hp, speed, cost, ability, monster, back, crank)values('$a1',$a2,$a3,$a4,$a5,$a6,$a7,$a8,$a9)");
}
?>