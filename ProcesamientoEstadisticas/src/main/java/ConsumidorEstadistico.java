
import com.google.gson.Gson;
import com.opencsv.CSVWriter;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author twcam
 */
public class ConsumidorEstadistico {

    JobCompletion jc = null;

    public static void main(String[] args) {
        try {
            Channel c = ConexionRabbitMQ.getChannel();
            Gson gson = new Gson();

            try {
                ConnectionFactory factory = new ConnectionFactory();
                factory.setUsername(RabbitMQStuff.USER);
                factory.setPassword(RabbitMQStuff.PASSWD);
                factory.setVirtualHost(RabbitMQStuff.VIRTUAL_HOST);
                factory.setHost(RabbitMQStuff.HOST);
                factory.setPort(RabbitMQStuff.PORT);
                Connection conn = factory.newConnection();
                Channel channel = conn.createChannel();

                channel.queueDeclare(RabbitMQStuff.COLA_TIEMPOS, true, false, false, null);
                System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

                Consumer consumer = new DefaultConsumer(channel) {
                    @Override
                    public void handleDelivery(String consumerTag, Envelope envelope,
                            AMQP.BasicProperties properties, byte[] body)
                            throws IOException {
                        String message = new String(body, "UTF-8");
                        System.out.println(" [x] Received '" + message + "'");
                        JobCompletion jc = gson.fromJson(message, JobCompletion.class);
                        System.out.println(jc);
//                      
                        String nombreArchivo = "/var/web/resources/estadisticas.csv";
                        String[] entradasCsv = {jc.getWorker(), String.valueOf(jc.getTsCreationMessage()), String.valueOf(jc.getTsReceptionWorker()), String.valueOf(jc.getTsFinalizationWorker())};
//                        try (FileOutputStream fos = new FileOutputStream(nombreArchivo);
//                                OutputStreamWriter osw = new OutputStreamWriter(fos,
//                                        StandardCharsets.UTF_8);
//                                CSVWriter writer = new CSVWriter(osw)) {

//                        FileWriter mFileWriter = new FileWriter(nombreArchivo, true);
//                        CSVWriter mCsvWriter = new CSVWriter(mFileWriter);
//                        mCsvWriter.writeNext(entradasCsv);
//                        } catch (IOException ex) {
//                            Logger.getLogger(ConsumidorEstadistico.class.getName()).log(Level.SEVERE, null, ex);
//                        }
                        FileWriter pw = new FileWriter(nombreArchivo, true);
                        for(String entradas:entradasCsv) {
                            pw.append(entradas);
                            pw.append(",");
                        }
                        pw.append("\n");
                        pw.flush();
                        pw.close();
                    }
                };

                channel.basicConsume(RabbitMQStuff.COLA_TIEMPOS, true, consumer);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void crearCSV() {

    }
}
