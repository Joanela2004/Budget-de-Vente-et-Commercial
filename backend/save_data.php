<?php
include 'db_config.php';

header('Content-Type: application/json');

// on ne traite que POST
if($_SERVER['REQUEST_METHOD']==='POST'){

    $json=file_get_contents('php://input');
    $data = json_decode($json, true);

    if(!empty($data)){

        $t = $data['periode_t'];
        $annee = $data['annee'];
        $vi=$data['quantite_vendu'];

        $prix = isset($data['prix_unitaire']) ? $data['prix_unitaire']:0;
        $cout = isset($data['cout_unitaire']) ? $data['cout_unitaire'] : 0;
        $fixes = isset($data['charges_fixes']) ? $data['charges_fixes'] : 0;

        //preparons la requete pour inserer
        $sql="INSERT INTO ventes(periode_t,annee,quantite_vendu, prix_unitaire, cout_unitaire) VALUES (?,?,?,?,?,?)";
        $stmt=$con->prepare($sql);
        $stmt->bind_param("iidddd", $t, $annee, $vi, $prix, $cout, $fixes);

        if($stmt->execute()){

            echo json_code(["status"=>"success","message"=>"ligne enregistrée avec succes"]);

        }else{
            echo json_code(["status"=>"error","message"=>"Erreur SQL".$con->error]);

        }
        $stmt->close();
    }else{
        echo json_encode(["status" => "error", "message" => "Données JSON vides ou invalides"]);
    }
}else{
    echo json_encode(["status" => "error", "message" => "Méthode POST requise"]);
}
$con->close();
?>