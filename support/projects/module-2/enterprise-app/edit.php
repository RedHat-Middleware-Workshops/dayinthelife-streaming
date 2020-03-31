<?php
echo '
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="utf-8">
    <title>International Inc</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">

    <link href="/css/bootstrap.css" rel="stylesheet">
    <style>
        body {
            padding-top: 60px;
            /* 60px to make the container go all the way to the bottom of the topbar */
        }
        tr:hover { 
          background: red; 
        }
    </style>
    <link href="/css/bootstrap-responsive.css" rel="stylesheet">
</head>

<body>

    <div class="navbar navbar-inverse navbar-fixed-top">
        <div class="navbar-inner">
            <div class="container-fluid">
                <button type="button" class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </button>
                <a class="brand" href="#">Orders Deluxe &#39;95</a>
                <div class="nav-collapse collapse">
                    <p class="navbar-text pull-right">
                        Logged in as <a href="#" class="navbar-link">SuperAdmin</a>
                    </p>
                    <ul class="nav">
                        <li class="active"><a href="#">Home</a></li>
                        <li><a href="#invoice">Invoices</a></li>
                        <li><a href="#inventory">Inventory</a></li>
                        <li><a href="#accounts">Accounts</a></li>
                        <li><a href="#shipments">Shipments</a></li>
                        <li><a href="#reports">Reports</a></li>
                    </ul>
                </div>
                <!--/.nav-collapse -->
            </div>
        </div>
    </div>

    <div class="container-fluid">
    <div class="container-fluid">
    <div class="row-fluid">
        <div class="span3">
            <div class="well sidebar-nav">
                <ul class="nav nav-list">
                    <li class="nav-header">Type</li>
                    <li class="active"><a href="#">Entered</a></li>
                    <li><a href="#">Retrieved</a></li>
                    <li><a href="#">Imported</a></li>
                    <li><a href="#">Generated</a></li>
                    <li class="nav-header">Status</li>
                    <li><a href="#">Open</a></li>
                    <li><a href="#">Printed</a></li>
                    <li><a href="#">Deleted</a></li>
                    <li><a href="#">Posted</a></li>
                </ul>
            </div>
            <!--/.well -->
        </div>
        <!--/span-->
        <div class="span9">
            <div class="hero-unit">
            <h1>Entered Order</h1>
';

if ( isset($_GET["order"]) == false ) 
{
  die("No order selected");
}

$order = json_decode(base64_decode(htmlspecialchars($_GET["order"])), true);

if ( isset($_POST["submit"]) == true ) 
{
  /* Connect to the remote server using SQL Server Authentication and   
  specify the InternationalDB database as the database in use. */
  $serverName = $_ENV["SERVER_NAME"] ? $_ENV["SERVER_NAME"] : "localhost, 1433";
  $connectionOptions = array(
      "Database"=>"InternationalDB",  
      "Authentication"=>"SqlPassword",
      "UID"=>"sa", "PWD"=>"Password!",
      "TrustServerCertificate"=>true);  
  $conn = sqlsrv_connect($serverName, $connectionOptions);
  if($conn == false) 
  {
    die(var_dump(sqlsrv_errors()));
  }
  /* update row */
  $query = "UPDATE [InternationalDB].[dbo].[Orders]
            SET [OrderItemName] = (?), [Quantity] = (?), [ShipmentAddress] = (?), [ZipCode] = (?) 
            WHERE [OrderId] = (?)";
  $params = array($_POST["OrderItemName"], $_POST["Quantity"], $_POST["ShipmentAddress"], $_POST["ZipCode"], $_POST["OrderId"]);

  $stmt = sqlsrv_query( $conn, $query, $params);

  if( $stmt === false)  
  {
    echo "Error in statement execution: <br />";
    die( print_r( sqlsrv_errors(), true));
  }  
  /* Free statement resources. */
  sqlsrv_free_stmt( $stmt );
  
  /* Free connection resources. */
  sqlsrv_close( $conn );

  echo '<span class="label label-success">Order Saved!</span>';

  $order = array_merge($order, $_POST);
}  

echo '
            <form class="form-horizontal" method="post">
              <fieldset>
                <legend>Edit Order ' . $order["OrderId"] . '</legend>
                <input type="hidden" name="OrderId" value="' . $order["OrderId"] . '">
                <label>Item Name</label>
                <input type="text" name="OrderItemName" placeholder="OrderItemName" value="' . $order["OrderItemName"] . '">
                <label>Quantity</label>
                <input type="text" name="Quantity" placeholder="Quantity" value="' . $order["Quantity"] . '">
                <label>Shipment Address</label>
                <input type="text" name="ShipmentAddress"  placeholder="ShipmentAddress" value="' . $order["ShipmentAddress"] . '">
                <label>Zip Code</label>
                <input type="text" name="ZipCode"  placeholder="ZipCode" value="' . $order["ZipCode"] . '">
                <span class="help-block">Click Save to update the record.</span>
                <button type="submit" name="submit" class="btn">Save</button>
              </fieldset>
            </form>
        </div>
        <!--/span-->
    </div>
    <!--/row-->
  </div>
<hr>

<footer>
<p>&copy; International Inc 1995</p>
</footer>

</div> <!-- /container -->

<!-- Placed at the end of the document so the pages load faster -->
<script src="/js/jquery.js"></script>
<script src="/js/bootstrap.js"></script>

</body>

</html>
';
?>