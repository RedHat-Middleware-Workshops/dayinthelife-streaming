<?php echo 
'
<!DOCTYPE html>
<html>
<head>
    <title>International Order System 95</title>
    <link href="/css/bootstrap.css" rel="stylesheet">
    <style>
      body {
        padding-top: 60px; /* 60px to make the container go all the way to the bottom of the topbar */
      }
    </style>
    <!--link href="/css/bootstrap-responsive.css" rel="stylesheet"-->
</head>
<body>

<div class="navbar navbar-inverse navbar-fixed-top">
<div class="navbar-inner">
  <div class="container">
    <button type="button" class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
      <span class="icon-bar"></span>
      <span class="icon-bar"></span>
      <span class="icon-bar"></span>
    </button>
    <a class="brand" href="#">Project name</a>
    <div class="nav-collapse collapse">
      <ul class="nav">
        <li class="active"><a href="#">Home</a></li>
        <li><a href="#about">About</a></li>
        <li><a href="#contact">Contact</a></li>
      </ul>
    </div><!--/.nav-collapse -->
  </div>
</div>
</div>

<div class="container">

<h1>Bootstrap starter template</h1>
<p>Use this document as a way to quick start any new project.<br> All you get is this message and a barebones HTML document.</p>

<form action="upload.php" method="post" enctype="multipart/form-data">
Select image to upload:
<input type="file" name="fileToUpload" id="fileToUpload">
<input type="submit" value="Upload Image" name="submit">
</form>

</div> <!-- /container -->
    
</body>
</html>
'; ?>