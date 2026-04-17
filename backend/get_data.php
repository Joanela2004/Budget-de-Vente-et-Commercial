<?php
include 'db_config.php';
header('Content-Type: application/json');

$sql = "SELECT * FROM ventes ORDER BY periode_t ASC";
$result=$con->query($sql);

$ventes=[];
if($result->num_rows > 0){
    while($row=$result->fetch_assoc()){
        $ventes[]=$row;
    }
    echo json_encode(["status"=>"success", "data"=>$ventes]);

}else{
    echo json_encode(["status"=>"succes","data"=>[], "message"=>"Aucune donnée trouvé"]);
}

$con->close();
?>