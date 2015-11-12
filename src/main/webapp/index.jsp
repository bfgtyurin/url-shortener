<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>URL-shortener</title>
	<link rel="stylesheet" href="bower_components/bootstrap/dist/css/bootstrap.css">
	<link rel="stylesheet" href="assets/styles/main.css">
</head>
<body>
	<div class="container">
    <div class="row">
      <div class="col-md-4"></div>
      <div class="col-md-4">
        <form class="form-inline">
          <div class="form-group form-group-lg">
            <input class="form-control" name="link" type="text" id="shortenForm" placeholder="Place a link to shorten it">
          </div>
          <button class="btn btn-default" type="submit" id="shortenButton">SHORTEN</button>
        </form>
      </div>
      <div class="col-md-4"></div>
    </div>
	</div>
</body>
<script src="bower_components/jquery/dist/jquery.js"></script>
<script src="scripts/app/app.js"></script>
</html>
