package MongoDB;

import java.util.Scanner;
import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.Cursor;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Conexao {

	private static MongoClientURI uri; // ir� conter a URL do banco de dados, e das tabelas
	private static MongoClient mongo; // servir� para podermos acessar o banco de dados, e as tabelas
	private static DB database; // ir� guardar o banco de dados
	private static DBCollection collection; // ir� guardar a tabela

	// DB / DataBase / Banco de Dados = mesma coisa
	// Collection / Tabela = mesma coisa
	// document / documento = coisas que est�o dentro das tabelas

	public static void main(String[] args) throws Exception {
		try {
			// Estes comandos servem para retirar os logs que s�o automaticamente feitos
			// pelo MongoDB (comente esses c�digos se houver duvida)
			Logger mongoLogger = Logger.getLogger("org.mongodb.driver");
			mongoLogger.setLevel(Level.SEVERE);
			
			connect();
			
			// System.out.println(collection.getIndexInfo());
			
			collection.find().explain().put("nome", "teste");

			// ficar� repetindo at� que escrevam 0
			while (true == true) {

				connect(); // Abre a conexao para acessarmos as tabelas e o banco

				System.out.println("1-INSERIR \n2-ATUALIZAR \n3-DELETAR \n4-BUSCAR TODOS \n5-BUSCAR UM\n\n");
				Scanner sc = new Scanner(System.in);
				int opcao = sc.nextInt();

				switch (opcao) {

				case 0:

					disconnect(); // Fecha a conexao com o banco
					System.exit(0); // Fecha o programa
					sc.close();

					break;

				case 1:

					// Aqui ele insere um "documento" na tabela

					System.out.println("Digite alguma coisa:");
					String strDocument_Insert = sc.next();

					Insert_Document(strDocument_Insert);

					break;

				case 2:

					// Aqui ele atualiza um "documento" ja existente na tabela por um novo

					System.out.println("Digite o antigo conteudo:");
					String strOldDocument_Update = sc.next();
					System.out.println("Digite o novo conteudo:");
					String strNewDocument_Update = sc.next();

					Update_Document(strNewDocument_Update, strOldDocument_Update);

					break;

				case 3:

					// Aqui ele deleta um "documento"

					System.out.println("Digite o que quiser deletar:");
					String strDocument_Delete = sc.next();

					Delete_Document(strDocument_Delete);

					break;

				case 4:

					// Aqui ele lista todos os documentos que existem na tabela

					Read_Documents();

					break;

				case 5:

					// Aqui ele lista um "documento" especifico que existir na tabela

					System.out.println("Digite o que estiver procurando:");
					String strDocument_Read = sc.next();

					Read_Document(strDocument_Read);

					break;

				case 6:

					// System.out.println( Read_Document_And_Return("aaaaaaaaaa") );

					// Obj to JSON
					Modelo teste1 = new Modelo();
					teste1.nome = "Thales1";

					Gson gson = new Gson();
					String jsonStr = gson.toJson(teste1);

					// System.out.println( jsonStr );
					// -----------------------------------------------

					// JSON to Obj
					String json2 = "{\"nome\":\"Thales2\"}";
					Modelo teste2 = new Modelo();
					teste2 = gson.fromJson(json2, Modelo.class); // deserializes json into target2

					// System.out.println( teste2.nome );
					// -----------------------------------------------

					// Teste com Mongo

					String content = Read_Document_And_Return("aaaaaaaaaa");
					Modelo teste = new Modelo();

					teste = gson.fromJson(content, Modelo.class);

					System.out.println(teste.nome);

					// -----------------------------------------------

					break;

				default:
					break;
				}

				disconnect();
				System.out.println("\n\n");
			}

		} catch (Exception e) {
			disconnect();
		}
	}

	@SuppressWarnings("deprecation")
	private static void connect() {
		try {

			// Aqui se atribui os valores para as variaveis que se conectam ao banco

			uri = new MongoClientURI(
					"mongodb+srv://thales:iambatman@teste-tngy3.mongodb.net/test?retryWrites=true&w=majority");

			mongo = new MongoClient(uri);
			database = mongo.getDB("DataBaseTeste");
			collection = database.getCollection("CollectionTeste");
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	private static void disconnect() {
		try {

			// Desconecta do banco
			mongo.close();

		} catch (Exception e) {
			System.out.println(e);
		}
	}

	// Insere um documento
	public static void Insert_Document(String strDocument) {
		try {

			BasicDBObject document = new BasicDBObject("nome", strDocument);
			collection.insert(document);

		} catch (Exception e) {
			System.out.println(e);
		}
	}

	// Atualiza um documento
	public static void Update_Document(String strNewDocument_Update, String strOldDocument_Update) {
		try {
			// new value
			BasicDBObject newDocument = new BasicDBObject("nome", strNewDocument_Update);

			// old value
			BasicDBObject oldDocument = new BasicDBObject("nome", strOldDocument_Update);

			collection.update(oldDocument, newDocument);

		} catch (Exception e) {
			System.out.println(e);
		}
	}

	// Deleta um documento
	public static void Delete_Document(String strDocument) {
		try {

			BasicDBObject document = new BasicDBObject("nome", strDocument);

			collection.remove(document);

			// collection.drop(); isso apaga a collection

		} catch (Exception e) {
			System.out.println(e);
		}
	}

	// Le todos os documentos
	public static void Read_Documents() {
		try {

			Cursor cursor = collection.find();
			while (cursor.hasNext()) {
				System.out.println(cursor.next());

			}

		} catch (Exception e) {
			System.out.println(e);
		}
	}

	// Le um documento
	public static void Read_Document(String strDocument) {
		try {

			BasicDBObject document = new BasicDBObject("nome", strDocument);

			Cursor cursor = collection.find(document);
			while (cursor.hasNext()) {
				System.out.println(cursor.next());

			}

		} catch (Exception e) {
			System.out.println(e);
		}
	}

	// Le um documento
	public static String Read_Document_And_Return(String strDocument) {
		try {

			BasicDBObject document = new BasicDBObject("nome", strDocument);
			String content = "";
			Cursor cursor = collection.find(document);
			while (cursor.hasNext()) {
				content = cursor.next().toString();

			}
			return content;
		} catch (Exception e) {
			System.out.println(e);
			return e.getMessage();
		}
	}

}
