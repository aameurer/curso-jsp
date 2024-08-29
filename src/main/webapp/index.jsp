<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	
<!DOCTYPE html>
<html lang="en">

<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
	rel="stylesheet"
	integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH"
	crossorigin="anonymous">

<style type="text/css">

form {
	position: absolute;
	top: 40%;
	left: 33%;
	right: 33%
}

h5 {
	position: absolute;
	top: 30%;
	left: 33%;
}

.msg {
	position: absolute;
	top: 10%;
	left: 33%;
	font-size: 15px;
	color: #664d03;
	background-color: #fff3cd;
    border-color: #ffecb5;
}

</style>

<title>Curso JSP</title>

</head>


<body>

	<h5>Bem Vindo ao Curso JSP</h5>

	<!-- Aqui chama a tela ServLetLogin  -->
	<form action="<%=request.getContextPath() %>/ServeLetLogin" method="post"
		class="row g-3 needs-validation" novalidate>
		<input type="hidden" value="<%=request.getParameter("url")%>" name=url>

		<div class="mb-3">
			<label class="form-label">Login</label> 
			<input class="form-control"	name="login" type="text" required="required">
			<div class="invalid-feedback">Digite o Login</div>
			<div class="valid-feedback">Login Ok</div>
		</div>

		<div class="mb-3">
			<label class="form-label">Senha</label> 
			<input class="form-control"	name="senha" type="password" required="required">
			<div class="invalid-feedback">Digite a Senha</div>
			<div class="valid-feedback">Senha Ok</div>
		</div>


		<!-- 		<div class="col-12"> -->
		<input class="btn btn-primary" type="submit" value="Acessar">
		<!-- 		</div> -->

	</form>

	<!-- Menssagem da validação do BackEnd  -->

	<h5 class="msg">${msg}</h5>

	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
		integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz"
		crossorigin="anonymous"></script>



	<script type="text/javascript">
		// Example starter JavaScript for disabling form submissions if there are invalid fields
		(() => {
		  'use strict'

		  // Fetch all the forms we want to apply custom Bootstrap validation styles to
		  const forms = document.querySelectorAll('.needs-validation')

		  // Loop over them and prevent submission
		  Array.from(forms).forEach(form => {
		    form.addEventListener('submit', event => {
		      if (!form.checkValidity()) {
		        event.preventDefault()
		        event.stopPropagation()
		      }

		      form.classList.add('was-validated')
		    }, false)
		  })
		})()
		</script>


</body>
</html>