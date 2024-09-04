package Servelets;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.sql.Date;

import org.apache.commons.compress.utils.IOUtils;
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
				
				request.setAttribute("totalPagina", daoUsuarioRepository.totalPagina(this.getUserLogado(request)));
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
					response.addHeader("totalPagina", ""+daoUsuarioRepository.consultaUsuarioListTotalPaginaPaginacao(nomeBusca, super.getUserLogado(request)));
					response.getWriter().write(json);
					
			}
			
			else if (acao != null && !acao.isEmpty() && acao.equalsIgnoreCase("buscarUserAjaxPage")) {
				String nomeBusca = request.getParameter("nomeBusca");
				String pagina = request.getParameter("pagina");
				
				List<ModelLogin> dadosJsonUser = daoUsuarioRepository.consultaUsuaListOffSet(nomeBusca, super.getUserLogado(request), Integer.parseInt(pagina));
				
				ObjectMapper mapper = new ObjectMapper();
				String json = mapper.writeValueAsString(dadosJsonUser);
				
				// System.out.println(dadosJsonUser);
				response.addHeader("totalPagina", ""+daoUsuarioRepository.consultaUsuarioListTotalPaginaPaginacao(nomeBusca, super.getUserLogado(request)));
				response.getWriter().write(json);
			}			
			
			else if (acao != null && !acao.isEmpty() && acao.equalsIgnoreCase("buscarEditar")) {
				String id = request.getParameter("id");
//	System.out.println(id);				
				ModelLogin modelLogin = daoUsuarioRepository.consultaUsuarioId(id, super.getUserLogado(request));
				
//	System.out.println(modelLogin);
				
	            List<ModelLogin> modelLogins = daoUsuarioRepository.consultaUsuarioList(super.getUserLogado(request));
	 		    request.setAttribute("modelLogins", modelLogins);
				
				request.setAttribute("msg", "Usuário em Edição");
				request.setAttribute("modelLogin", modelLogin); // para manter os dados nos campos na tela
				request.setAttribute("totalPagina", daoUsuarioRepository.totalPagina(this.getUserLogado(request)));

				request.getRequestDispatcher("Principal/usuario.jsp").forward(request, response);
				
			}
			
			 else if (acao != null && !acao.isEmpty() && acao.equalsIgnoreCase("listarUser")) {
				 
				 List<ModelLogin> modelLogins = daoUsuarioRepository.consultaUsuarioList(super.getUserLogado(request));
				 
				 request.setAttribute("msg", "Usuários carregados");
			     request.setAttribute("modelLogins", modelLogins);
					request.setAttribute("totalPagina", daoUsuarioRepository.totalPagina(this.getUserLogado(request)));

				 request.getRequestDispatcher("Principal/usuario.jsp").forward(request, response);
				 
			 }
			
			 else if (acao != null && !acao.isEmpty() && acao.equalsIgnoreCase("downloadFoto")) {
//System.out.println("download - - - -chegou na imagem ");					 
				 String idUser = request.getParameter("id");
				 
				 ModelLogin modelLogin =  daoUsuarioRepository.consultaUsuarioId(idUser, super.getUserLogado(request));
				 if (modelLogin.getFotouser() != null && !modelLogin.getFotouser().isEmpty()) {
					 
					 response.setHeader("Content-Disposition", "attachment;filename=arquivo." + modelLogin.getExtensaofotouser());
					 response.getOutputStream().write(new org.apache.tomcat.util.codec.binary.Base64().decodeBase64(modelLogin.getFotouser().split("\\,")[1]));
//System.out.println(" decoder download - - - -chegou na imagem ");					 
					 
				 }
				 
			 }

			
			 else if (acao != null && !acao.isEmpty() && acao.equalsIgnoreCase("paginar")) {
				 Integer offset = Integer.parseInt(request.getParameter("pagina"));
				 
				 List<ModelLogin> modelLogins = daoUsuarioRepository.consultaUsuarioListPaginada(this.getUserLogado(request), offset);
				 
				 request.setAttribute("modelLogins", modelLogins);
			     request.setAttribute("totalPagina", daoUsuarioRepository.totalPagina(this.getUserLogado(request)));
				 request.getRequestDispatcher("Principal/usuario.jsp").forward(request, response);
			 }
			
			
			 else if (acao != null && !acao.isEmpty() && acao.equalsIgnoreCase("imprimirRelatorioUser")) {
				 
				 String dataInicial = request.getParameter("dataInicial");
				 String dataFinal = request.getParameter("dataFinal");
				 
				
				 if (dataInicial == null || dataInicial.isEmpty() && dataFinal == null || dataFinal.isEmpty()) {
					 
					 request.setAttribute("listaUser", daoUsuarioRepository.consultaUsuarioListRel(super.getUserLogado(request)));
					 
				 }else {
					
					 request.setAttribute("listaUser", daoUsuarioRepository.consultaUsuarioListRel(super.getUserLogado(request), dataInicial, dataFinal));
					 
				 }
				 
				 
				 request.setAttribute("dataInicial", dataInicial);
				 request.setAttribute("dataFinal", dataFinal);
				 request.getRequestDispatcher("Principal/reluser.jsp").forward(request, response);
				 
			 }


			else {
	            List<ModelLogin> modelLogins = daoUsuarioRepository.consultaUsuarioList(super.getUserLogado(request));
	 		    request.setAttribute("modelLogins", modelLogins);
				request.setAttribute("totalPagina", daoUsuarioRepository.totalPagina(this.getUserLogado(request)));

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
		
		String cep = request.getParameter("cep");
		String logradouro = request.getParameter("logradouro");
		String bairro = request.getParameter("bairro");
		String localidade = request.getParameter("localidade");
		String uf = request.getParameter("uf");
		String numero = request.getParameter("numero");
		String dataNascimento = request.getParameter("dataNascimento");
		String rendaMensal = request.getParameter("rendamensal");
		
		// converter os dados numericos pra gravar certo no banco
		rendaMensal = rendaMensal.split("\\ ")[1].replaceAll("\\.", "").replaceAll("\\,", ".");
		
		
		
		ModelLogin modelLogin = new ModelLogin();
		
		modelLogin.setId(id != null && !id.isEmpty() ? Long.parseLong(id) : null);
		modelLogin.setNome(nome);
		modelLogin.setEmail(email);
		modelLogin.setLogin(login);
		modelLogin.setSenha(senha);
		modelLogin.setPerfil(perfil);
		modelLogin.setSexo(sexo);
		
		modelLogin.setCep(cep);
		modelLogin.setLogradouro(logradouro);
		modelLogin.setBairro(bairro);
		modelLogin.setLocalidade(localidade);
		modelLogin.setUf(uf);
		modelLogin.setNumero(numero);
		
		// Date.valueOf(new SimpleDateFormat("yyyy-mm-dd").format(new SimpleDateFormat("dd/mm/yyyy").parse(dataNascimento)));
		
		modelLogin.setDataNascimento(Date.valueOf(new SimpleDateFormat("yyyy-mm-dd").format(new SimpleDateFormat("dd/mm/yyyy").parse(dataNascimento))));
		modelLogin.setRendamensal(Double.valueOf(rendaMensal));
		
System.out.println("antes - - - -chegou na imagem ");	


		if (request.getPart("fileFoto") != null) {
			Part part = request.getPart("fileFoto");/* Pega a foto no frontend */
		
			if (part.getSize() > 0) {
				byte[] foto = IOUtils.toByteArray(part.getInputStream());
		
				String imagemBase64 = "data:image/" + part.getContentType() + ";base64,"
						+ new org.apache.tomcat.util.codec.binary.Base64().encodeBase64String(foto);
		
				modelLogin.setFotouser(imagemBase64);
				modelLogin.setExtensaofotouser(part.getContentType().split("\\/")[0]);
			}
		}

		
//		if (ServletFileUpload.isMultipartContent(request)) {
//System.out.println("chegou na imagem ");
//			Part part = request.getPart("fileFoto"); /*Pega foto da tela*/
//			if (part.getSize() > 0) {
//				byte[] foto = IOUtils.toByteArray(part.getInputStream()); /*Converte imagem para byte*/
//				String imagemBase64 = "data:image/" + part.getContentType().split("\\/")[1] + ";base64," +  Base64().encodeBase64String(foto);
//				String imagemBase64 = "data:image/" + part.getContentType().split("\\/")[1] + ";base64," +  new Base64().encodeBase64String(foto);
//				modelLogin.setFotouser(imagemBase64);
//				modelLogin.setExtensaofotouser(part.getContentType().split("\\/")[1]);
//System.out.println("chegou na imagem ");
//System.out.println(imagemBase64);
//			}
		//}
		
		
		
		
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
		request.setAttribute("totalPagina", daoUsuarioRepository.totalPagina(this.getUserLogado(request)));

		request.getRequestDispatcher("Principal/usuario.jsp").forward(request, response);
		
		} catch (Exception e) {
			e.printStackTrace();
			RequestDispatcher redirecionar = request.getRequestDispatcher("erro.jsp");
			request.setAttribute("msg", e.getMessage());
			redirecionar.forward(request, response);
		}

	}

}
