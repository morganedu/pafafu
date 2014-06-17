<%@ page language="java" contentType="text/html; charset=US-ASCII"
	pageEncoding="US-ASCII"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=US-ASCII">
<title>Insert title here</title>
</head>
<body>
	<form method="post" action="/register">
		<div>
			First Name: <input type="text" name="firstName" id="firstName" />
		</div>
		<div>
			Last Name: <input type="text" name="lastName" id="lastName" />
		</div>
		<div>
			Sreen Name: <input type="text" name="screenName" id="screenName" />
		</div>
		<div>
			Email Address: <input type="text" name="emailAddress" id="emailAddress" />
		</div>
		<div>
			Department: <input type="text" name="department" id="department" />
		</div>
		<div>
			<input type="submit" value="Send" class="buttton" />
		</div>
	</form>
</body>
</html>