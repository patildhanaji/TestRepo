<html>

<head>
<title>Simple Blog App</title>
</head>

<body>
	<font color="red">${errorMessage}</font>
	<form method="post">
		User Id : <input type="text" name="userId" />
		Title : <input type="text" name="postTitle" />
		Body : <input type="text" name="postBody" />
		<input type="hidden" name="loginUser" value="admin" />		 
		<input type="submit" />
	</form>
</body>

</html>