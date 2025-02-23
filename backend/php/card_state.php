<?php
header('Content-Type: application/json; charset=utf-8');
$recv = json_decode(file_get_contents('php://input'), true);
$s = new PDO("mysql:host=localhost;dbname=origincard", "root", "root");
$a = $recv["crank"];

if ($a == 0) {
    $re = $s->query("select * from mycardtable");
    $rows = array();
    while ($row = $re->fetch(PDO::FETCH_ASSOC)) {
        $rows[] = $row;
    }
} else {
    $re = $s->query("select * from mycardtable where crank = $a");
    $rows = array();
    while ($row = $re->fetch(PDO::FETCH_ASSOC)) {
        $rows[] = $row;
    }
}
echo json_encode($rows);
?>