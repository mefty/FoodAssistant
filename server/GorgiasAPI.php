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
		
		$outputFile='./output.txt';
		$myfile = fopen($outputFile, "w");
		$newOutput="Dear ".$name.",\n\nI'm sending you this e-mail to inform you about the food you are about to cook on ".$dateName.", ".$day."/".$month."/".$year.":\n\n";
		$newOutput=$newOutput.$output."Sincerely,\nYour food assistant!";
		
		fwrite($myfile,$newOutput);
		fclose($myfile);
		if($_REQUEST['mailing']=="mail"){
			mail("$email","FoodAssistant for ".$dateName.", ".$day."/".$month."/".$year,$newOutput);
		}
	}
}
?>