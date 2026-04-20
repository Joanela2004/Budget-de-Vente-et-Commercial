<?php
header("Content-Type: application/json");
require "db_config.php";

$method = $_SERVER['REQUEST_METHOD'];

switch($method){
    case 'GET':
  $result = $con->query("SELECT * FROM ventes ORDER by t ASC");
    $ventes = [];
    
    if ($result) {
        while($row = $result->fetch_assoc()){
            $ventes[] = $row;
        }
    }
    echo json_encode($ventes);
        break;
    case 'POST':
        //ajoutons une vente
        $data = json_decode(file_get_contents("php://input"),true);
        if($data){
            $stmt = $con->prepare("INSERT INTO ventes (t, annee , vi) VALUES (?,?,?)");
            $stmt->bind_param("iid",$data['t'], $data['annee'], $data['vi']);
            if($stmt->execute()){
                echo json_encode(["status"=>"success", "message"=>"ajouté"]);

            }else{
                echo json_encode(["status"=>"error", "message"=>"Erreur SQL"]);

            }
        }
        break;
    case 'PUT':
       $data = json_decode(file_get_contents("php://input"), true);
        $stmt = $con->prepare("UPDATE ventes SET vi = ? WHERE annee = ?");
        $stmt->bind_param("di", $data['vi'], $data['annee']); // d = double, i = integer
        
        if($stmt->execute()){
            echo json_encode(["status"=>"success", "message"=>"Vente mise à jour"]);
        } else {
            echo json_encode(["status"=>"error", "message"=>"Erreur SQL"]);
        }        break;
    case 'DELETE':
     if(isset($_GET['annee'])){
        $annee = $_GET['annee'];

        // 1. On récupère le 't' de la ligne à supprimer
        $stmt_get = $con->prepare("SELECT t FROM ventes WHERE annee = ?");
        $stmt_get->bind_param("i", $annee);
        $stmt_get->execute();
        $result = $stmt_get->get_result();
        
        if($row = $result->fetch_assoc()) {
            $t_deleted = $row['t'];

            
            $stmt_del = $con->prepare("DELETE FROM ventes WHERE annee = ?");
            $stmt_del->bind_param("i", $annee);
            $stmt_del->execute();

            $stmt_upd = $con->prepare("UPDATE ventes SET t = t - 1 WHERE t > ?");
            $stmt_upd->bind_param("i", $t_deleted);
            $stmt_upd->execute();

            echo json_encode(["status"=>"success", "message"=>"Supprimé et index réajusté"]);
        } else {
            echo json_encode(["status"=>"error", "message"=>"Donnée introuvable"]);
        }
        break;
    }
        
    }
$con->close();
?>