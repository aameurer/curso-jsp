<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Tela que Mostra os Erros</title>
</head>
<body>
<h1>Menssagem de Erro, Entre em contato com a equipe de suporte do Sistema.</h1>

<%
out.print(request.getAttribute("msg"));
%>

</body>
</html>