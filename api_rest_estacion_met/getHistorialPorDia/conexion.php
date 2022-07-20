<?php

include 'config.php';

class Conexion extends PDO{

    public function __construct(){        
        try{            
            parent::__construct('mysql:host='. DB_SERVIDOR . ';dbname=' . DB_NOMBRE . ';charset=utf8',
            DB_USUARIO, DB_PASS, array(PDO::ATTR_ERRMODE => PDO::ERRMODE_EXCEPTION));            
        } catch(PDOException $e){
            echo 'Error: ' . $e->getMessage();
            exit;
        }
    }
}

?>