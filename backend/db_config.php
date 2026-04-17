<?php
$host = "localhost";
$user="nella";
$pwd="281104";
$dbname="db_madmeca";

$con = new mysqli($host, $name , $pwd , $dbname);
if($con->connect_error){
    die(json_encode([
        "status"=>"error",
        "message"=>"Echec de la connexion:".$con->connect_error
    ]));
}
$con->set_charset("utf8");
echo "connexion réussie";

?>