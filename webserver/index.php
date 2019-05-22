
<?php
	header('Content-Type: text/json; charset=utf-8');

	ini_set('default_charset', 'utf-8');

	$ip = "localhost";
	$db = "androidapp";
	$DBusername = "";
	$DBpassword = "";


	try {
		$dbObj = new PDO('mysql:host='.$ip.';dbname='.$db, $DBusername, $DBpassword, array(PDO::MYSQL_ATTR_INIT_COMMAND => "SET NAMES utf8") );
	} catch (PDOException $e) {
		echo "db error:".$e;
	}

	$action = inGet("a");

	if($action == "login"){
		$email = inPost("email");
		$password = md5(inPost("password"));

		if($email != null){
			$row = rowSelectQuery("SELECT id, admin FROM user WHERE email=? AND password=?;", array($email, $password));
		}
		//echo json_encode(array("email" => $email, "password" => $password));
		echo json_encode(array("id" => $row["id"] ?? 0, "admin" => $row["admin"] ?? 0,"success" => is_numeric($row["id"])));
	}elseif ($action == "getContacts") {
		$id = inPost("id");
		$contacts = selectQuery("SELECT c.id, c.firstname, c.lastname, c.email, c.phonenumber FROM contact AS c INNER JOIN `user` AS u ON c.user_id=u.id WHERE u.id=?", array($id));
		echo json_encode(array("data" => $contacts));
	}elseif ($action == "getUsers") {
		$id = inPost("id");
		$admin = singleSelectQuery("SELECT admin FROM user WHERE id=?;", array($id));
		if($admin == 1){
			$users = selectQuery("SELECT id, email, admin FROM user;");
			foreach($users as $i => $user){
				$users[$i]["contacts"] = selectQuery("SELECT c.id, c.firstname, c.lastname, c.email, c.phonenumber FROM contact AS c INNER JOIN `user` AS u ON c.user_id=u.id WHERE u.id=?", array($user["id"]));
			}
			echo json_encode(array("success" => true, "data"=> $users));
		}else{
			echo json_encode(array("success" => false));
		}
	}elseif($action == "addContact"){
		$id = inPost("id");
		$firstname = inPost("firstname");
		$lastname = inPost("lastname");
		$email = inPost("email");
		$phonenumber = inPost("phonenumber");
		$success = insertQuery("INSERT INTO `contact` (`user_id`, `firstname`, `lastname`, `email`, `phonenumber`) VALUES (?, ?, ?, ?, ?);", array($id, $firstname, $lastname, $email, $phonenumber));
		echo json_encode(array("id" => $id ?? 0, "success" => $success));
	}elseif($action == "addUser"){
		$id = inPost("id");
		$email = inPost("email");
		$password = inPost("password");
		$isAdmin = inPost("admin");
		$admin = singleSelectQuery("SELECT admin FROM user WHERE id=?;", array($id));
		if($admin == 1){
			$success = insertQuery("INSERT INTO user (email, password, admin) VALUES (?, ?, ?);", array($email, md5($password), ($isAdmin == "true") ? 1 : 0));
			echo json_encode(array("success" => $success));
		}else{
			echo json_encode(array("success" => false));
		}
	}elseif($action == "editContact"){
		$contactId = inPost("contactId");
		$firstname = inPost("firstname");
		$lastname = inPost("lastname");
		$email = inPost("email");
		$phonenumber = inPost("phonenumber");
		$success = insertQuery("UPDATE contact SET firstname=?, lastname=?, email=?, phonenumber=? WHERE id=?", array($firstname, $lastname, $email, $phonenumber, $contactId));
		echo json_encode(array("id" => $id ?? 0, "success" => $success));
	}elseif($action == "editUser"){
		$id = inPost("id");
		$userId = inPost("userId");
		$email = inPost("email");
		$changePassword = inPost("changePassword");
		$isAdmin = inPost("admin");
		$admin = singleSelectQuery("SELECT admin FROM user WHERE id=?;", array($id));
		if($admin == 1){
			if($changePassword == "true"){
				$password = md5(inPost("password"));
				$success = insertQuery("UPDATE user SET email=?, password=?, admin=? WHERE id=?", array($email, $password, ($isAdmin == "true") ? 1 : 0, $userId));
			}else{
				$success = insertQuery("UPDATE user SET email=?, admin=? WHERE id=?", array($email, ($isAdmin == "true") ? 1 : 0, $userId));
			}
			echo json_encode(array("success" => $success));
		}else{
			echo json_encode(array("success" => false));
		}
	}elseif($action == "getContact"){
		$id = inPost("id");
		$contactId = inPost("contactId");
		$contact = rowSelectQuery("SELECT c.id, c.firstname, c.lastname, c.email, c.phonenumber FROM contact AS c INNER JOIN `user` AS u ON c.user_id=u.id WHERE u.id=? AND c.id=?", array($id, $contactId));
		echo json_encode(array("data" => $contact));
	}elseif($action == "getUser"){
		$id = inPost("id");
		$userId = inPost("userId");
		$admin = singleSelectQuery("SELECT admin FROM user WHERE id=?;", array($id));
		if($admin == 1 || $id == $userId){
			$user = rowSelectQuery("SELECT id, email, admin FROM user WHERE id=?", array($userId));
			$contacts = selectQuery("SELECT c.id, c.firstname, c.lastname, c.email, c.phonenumber FROM contact AS c WHERE c.user_id=?", array($userId));
			$user["contacts"] = $contacts;
			echo json_encode(array("success" => true, "data" => $user));
		}else{
			echo json_encode(array("success" => false));
		}
	}elseif($action == "deleteContact"){
		$id = inPost("id");
		$contactId = inPost("contactId");
		$success = insertQuery("DELETE FROM `contact` WHERE `id`=? AND `user_id`=?;", array($contactId, $id));
		echo json_encode(array("id" => $id ?? 0, "success" => $success));
	}elseif($action == "deleteUser"){
		$id = inPost("id");
		$userId = inPost("userId");
		$admin = singleSelectQuery("SELECT admin FROM user WHERE id=?;", array($id));
		if($admin == 1){
			$success = insertQuery("DELETE FROM `user` WHERE `id`=?;", array($userId));
			echo json_encode(array("success" => $success));
		}else{
			echo json_encode(array("success" => false));
		}
		
	}


	function inGet($str){
		return $_REQUEST[$str] ?? null;
	}
	function inPost($str){
		return json_decode(file_get_contents('php://input'), true)[$str] ?? null;
	}
	function insertQuery($sql, $data = array()){
		global $dbObj;
		try {
			$sth = $dbObj->prepare($sql);
			$sth->execute($data);
			return true;
		}catch(Exception $e){
			return false;
		}
	}
	function selectQuery($sql, $data = array()){
		global $dbObj;
		try {
			$sth = $dbObj->prepare($sql);
			$sth->execute($data);
			return $sth->fetchAll();
		}catch(Exception $e){
			return false;
		}
	}
	function singleSelectQuery($sql, $data = array()){
		global $dbObj;
		try {
			$sth = $dbObj->prepare($sql);
			$sth->execute($data);
			return $sth->fetchColumn();
		}catch(Exception $e){
			return false;
		}
	}
	function rowSelectQuery($sql, $data = array()){
		global $dbObj;
		try {
			$sth = $dbObj->prepare($sql);
			$sth->execute($data);
			return $sth->fetch();
		}catch(Exception $e){
			return false;
		}
	}
	$dbObj = null;

?>
