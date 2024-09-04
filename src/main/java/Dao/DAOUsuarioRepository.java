package Dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import Connection.SingleConnectionBanco;
import Model.ModelLogin;
import Model.ModelTelefone;

public class DAOUsuarioRepository {
	
	private Connection connection;
	
//	private DAOTelefoneRepository daoTelefoneRepository = new DAOTelefoneRepository();
	
	public DAOUsuarioRepository() {
		connection = SingleConnectionBanco.getConnection();
	
	}
	
	public ModelLogin gravarUsuario(ModelLogin objeto, Long userLogado) throws Exception {
		if (objeto.isNovo()) { // Grava um Novo
			String sql = "INSERT INTO model_login(login, senha, nome, email, usuario_id, perfil, sexo, cep, logradouro, bairro, localidade, uf, numero, datanascimento, rendamensal) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
			PreparedStatement prepareSql = connection.prepareStatement(sql);
			prepareSql.setString(1, objeto.getLogin());
			prepareSql.setString(2, objeto.getSenha());
			prepareSql.setString(3, objeto.getNome());
			prepareSql.setString(4, objeto.getEmail());
			prepareSql.setLong(5, userLogado);
			prepareSql.setString(6, objeto.getPerfil());
			prepareSql.setString(7, objeto.getSexo());
			
			prepareSql.setString(8, objeto.getCep());
			prepareSql.setString(9, objeto.getLogradouro());
			prepareSql.setString(10, objeto.getBairro());
			prepareSql.setString(11, objeto.getLocalidade());
			prepareSql.setString(12, objeto.getUf());
			prepareSql.setString(13, objeto.getNumero());
			prepareSql.setDate(14, objeto.getDataNascimento());
			prepareSql.setDouble(15, objeto.getRendamensal());

			
			prepareSql.execute();
			connection.commit();
			
				if (objeto.getFotouser() != null && !objeto.getFotouser().isEmpty()) {
					sql = "update model_login set fotouser = ?, extensaofotouser = ? where login = ?";
					prepareSql = connection.prepareStatement(sql);
					prepareSql.setString(1, objeto.getFotouser());
					prepareSql.setString(2, objeto.getExtensaofotouser());
					prepareSql.setString(3, objeto.getLogin());
	
					prepareSql.execute();
					connection.commit();
					
				}
		
		} else {
			String sql = "UPDATE model_login set login=?, senha=?, nome=?, email=?, perfil=?, sexo=?, cep=?, logradouro=?, bairro=?, localidade=?, uf=?, numero=?, datanascimento=?, rendamensal=? WHERE id="+objeto.getId();
			PreparedStatement prepareSql = connection.prepareStatement(sql);
			prepareSql.setString(1, objeto.getLogin());
			prepareSql.setString(2, objeto.getSenha());
			prepareSql.setString(3, objeto.getNome());
			prepareSql.setString(4, objeto.getEmail());
			prepareSql.setString(5, objeto.getPerfil());
			prepareSql.setString(6, objeto.getSexo());

			prepareSql.setString(7, objeto.getCep());
			prepareSql.setString(8, objeto.getLogradouro());
			prepareSql.setString(9, objeto.getBairro());
			prepareSql.setString(10, objeto.getLocalidade());
			prepareSql.setString(11, objeto.getUf());
			prepareSql.setString(12, objeto.getNumero());
			prepareSql.setDate(13, objeto.getDataNascimento());
			prepareSql.setDouble(14, objeto.getRendamensal());
			
			prepareSql.executeUpdate();
			connection.commit();
			
				if (objeto.getFotouser() != null && !objeto.getFotouser().isEmpty()) {
					sql = "update model_login set fotouser = ?, extensaofotouser = ? where id = ?";
					prepareSql = connection.prepareStatement(sql);
					prepareSql.setString(1, objeto.getFotouser());
					prepareSql.setString(2, objeto.getExtensaofotouser());
					prepareSql.setLong(3, objeto.getId());
	
					prepareSql.execute();
					connection.commit();
					
				}
		}
		return this.consultaUsuario(objeto.getLogin(), userLogado);

	}
	
	
	public int totalPagina(Long userLogado) throws Exception {
		String sql = "select count(1) as total from model_login  where usuario_id = " + userLogado;
		PreparedStatement statement = connection.prepareStatement(sql);
		ResultSet resultado = statement.executeQuery();
		resultado.next();
		Double cadastros = resultado.getDouble("total");
		Double porpagina = 5.0;
		Double pagina = cadastros / porpagina;
		Double resto = pagina % 2;
		if (resto > 0) {
			pagina ++;
		}
		return pagina.intValue();
	}

	
	
	
	
public List<ModelLogin> consultaUsuarioListRel(Long userLogado) throws Exception {
		
		List<ModelLogin> retorno = new ArrayList<ModelLogin>();
		
		String sql = "select * from model_login where useradmin is false and usuario_id = " + userLogado;
		PreparedStatement statement = connection.prepareStatement(sql);
		
		ResultSet resultado = statement.executeQuery();
		
		while (resultado.next()) { /*percorrer as linhas de resultado do SQL*/
			
			ModelLogin modelLogin = new ModelLogin();
			
			modelLogin.setEmail(resultado.getString("email"));
			modelLogin.setId(resultado.getLong("id"));
			modelLogin.setLogin(resultado.getString("login"));
			modelLogin.setNome(resultado.getString("nome"));
			//modelLogin.setSenha(resultado.getString("senha"));
			modelLogin.setPerfil(resultado.getString("perfil"));
			modelLogin.setSexo(resultado.getString("sexo"));
			
			modelLogin.setTelefones(this.listFone(modelLogin.getId()));
			
			retorno.add(modelLogin);
		}
		
		
		return retorno;
	}
	
	
	public List<ModelLogin> consultaUsuarioListRel(Long userLogado, String dataInicial, String dataFinal) throws Exception {
		
		List<ModelLogin> retorno = new ArrayList<ModelLogin>();
		
		String sql = "select * from model_login where useradmin is false and usuario_id = " + userLogado + " and datanascimento >= ? and datanascimento <= ?";
		PreparedStatement statement = connection.prepareStatement(sql);
		statement.setDate(1, Date.valueOf(new SimpleDateFormat("yyyy-mm-dd").format(new SimpleDateFormat("dd/mm/yyyy").parse(dataInicial))));
		statement.setDate(2, Date.valueOf(new SimpleDateFormat("yyyy-mm-dd").format(new SimpleDateFormat("dd/mm/yyyy").parse(dataFinal))));
		
		ResultSet resultado = statement.executeQuery();
		
		while (resultado.next()) { /*percorrer as linhas de resultado do SQL*/
			
			ModelLogin modelLogin = new ModelLogin();
			
			modelLogin.setEmail(resultado.getString("email"));
			modelLogin.setId(resultado.getLong("id"));
			modelLogin.setLogin(resultado.getString("login"));
			modelLogin.setNome(resultado.getString("nome"));
			//modelLogin.setSenha(resultado.getString("senha"));
			modelLogin.setPerfil(resultado.getString("perfil"));
			modelLogin.setSexo(resultado.getString("sexo"));
			
			modelLogin.setTelefones(this.listFone(modelLogin.getId()));
			
			retorno.add(modelLogin);
		}
		
		
		return retorno;
	}
	

	
	
	
	public int consultaUsuarioListTotalPaginaPaginacao(String nome, Long userLogado) throws Exception {
		String sql = "select count(1) as total from model_login  where upper(nome) like upper(?) and useradmin is false and usuario_id = ? ";
		PreparedStatement statement = connection.prepareStatement(sql);
		statement.setString(1, "%" + nome + "%");
		statement.setLong(2, userLogado);
		ResultSet resultado = statement.executeQuery();
		resultado.next();
		Double cadastros = resultado.getDouble("total");
		Double porpagina = 5.0;
		Double pagina = cadastros / porpagina;
		Double resto = pagina % 2;
		if (resto > 0) {
			pagina ++;
		}
		return pagina.intValue();
	}


	
	
	
	
	
	public List<ModelLogin> consultaUsuarioListPaginada(Long userLogado, Integer offset) throws Exception {
		
		List<ModelLogin> retorno = new ArrayList<ModelLogin>();
		//String sql = "select * from model_login where useradmin is false and usuario_id = " + userLogado + " order by nome offset "+offset+" limit 5";
		String sql = "select * from model_login where useradmin is false and usuario_id = " + userLogado + " order by nome offset "+offset+" limit 5";
		PreparedStatement statement = connection.prepareStatement(sql);
		
		ResultSet resultado = statement.executeQuery();
		
		while (resultado.next()) { /*percorrer as linhas de resultado do SQL*/
			
			ModelLogin modelLogin = new ModelLogin();
			
			modelLogin.setEmail(resultado.getString("email"));
			modelLogin.setId(resultado.getLong("id"));
			modelLogin.setLogin(resultado.getString("login"));
			modelLogin.setNome(resultado.getString("nome"));
			//modelLogin.setSenha(resultado.getString("senha"));
			modelLogin.setPerfil(resultado.getString("perfil"));
			modelLogin.setSexo(resultado.getString("sexo"));
			
			retorno.add(modelLogin);
		}
		
		
		return retorno;
	}
	

	
	
	public List<ModelLogin> consultaUsuarioList(Long userLogado) throws Exception {
		
		List<ModelLogin> retorno = new ArrayList<ModelLogin>();
		//String sql = "select * from model_login where useradmin is false and usuario_id = " + userLogado + " limit 5";
		String sql = "select * from model_login where useradmin is false and usuario_id = " + userLogado + " limit 5";
		PreparedStatement statement = connection.prepareStatement(sql);
		
		ResultSet resultado = statement.executeQuery();
		
		while (resultado.next()) { /*percorrer as linhas de resultado do SQL*/
			
			ModelLogin modelLogin = new ModelLogin();
			
			modelLogin.setEmail(resultado.getString("email"));
			modelLogin.setId(resultado.getLong("id"));
			modelLogin.setLogin(resultado.getString("login"));
			modelLogin.setNome(resultado.getString("nome"));
			//modelLogin.setSenha(resultado.getString("senha"));
			modelLogin.setPerfil(resultado.getString("perfil"));
			modelLogin.setSexo(resultado.getString("sexo"));
			
			retorno.add(modelLogin);
		}
		
		
		return retorno;
	}

	
	public List<ModelLogin> consultaUsuaListOffSet(String nome, Long userLogado, int offset) throws Exception {
		List<ModelLogin> retorno = new ArrayList<ModelLogin>();
		String sql = "select * from model_login where upper(nome) like upper(?) and useradmin is false and usuario_id = ? offset "+offset+" limit 5";
		//String sql = "SELECT * FROM model_login WHERE upper(nome) LIKE upper(?) and useradmin is false and usuario_id = ? limit 5";
		PreparedStatement statement = connection.prepareStatement(sql);
		statement.setString(1, "%" + nome + "%");
		statement.setLong(2, userLogado);
		
		ResultSet resultado = statement.executeQuery();
		
		while (resultado.next()) { // percorer as linhas do resultado do SQL
			ModelLogin modelLogin = new ModelLogin();
			modelLogin.setEmail(resultado.getString("email"));
			modelLogin.setId(resultado.getLong("id"));
			modelLogin.setLogin(resultado.getString("login"));
			modelLogin.setNome(resultado.getString("nome"));
			modelLogin.setPerfil(resultado.getString("perfil"));
			modelLogin.setSexo(resultado.getString("sexo"));
			
			retorno.add(modelLogin);
			
		}
		return retorno;
	}

	
	
	public List<ModelLogin> consultaUsuaList(String nome, Long userLogado) throws Exception {
		List<ModelLogin> retorno = new ArrayList<ModelLogin>();
		String sql = "select * from model_login where upper(nome) like upper(?) and useradmin is false and usuario_id = ? limit 5";
		//String sql = "SELECT * FROM model_login WHERE upper(nome) LIKE upper(?) and useradmin is false and usuario_id = ? limit 5";
		PreparedStatement statement = connection.prepareStatement(sql);
		statement.setString(1, "%" + nome + "%");
		statement.setLong(2, userLogado);
		
		ResultSet resultado = statement.executeQuery();
		
		while (resultado.next()) { // percorer as linhas do resultado do SQL
			ModelLogin modelLogin = new ModelLogin();
			modelLogin.setEmail(resultado.getString("email"));
			modelLogin.setId(resultado.getLong("id"));
			modelLogin.setLogin(resultado.getString("login"));
			modelLogin.setNome(resultado.getString("nome"));
			modelLogin.setPerfil(resultado.getString("perfil"));
			modelLogin.setSexo(resultado.getString("sexo"));
			
			retorno.add(modelLogin);
			
		}
		return retorno;
	}

	
	public ModelLogin consultaUsuarioLogado(String login) throws Exception {
		
		ModelLogin modelLogin = new ModelLogin();
		
		String sql = "SELECT * FROM model_login where upper(login) = upper('"+login+"')";
		
		PreparedStatement statement = connection.prepareStatement(sql);
		//statement.setString(1, login);
		
		 ResultSet resultado = statement.executeQuery();
		 
		 while (resultado.next()) { // se tem resultado
			 modelLogin.setId(resultado.getLong("id"));
			 modelLogin.setNome(resultado.getString("nome"));
			 modelLogin.setEmail(resultado.getString("email"));
			 modelLogin.setLogin(resultado.getString("login"));
			 modelLogin.setSenha(resultado.getString("senha"));
			 modelLogin.setUseradmin(resultado.getBoolean("useradmin"));
			 modelLogin.setPerfil(resultado.getString("perfil"));
             modelLogin.setSexo(resultado.getString("sexo"));
             modelLogin.setFotouser(resultado.getString("fotouser"));
             
 			modelLogin.setCep(resultado.getString("cep"));
 			modelLogin.setLogradouro(resultado.getString("logradouro"));
 			modelLogin.setBairro(resultado.getString("bairro"));
 			modelLogin.setLocalidade(resultado.getString("localidade"));
 			modelLogin.setUf(resultado.getString("uf"));
 			modelLogin.setNumero(resultado.getString("numero"));
 			modelLogin.setDataNascimento(resultado.getDate("datanascimento"));
 			modelLogin.setRendamensal(resultado.getDouble("rendamensal"));

		 }
		 return modelLogin;
	}
	
	
	public ModelLogin consultaUsuario(String login) throws Exception {
		
		ModelLogin modelLogin = new ModelLogin();
		
		String sql = "SELECT * FROM model_login where upper(login) = upper('"+login+"') and useradmin is false ";
		
		PreparedStatement statement = connection.prepareStatement(sql);
		//statement.setString(1, login);
		
		 ResultSet resultado = statement.executeQuery();
		 
		 while (resultado.next()) { // se tem resultado
			 modelLogin.setId(resultado.getLong("id"));
			 modelLogin.setNome(resultado.getString("nome"));
			 modelLogin.setEmail(resultado.getString("email"));
			 modelLogin.setLogin(resultado.getString("login"));
			 modelLogin.setSenha(resultado.getString("senha"));
			 modelLogin.setUseradmin(resultado.getBoolean("useradmin"));
			 modelLogin.setPerfil(resultado.getString("perfil"));
             modelLogin.setSexo(resultado.getString("sexo"));
             modelLogin.setFotouser(resultado.getString("fotouser"));

  			modelLogin.setCep(resultado.getString("cep"));
  			modelLogin.setLogradouro(resultado.getString("logradouro"));
  			modelLogin.setBairro(resultado.getString("bairro"));
  			modelLogin.setLocalidade(resultado.getString("localidade"));
  			modelLogin.setUf(resultado.getString("uf"));
  			modelLogin.setNumero(resultado.getString("numero"));
 			modelLogin.setDataNascimento(resultado.getDate("datanascimento"));
 			modelLogin.setRendamensal(resultado.getDouble("rendamensal"));

		 }
		 return modelLogin;
	}
	
	
	public ModelLogin consultaUsuario(String login, Long UserLogado) throws Exception {
		
		ModelLogin modelLogin = new ModelLogin();
		
		String sql = "SELECT * FROM model_login where upper(login) = upper('"+login+"') and useradmin is false and usuario_id = " + UserLogado ;
		
		PreparedStatement statement = connection.prepareStatement(sql);
		//statement.setString(1, login);
		
		 ResultSet resultado = statement.executeQuery();
		 
		 while (resultado.next()) { // se tem resultado
			 modelLogin.setId(resultado.getLong("id"));
			 modelLogin.setNome(resultado.getString("nome"));
			 modelLogin.setEmail(resultado.getString("email"));
			 modelLogin.setLogin(resultado.getString("login"));
			 modelLogin.setSenha(resultado.getString("senha"));
			 modelLogin.setPerfil(resultado.getString("perfil"));
 			 modelLogin.setSexo(resultado.getString("sexo"));
             modelLogin.setFotouser(resultado.getString("fotouser"));

  			modelLogin.setCep(resultado.getString("cep"));
  			modelLogin.setLogradouro(resultado.getString("logradouro"));
  			modelLogin.setBairro(resultado.getString("bairro"));
  			modelLogin.setLocalidade(resultado.getString("localidade"));
  			modelLogin.setUf(resultado.getString("uf"));
  			modelLogin.setNumero(resultado.getString("numero"));
 			modelLogin.setDataNascimento(resultado.getDate("datanascimento"));
 			modelLogin.setRendamensal(resultado.getDouble("rendamensal"));

		 }
		 return modelLogin;
	}
		 
	
	public ModelLogin consultaUsuarioId(String id, Long userLogado) throws Exception {
		
		ModelLogin modelLogin = new ModelLogin();
		
		String sql = "SELECT * FROM model_login where id = ? and useradmin is false and usuario_id = ?";
		
		PreparedStatement statement = connection.prepareStatement(sql);
		statement.setLong(1, Long.parseLong(id));
		statement.setLong(2, userLogado);
	
		 ResultSet resultado = statement.executeQuery();
		 
		 while (resultado.next()) { // se tem resultado
			 modelLogin.setId(resultado.getLong("id"));
			 modelLogin.setNome(resultado.getString("nome"));
			 modelLogin.setEmail(resultado.getString("email"));
			 modelLogin.setLogin(resultado.getString("login"));
			 modelLogin.setSenha(resultado.getString("senha"));
			 modelLogin.setPerfil(resultado.getString("perfil"));
			 modelLogin.setSexo(resultado.getString("sexo"));
	         modelLogin.setFotouser(resultado.getString("fotouser"));
	         modelLogin.setExtensaofotouser(resultado.getString("extensaofotouser"));

	 			modelLogin.setCep(resultado.getString("cep"));
	 			modelLogin.setLogradouro(resultado.getString("logradouro"));
	 			modelLogin.setBairro(resultado.getString("bairro"));
	 			modelLogin.setLocalidade(resultado.getString("localidade"));
	 			modelLogin.setUf(resultado.getString("uf"));
	 			modelLogin.setNumero(resultado.getString("numero"));
	 			modelLogin.setDataNascimento(resultado.getDate("datanascimento"));
	 			modelLogin.setRendamensal(resultado.getDouble("rendamensal"));

		 }
		 return modelLogin;
	}

	public ModelLogin consultaUsuarioId(Long id) throws Exception {
		
		ModelLogin modelLogin = new ModelLogin();
		
		String sql = "SELECT * FROM model_login where id = ? and useradmin is false ";
		
		PreparedStatement statement = connection.prepareStatement(sql);
		statement.setLong(1, id);
	
		 ResultSet resultado = statement.executeQuery();
		 
		 while (resultado.next()) { // se tem resultado
			 modelLogin.setId(resultado.getLong("id"));
			 modelLogin.setNome(resultado.getString("nome"));
			 modelLogin.setEmail(resultado.getString("email"));
			 modelLogin.setLogin(resultado.getString("login"));
			 modelLogin.setSenha(resultado.getString("senha"));
			 modelLogin.setPerfil(resultado.getString("perfil"));
			 modelLogin.setSexo(resultado.getString("sexo"));
	         modelLogin.setFotouser(resultado.getString("fotouser"));
	         modelLogin.setExtensaofotouser(resultado.getString("extensaofotouser"));

	 			modelLogin.setCep(resultado.getString("cep"));
	 			modelLogin.setLogradouro(resultado.getString("logradouro"));
	 			modelLogin.setBairro(resultado.getString("bairro"));
	 			modelLogin.setLocalidade(resultado.getString("localidade"));
	 			modelLogin.setUf(resultado.getString("uf"));
	 			modelLogin.setNumero(resultado.getString("numero"));
	 			modelLogin.setDataNascimento(resultado.getDate("datanascimento"));
	 			modelLogin.setRendamensal(resultado.getDouble("rendamensal"));

		 }
		 return modelLogin;
	}

	
	public boolean validarLogin(String login) throws Exception {
		String sql = "SELECT count(1) > 0 as existe FROM model_login where upper(login) = upper('"+login+"')";
		PreparedStatement statement = connection.prepareStatement(sql);
		ResultSet resultado = statement.executeQuery();
		resultado.next(); // para ele entrar nos resultado
		return resultado.getBoolean("existe");
	
	}	
		
	
	public void deletarUser(String idUser) throws Exception{
		String sql = "DELETE FROM model_login where ID = ? and useradmin is false";
		PreparedStatement preparaSql = connection.prepareStatement(sql);
		preparaSql.setLong(1, Long.parseLong(idUser));
		preparaSql.executeLargeUpdate();
		connection.commit();
	}
	

	
public List<ModelTelefone> listFone(Long idUserPai) throws Exception {
		
		List<ModelTelefone> retorno = new ArrayList<ModelTelefone>();
		
		String sql = "select * from telefone where usuario_pai_id = ?";
		PreparedStatement preparedStatement = connection.prepareStatement(sql);
		
		preparedStatement.setLong(1, idUserPai);
		
		ResultSet rs = preparedStatement.executeQuery();
		
		while (rs.next()) {
			
			ModelTelefone modelTelefone = new ModelTelefone();
			
			modelTelefone.setId(rs.getLong("id"));
			modelTelefone.setNumero(rs.getString("numero"));
			modelTelefone.setUsuario_cad_id(this.consultaUsuarioId(rs.getLong("usuario_cad_id")));
			modelTelefone.setUsuario_pai_id(this.consultaUsuarioId(rs.getLong("usuario_pai_id")));
			
			retorno.add(modelTelefone);
			
		}
		
		return retorno;
	}

}
