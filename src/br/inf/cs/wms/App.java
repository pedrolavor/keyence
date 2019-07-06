package br.inf.cs.wms;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

import br.inf.cs.barcode.GS1;
import br.inf.cs.keyence.KeyenceController;

import static com.mongodb.client.model.Filters.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class App {

	public static void main(String[] args) throws Exception {
		String HOST = "192.168.0.124";
		int PORT = 9004;
		Scanner entrada = new Scanner(System.in);
		Integer comando = -1;
		KeyenceController keyence = new KeyenceController();
		
		MongoClient mongoClient = new MongoClient("localhost", 27017);
		MongoDatabase database = mongoClient.getDatabase("wms");
		
		
		
		
		// INICIAR COMUNICACAO COM SCANNER
		keyence.host(HOST);
		keyence.porta(PORT);
		try {
			keyence.conectar();
			
		} catch(Exception e) {
			System.out.println("Falha de conex�o com scanner!");
			System.exit(0);
		}
		
		
		
		
		
		// MENU
		while(comando != 5) {
			menu();
			comando = Integer.valueOf(entrada.nextLine());

			switch (comando) {
			case 1:
					MongoCollection<Document> pedidos = database.getCollection("pedidos");
					Document pedidoAndamento = pedidos.find(eq("status", "andamento")).first();
					if(pedidoAndamento.isEmpty()) {
						System.out.println("N�o nenhum pedido em adamento!");
					} else {
						
						int leiturasRealizadas = 0;
						int qtdLeitura = (int) pedidoAndamento.get("qtdUnidades");
						System.out.println("Unidades: " + qtdLeitura);
						
						while(leiturasRealizadas < 5) {
							System.out.println("+-------------------------------------------------");
							System.out.println("| Id: " + pedidoAndamento.getObjectId("_id"));
							System.out.println("| Data Emiss�o: " + pedidoAndamento.getString("emissao"));
							System.out.println("| Cliente: " + pedidoAndamento.getString("cliente"));
							System.out.println("| Valor: R$" + pedidoAndamento.getDouble("valor"));
							System.out.println("| NF: " + pedidoAndamento.getBoolean("nf"));
							System.out.println("| Boleto: " + pedidoAndamento.getBoolean("boleto"));
							System.out.println("| Guia: " + pedidoAndamento.getBoolean("guia"));
							System.out.println("| Itens: ");
							System.out.println("+-------------------------------------------------");
							List<Document> itens = (ArrayList<Document>) pedidoAndamento.get("itens");
							for(Document item : itens) {
								System.out.println("|    " + item.getString("descricao") + " - " + item.get("lote") + " - " + item.get("quantidade"));
							}
							System.out.println("+-------------------------------------------------");
							System.out.println(keyence.nome());
							leiturasRealizadas++;
						}
						
						keyence.pararLeitura();
						
					}
				break;
			case 2:
				try {
					System.out.println(keyence.focar());
				} catch (Exception e) {
					System.out.println("Houve um problema ao tentar focar o scanner!");
					System.out.println();
				}
				break;
			case 3:
				try {
					System.out.println(keyence.tunar());
				} catch (Exception e) {
					System.out.println("Houve um problema ao tentar realizar o tuning!");
					System.out.println();
				}
				break;
			case 4:
				try {
					keyence.conectar();
				} catch (Exception e) {
					System.out.println("Houve um problema ao tentar se conectar!");
					System.out.println();
				}
				break;
			case 5:
				try {
					keyence.pararLeitura();
					keyence.desconectar();
					entrada.close();
					mongoClient.close();
					System.exit(0);
				} catch (Exception e) {
					System.out.println("Houve um problema ao tentar se desconectar!");
					System.out.println();
				}
				break;

			default:
				System.out.println("Comando n�o encontrado!");
				System.out.println();
				break;
			}
		}
//		
//		//PEGAR PEDIDO EM ANDAMENTO
//		MongoCollection<Document> pedidos = database.getCollection("pedidos");
//		Document pedidoAndamento = pedidos.find(eq("status", "andamento")).first();
//		if(!pedidoAndamento.isEmpty()) {
//			System.out.println("+-------------------------------------------------");
//			System.out.println("| Id: " + pedidoAndamento.getObjectId("_id"));
//			System.out.println("| Data Emiss�o: " + pedidoAndamento.getString("emissao"));
//			System.out.println("| Cliente: " + pedidoAndamento.getString("cliente"));
//			System.out.println("| Valor: R$" + pedidoAndamento.getDouble("valor"));
//			System.out.println("| NF: " + pedidoAndamento.getBoolean("nf"));
//			System.out.println("| Boleto: " + pedidoAndamento.getBoolean("boleto"));
//			System.out.println("| Guia: " + pedidoAndamento.getBoolean("guia"));
//			System.out.println("| Itens: ");
//			System.out.println("+-------------------------------------------------");
//			List<Document> itens = (ArrayList<Document>) pedidoAndamento.get("itens");
//			for(Document item : itens) {
//				System.out.println("|    " + item.getString("descricao") + " - " + item.get("lote") + " - " + item.get("quantidade"));
//			}
//			System.out.println("+-------------------------------------------------");
//		}
		
		
		
//		mongoClient.close();
	}
	
	
	public static void menu() {
		System.out.println("+-----------------------+");
		System.out.println("| 1 - Iniciar Expedi��o |");
		System.out.println("| 2 - AutoFocus         |");
		System.out.println("| 3 - Tuning            |");
		System.out.println("| 4 - Conectar          |");
		System.out.println("| 5 - Desconectar       |");
		System.out.println("+-----------------------+");
		System.out.println();
	}
	
}
