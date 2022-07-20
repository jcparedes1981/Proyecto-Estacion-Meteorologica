<?php

include 'conexion.php';

$pdo_conexion = new Conexion();
if ($_SERVER['REQUEST_METHOD'] == 'GET'){
    if (isset($_GET['id'])){
        $sql = $pdo_conexion->prepare("SELECT idEstacion as id, ultimaTemp as temp, ultimaHumed as hum, estado, ultimaFechaDatos as ultimoRegistro FROM tbl_dispositivos WHERE id=:id");
        $sql->bindValue(':id',$_GET['id']);
        $sql->execute();
        $sql->setFetchMode(PDO::FETCH_ASSOC);
        header('Content-Type: application/json; charset=utf-8');
        header('HTTP/1.1 200');
        echo json_encode($sql->fetchAll());
        exit;
    } else {
        $sql = $pdo_conexion->prepare("SELECT idEstacion as id, ultimaTemp as temp, ultimaHumed as hum, estado, ultimaFechaDatos as ultimoRegistro FROM tbl_dispositivos");
        $sql->execute();
        $sql->setFetchMode(PDO::FETCH_ASSOC);
        header('Content-Type: application/json; charset=utf-8');
        header('HTTP/1.1 200');
        echo json_encode($sql->fetchAll());
        exit;
    }
}



?>