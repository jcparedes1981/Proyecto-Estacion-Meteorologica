<?php

include 'conexion.php';

$pdo_conexion = new Conexion();

/*"SELECT  ROUND(AVG(temperatura),2) as temp, CONCAT(DATE(fechaHora),' ',HOUR(fechaHora),':',
(CASE WHEN minute(fechaHora)<15 THEN '00'
	WHEN minute(fechaHora)<30 THEN '15'
    WHEN minute(fechaHora)<45 THEN '30'
    ELSE '45' END),':00') as time, idEstacion as idDispositivo
FROM tbl_datosclima2
WHERE '2022-07-17' = DATE(fechaHora) AND idEstacion=:idDispositivo
GROUP BY idEstacion, CONCAT(DATE(fechaHora),'',HOUR(fechaHora),':',
(CASE WHEN minute(fechaHora)<15 THEN '00'
	WHEN minute(fechaHora)<30 THEN '15'
    WHEN minute(fechaHora)<45 THEN '30'
    ELSE '45' END),':00')
ORDER BY fechaHora";*/

if ($_SERVER['REQUEST_METHOD'] == 'GET'){
    if (isset($_GET['id'])){
        $sql = $pdo_conexion->prepare("SELECT @i:=@i+1 as indice, ROUND(AVG(temperatura),2) as temp, CONCAT(DATE(fechaHora),' ',HOUR(fechaHora),':',
                                    (CASE WHEN minute(fechaHora)<15 THEN '00'
                                        WHEN minute(fechaHora)<30 THEN '15'
                                        WHEN minute(fechaHora)<45 THEN '30'
                                        ELSE '45' END),':00') as time, idEstacion as idDispositivo
                                    FROM tbl_datosclima2, (SELECT @i:=0) r
                                    WHERE CURRENT_DATE() = DATE(fechaHora) AND idEstacion=:id
                                    GROUP BY idEstacion, CONCAT(DATE(fechaHora),'',HOUR(fechaHora),':',
                                    (CASE WHEN minute(fechaHora)<15 THEN '00'
                                        WHEN minute(fechaHora)<30 THEN '15'
                                        WHEN minute(fechaHora)<45 THEN '30'
                                        ELSE '45' END),':00')
                                    ORDER BY fechaHora");
        
        $sql->bindValue(':id',$_GET['id']);
        $sql->execute();
        $sql->setFetchMode(PDO::FETCH_ASSOC);
        header('Content-Type: application/json; charset=utf-8');
        header('HTTP/1.1 200');
        echo json_encode($sql->fetchAll());
        exit;
    } 
}



?>