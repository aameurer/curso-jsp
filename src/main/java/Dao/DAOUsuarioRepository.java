package Dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import Connection.SingleConnectionBanco;
import Model.ModelLogin;

public class DAOUsuarioRepository {
	
	private Connection connection;
	
	public DAOUsuarioRepository() {
		connection = SingleConnectionBanco.getConnection();
	
	}
	
	public ModelLogin gravarUsuario(ModelLogin objeto, Long userLogado) throws Exception {
		if (objeto.isNovo()) { // Grava um Novo
			String sql = "INSERT INTO model_login(login, senha, nome, email, usuario_id, perfil, sexo, cep, logradouro, bairro, localidade, uf, numero) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
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
			String sql = "UPDATE model_login set login=?, senha=?, nome=?, email=?, perfil=?, sexo=?, cep=?, logradouro=?, bairro=?, localidade=?, uf=?, numero=? WHERE id="+objeto.getId();
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
	
	public List<ModelLogin> consultaUsuarioList(Long userLogado) throws Exception {
		
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
			
			retorno.add(modelLogin);
		}
		
		
		return retorno;
	}
	
	public List<ModelLogin> consultaUsuaList(String nome, Long userLogado) throws Exception {
		List<ModelLogin> retorno = new ArrayList<ModelLogin>();
		
		String sql = "SELECT * FROM model_login WHERE upper(nome) LIKE upper(?) and useradmin is false and usuario_id = ?";
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
	

}
