package Filter;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import Connection.SingleConnectionBanco;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@WebFilter(urlPatterns = { "/Principal/*" }) // Intercepta todas as requisições que vierem do projeto ou mapeamento
public class FilterAutenticacao extends HttpFilter implements Filter {
	
	private static final long serialVersionUID = 1L;
	
	
	private static Connection connection;

	public FilterAutenticacao() {

	}

	// Encerra os processos quando o servidor é parado
	// Mataria os processo de conexão com banco
	public void destroy() {
		try {
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	// Intercepta as requisições e as respostas no sistema
	// Tudo que fizer no sistema vai passar por aqui
	// Validação de Autenticação
	// Dar commit e rolback de transação do banco
	// Validar e fazer redirecinamento do processo
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		try {
			HttpServletRequest req = (HttpServletRequest) request;
			HttpSession session = req.getSession();

			String usuarioLogado = (String) session.getAttribute("usuario");

			String urlParaAutenticar = req.getServletPath(); // url que esta sendo acessado

			// Validar se esta logado, se não direciona pra tela de loginnnn
			if (usuarioLogado == null && !urlParaAutenticar.equalsIgnoreCase("/Principal/ServeLetLogin")) { // Não esta
																											// Logado

				RequestDispatcher redireciona = request.getRequestDispatcher("/index.jsp?url=" + urlParaAutenticar);
				request.setAttribute("msg", "Por favor realize o login");
				redireciona.forward(request, response);
				return; // para a execução e retorna para o login
			} else {
				chain.doFilter(request, response);
			}
			
			connection.commit(); // Deu tudo certo Grava no Banco

		} catch (Exception e) {
			e.printStackTrace();
			RequestDispatcher redirecionar = request.getRequestDispatcher("erro.jsp");
			request.setAttribute("msg", e.getMessage());
			redirecionar.forward(request, response);
			try {
				connection.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}

		}

	}

	// Inicia os processos ou recursos quando o servidor sobe o projeto
	// Iniciar a conexão com o banco
	public void init(FilterConfig fConfig) throws ServletException {
		connection = SingleConnectionBanco.getConnection();

	}

}
