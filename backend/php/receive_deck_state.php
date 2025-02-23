<?php
header("Content-Type:application/Json;charset=utf-8");
$recv = json_decode(file_get_contents('php://input'), true);
$s = new PDO("mysql:host=localhost;dbname=origincard", "root", "root");
$a1 = $recv["name"];
$a2 = $recv["card1"];
$a3 = $recv["card2"];
$a4 = $recv["card3"];
$a5 = $recv["d_id"];
if ($a5 != 0) {
    $re = $s->query("UPDATE decktable SET name = '$a1', card1 = $a2, card2 = $a3, card3 = $a4 WHERE id = $a5;");
} else {
    $re = $s->query("insert into decktable (name, card1, card2, card3)values('$a1',$a2,$a3,$a4)");
}
?>