<!-- #!/usr/bin/php-cgi -->
<!DOCTYPE html>
<html>
<title>Hewwo</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
<head>
<link rel="stylesheet" href="./syles.css">

</head>

<script src="./jsStash.js"></script>




<body bgcolor="#f1f2f6" id="waa" style="height: 100%; width: 100% padding: 0px; margin: 0; min-width: 400px;" onload="logon('loginRefresh')">
</body>




<?php
    error_reporting(E_ERROR | E_PARSE);

    // print header
    $file = fopen("./head.html", "r") or die("Unable to open file!");
    while(!feof($file)) {
        echo fgets($file);
    }
    fclose($file);


    $page= "./actualhome.html"; // default page
    $pageToGo = $_POST['pageTOGO'];


    // get page 
    if ($pageToGo == "4") {
        $page = "./login.html";
    }
    else if ($pageToGo == "3") {
        $page = "./cv.html";
    }
    else if ($pageToGo == "2") {
        $page = "./about.html";
    }

    else if ($pageToGo == "1") {
        $page = "./courses.html";
    }
    else {
        $page = "./actualhome.html";
    }


    // print content 
    $file = fopen($page, "r") or die("Unable to open file!");
    while(!feof($file)) {
        echo fgets($file);
    }
    fclose($file);


    // print footer
    $file = fopen("./foot.html", "r") or die("Unable to open file!");
    while(!feof($file)) {
        echo fgets($file);
    }
    fclose($file);
?>



</html>