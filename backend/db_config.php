<?php
$host = "localhost";
$user=""; // votre nom d' utilisateur
$pwd=""; // utilisez votre mot de passe 
$dbname="db_madmeca";

$con = new mysqli($host, $user , $pwd , $dbname);
if($con->connect_error){
    die(json_encode([
        "status"=>"error",
        "message"=>"Echec de la connexion:".$con->connect_error
    ]));
}
$con->set_charset("utf8");

?>