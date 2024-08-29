package Servelets;

import java.io.IOException;
import java.util.List;

import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
//import org.apache.commons.io.IOUtils;
import org.apache.tomcat.util.codec.binary.Base64;

import com.fasterxml.jackson.databind.ObjectMapper;

import Dao.DAOUsuarioRepository;
import Model.ModelLogin;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;


@MultipartConfig
@WebServlet( urlPatterns =  {"/ServeLetUsuarioController"})
public class ServeLetUsuarioController extends ServeLetGenericUtil {
	private static final long serialVersionUID = 1L;
	
	DAOUsuarioRepository daoUsuarioRepository = new DAOUsuarioRepository();
       
    public ServeLetUsuarioController() {
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			String acao = request.getParameter("acao");
			if (acao != null && !acao.isEmpty() && acao.equalsIgnoreCase("deletar")) {
				String idUser = request.getParameter("id");
				daoUsuarioRepository.deletarUser(idUser);
				
	            List<ModelLogin> modelLogins = daoUsuarioRepository.consultaUsuarioList(super.getUserLogado(request));
	 		    request.setAttribute("modelLogins", modelLogins);
				
				request.setAttribute("msg", "Excluido com Sucesso!");
				request.getRequestDispatcher("Principal/usuario.jsp").forward(request, response);
			} 
			else if (acao != null && !acao.isEmpty() && acao.equalsIgnoreCase("deletarajax")) {
				String idUser = request.getParameter("id");
				daoUsuarioRepository.deletarUser(idUser);
				response.getWriter().write("Excluido com Sucesso!");
			} 
			else if (acao != null && !acao.isEmpty() && acao.equalsIgnoreCase("buscarUserAjax")) {
					String nomeBusca = request.getParameter("nomeBusca");
					
					List<ModelLogin> dadosJsonUser = daoUsuarioRepository.consultaUsuaList(nomeBusca, super.getUserLogado(request));
					
					ObjectMapper mapper = new ObjectMapper();
					String json = mapper.writeValueAsString(dadosJsonUser);
					
					// System.out.println(dadosJsonUser);
					
					response.getWriter().write(json);
					
			}
			else if (acao != null && !acao.isEmpty() && acao.equalsIgnoreCase("buscarEditar")) {
				String id = request.getParameter("id");
	System.out.println(id);				
				ModelLogin modelLogin = daoUsuarioRepository.consultaUsuarioId(id, super.getUserLogado(request));
				
	System.out.println(modelLogin);
				
	            List<ModelLogin> modelLogins = daoUsuarioRepository.consultaUsuarioList(super.getUserLogado(request));
	 		    request.setAttribute("modelLogins", modelLogins);
				
				request.setAttribute("msg", "Usuário em Edição");
				request.setAttribute("modelLogin", modelLogin); // para manter os dados nos campos na tela
				request.getRequestDispatcher("Principal/usuario.jsp").forward(request, response);
				
			}
			
			 else if (acao != null && !acao.isEmpty() && acao.equalsIgnoreCase("listarUser")) {
				 
				 List<ModelLogin> modelLogins = daoUsuarioRepository.consultaUsuarioList(super.getUserLogado(request));
				 
				 request.setAttribute("msg", "Usuários carregados");
			     request.setAttribute("modelLogins", modelLogins);
				 request.getRequestDispatcher("Principal/usuario.jsp").forward(request, response);
				 
			 }
			
			 else if (acao != null && !acao.isEmpty() && acao.equalsIgnoreCase("downloadFoto")) {
				 
				 String idUser = request.getParameter("id");
				 
				 ModelLogin modelLogin =  daoUsuarioRepository.consultaUsuarioId(idUser, super.getUserLogado(request));
				 if (modelLogin.getFotouser() != null && !modelLogin.getFotouser().isEmpty()) {
					 
					 response.setHeader("Content-Disposition", "attachment;filename=arquivo." + modelLogin.getExtensaofotouser());
					 response.getOutputStream().write(new Base64().decodeBase64(modelLogin.getFotouser().split("\\,")[1]));
					 
				 }
				 
			 }

			else {
	            List<ModelLogin> modelLogins = daoUsuarioRepository.consultaUsuarioList(super.getUserLogado(request));
	 		    request.setAttribute("modelLogins", modelLogins);

				request.getRequestDispatcher("Principal/usuario.jsp").forward(request, response);
			}
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
			RequestDispatcher redirecionar = request.getRequestDispatcher("erro.jsp");
			request.setAttribute("msg", e.getMessage());
			redirecionar.forward(request, response);
		}
		
		
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try {
		
			String msg = "Operação realizada com sucesso!";
			
		String id = request.getParameter("id");
		String nome = request.getParameter("nome");
		String email = request.getParameter("email");
		String login = request.getParameter("login");
		String senha = request.getParameter("senha");
		String perfil = request.getParameter("perfil");
		String sexo = request.getParameter("sexo");
		
		ModelLogin modelLogin = new ModelLogin();
		
		modelLogin.setId(id != null && !id.isEmpty() ? Long.parseLong(id) : null);
		modelLogin.setNome(nome);
		modelLogin.setEmail(email);
		modelLogin.setLogin(login);
		modelLogin.setSenha(senha);
		modelLogin.setPerfil(perfil);
		modelLogin.setSexo(sexo);
		
System.out.println("antes - - - -chegou na imagem ");	
		
		if (ServletFileUpload.isMultipartContent(request)) {
			
System.out.println("chegou na imagem ");

			Part part = request.getPart("fileFoto"); /*Pega foto da tela*/
			
			if (part.getSize() > 0) {
				byte[] foto = IOUtils.toByteArray(part.getInputStream()); /*Converte imagem para byte*/
//				String imagemBase64 = "data:image/" + part.getContentType().split("\\/")[1] + ";base64," +  Base64().encodeBase64String(foto);
				String imagemBase64 = "data:image/" + part.getContentType().split("\\/")[1] + ";base64," +  new Base64().encodeBase64String(foto);
				
				modelLogin.setFotouser(imagemBase64);
				modelLogin.setExtensaofotouser(part.getContentType().split("\\/")[1]);
				
				
System.out.println("chegou na imagem ");
System.out.println(imagemBase64);
			}
		}
		
		
		
		
		if (daoUsuarioRepository.validarLogin(modelLogin.getLogin()) && modelLogin.getId() == null) {
			msg = "Já existe um usuário com o mesmo login, informe outro login";
			
		} else {
			if (modelLogin.isNovo()) {
				msg = "Gravado com Sucesso!";
			}else {msg = "Atualizado com Sucesso!";
			}
			modelLogin = daoUsuarioRepository.gravarUsuario(modelLogin, super.getUserLogado(request));
		}

        List<ModelLogin> modelLogins = daoUsuarioRepository.consultaUsuarioList(super.getUserLogado(request));
		request.setAttribute("modelLogins", modelLogins);

		
		request.setAttribute("msg", msg);
		request.setAttribute("modelLogin", modelLogin); // para manter os dados nos campos na tela
		request.getRequestDispatcher("Principal/usuario.jsp").forward(request, response);
		
		} catch (Exception e) {
			e.printStackTrace();
			RequestDispatcher redirecionar = request.getRequestDispatcher("erro.jsp");
			request.setAttribute("msg", e.getMessage());
			redirecionar.forward(request, response);
		}

	}

}
