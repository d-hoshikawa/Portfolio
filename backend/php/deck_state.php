<?php
$s = new PDO("mysql:host=localhost;dbname=origincard", "root", "root");

$re = $s->query("select distinct mycardtable.*, decktable.id as d_id, decktable.name as d_name from mycardtable join decktable on mycardtable.id = decktable.card1 or mycardtable.id = decktable.card2 or mycardtable.id = decktable.card3");
$rows = array();

while ($row = $re->fetch(PDO::FETCH_ASSOC)) {
    $rows[] = $row;
}

header('Content-Type: application/json; charset=utf-8');
echo json_encode($rows);
?>