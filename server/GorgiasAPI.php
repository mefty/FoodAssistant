<?php
if(isset($_REQUEST['action'])){
	if($_REQUEST['action']=="executeJava"){
		$json = file_get_contents('php://input');
			$obj = json_decode($json);
			$name = $obj->{'name'};
			$email = $obj->{'email'};
			$moneyPP = $obj->{'moneyPP'};
			$meatAllergy = $obj->{'meatAllergy'};
			$molluscsAllergy = $obj->{'molluscsAllergy'};
			$day = $obj->{'day'};
			$month = $obj->{'month'};
			$year = $obj->{'year'};
			$dateName = $obj->{'dateName'};
		
		$gorgiasFile='./scenario.pl';
		$myfile = fopen($gorgiasFile, "w");
		fwrite($myfile, ":-consult('./environment.pl').\n");
		if($meatAllergy)
			fwrite($myfile, "allergyMeat.\n");
		if($molluscsAllergy)
			fwrite($myfile, "allergyMolluscs.\n");
		fwrite($myfile, "money(".$moneyPP.").\n"); 
		fwrite($myfile, "day(".$day.").\n");
		fwrite($myfile, "month(".$month.").\n");
		fwrite($myfile, "year(".$year.").\n");
		fwrite($myfile, "nameday(".$dateName.").\n");		
		fclose($myfile);
			
		$command="java -jar gorgias.jar ".escapeshellarg($gorgiasFile);
		$output= shell_exec(escapeshellcmd($command));
		echo $output;
		$output = str_replace('#',"\n", $output);
		$output = str_replace('|',"\n", $output);
		
		$outputFile='./output.txt';
		$myfile = fopen($outputFile, "w");
		$newOutput="Dear ".$name.",\n\nI'm sending you this e-mail to inform you about the food you are about to cook on ".$dateName.", ".$day."/".$month."/".$year.":\n\n";
		$newOutput=$newOutput.$output."Sincerely,\nYour food assistant!";
		
		fwrite($myfile,$newOutput);
		fclose($myfile);
		if($_REQUEST['mailing']=="mail"){
			require_once('phpmailer/src/PHPMailer.php');
			require_once("phpmailer/src/SMTP.php");
			require_once("phpmailer/src/OAuth.php");
			require_once("phpmailer/src/Exception.php");
			require_once("phpmailer/src/POP3.php");
			$mail = new PHPMailer\PHPMailer\PHPMailer();
			$mail->isSMTP();
			
			$mail->CharSet="UTF-8";
			$mail->Host = "smtp.gmail.com";
			$mail->SMTPDebug = 1; 
			$mail->Port = 465;
			
			$mail->SMTPSecure='ssl';
			$mail->SMTPAuth=true;
			$mail->isHTML(true);
			
			$mail->Username = 'foodassistant2018@gmail.com';
			$mail->Password = 'food2018';
			
			$mail->setFrom("foodassistant2018@gmail.com");
			$mail->AddAddress($email);
			$mail->Subject = "FoodAssistant for ".$dateName.", ".$day."/".$month."/".$year;
			$mail->Body = nl2br($newOutput);
			
			if(!$mail->Send()) {
				echo "Mailer Error: " . $mail->ErrorInfo;
			} else {
				echo "Message has been sent";
			}
		}
	}
}
?>