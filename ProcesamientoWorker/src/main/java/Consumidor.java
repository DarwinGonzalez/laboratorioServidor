
import com.google.gson.Gson;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import java.io.IOException;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.opencv.core.Core;
import org.opencv.core.Mat;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author twcam
 */
public class Consumidor {

    public static void main(String[] args) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME); // Esto es para trabajar con OpenCV
        try {
            Channel c = ConexionRabbitMQ.getChannel();
            Gson gson = new Gson();
            long tsReceptionWorker = System.currentTimeMillis();

            try {
                ConnectionFactory factory = new ConnectionFactory();
                factory.setUsername(RabbitMQStuff.USER);
                factory.setPassword(RabbitMQStuff.PASSWD);
                factory.setVirtualHost(RabbitMQStuff.VIRTUAL_HOST);
                factory.setHost(RabbitMQStuff.HOST);
                factory.setPort(RabbitMQStuff.PORT);
                Connection conn = factory.newConnection();
                Channel channel = conn.createChannel();

                channel.queueDeclare(RabbitMQStuff.COLA_TRABAJOS, true, false, false, null);
                System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

                Consumer consumer = new DefaultConsumer(channel) {
                    @Override
                    public void handleDelivery(String consumerTag, Envelope envelope,
                            AMQP.BasicProperties properties, byte[] body)
                            throws IOException {
                        String message = new String(body, "UTF-8");
                        System.out.println(" [x] Received '" + message + "'");
                        ImageJob ij = gson.fromJson(message, ImageJob.class);

                        String[] commands = new String[]{"bash", "-c", "scp twcam@10.50.0.10:" + ij.getPathSrc() + ij.getImageSrc() + " /home/twcam/opencv/images/descargadas/"};

                        ProcessBuilder pb = new ProcessBuilder(commands);
                        pb.redirectErrorStream(true);
                        Process process = pb.start();
                        int exitValue = 5;
                        try {
                            exitValue = process.waitFor();
                            System.out.println(exitValue);

                            if (exitValue == 0) {
                                System.out.println(ij.getAction());

                                String[] commands2 = new String[]{"bash", "-c", "scp /home/twcam/opencv/images/procesadas/" + ij.getImageDst() + " twcam@10.50.0.10:" + ij.getPathDst()};
//                                System.out.println(commands);
                                ProcessBuilder pb2 = new ProcessBuilder(commands2);
                                pb2.redirectErrorStream(true);
                                Process process2;
                                int exitValue2 = 5;
                                Mat img = OpenCVUtils.readFile("/home/twcam/opencv/images/descargadas/" + ij.getImageSrc());
                                switch (ij.getAction()) {
                                    case "blur":
                                        System.out.println("Voy a hacer un blur");
                                        Mat blur = OpenCVUtils.blur(img);
                                        OpenCVUtils.writeImage(blur, "/home/twcam/opencv/images/procesadas/" + ij.getImageDst());
                                        process2 = pb2.start();
                                        exitValue2 = process.waitFor();
                                        break;
                                    case "gray":
                                        System.out.println("Voy a hacer un gray");
                                        Mat gray = OpenCVUtils.gray(img);
                                        OpenCVUtils.writeImage(gray, "/home/twcam/opencv/images/procesadas/" + ij.getImageDst());
                                        process2 = pb2.start();
                                        exitValue2 = process.waitFor();
                                        break;
                                    case "edge":
                                        System.out.println("Voy a hacer un edge");
                                        Mat edge = OpenCVUtils.canny(img);
                                        OpenCVUtils.writeImage(edge, "/home/twcam/opencv/images/procesadas/" + ij.getImageDst());
                                        process2 = pb2.start();
                                        exitValue2 = process.waitFor();
                                        break;
                                    default:
                                        System.out.println("No estoy haciendo nada");
                                }
                                if (exitValue2 == 0) {
                                    System.out.println("Imagen enviada");
                                    JobCompletion jc = new JobCompletion("Worker 2", ij.getImageSrc(), ij.getTsCreationMessage(), tsReceptionWorker, System.currentTimeMillis());
                                    String json = gson.toJson(jc);

                                    // Usamos el canal para definir: el exchange, la cola y la asociación exchange-cola
                                    c.exchangeDeclare(RabbitMQStuff.EXCHANGE, "direct", true);
                                    c.queueDeclare(RabbitMQStuff.COLA_TIEMPOS, true, false, false, null);
                                    c.queueBind(RabbitMQStuff.COLA_TIEMPOS, RabbitMQStuff.EXCHANGE, RabbitMQStuff.RK_TIEMPOS);

                                    // Obtención del cuerpo del mensaje
                                    byte[] messageBody = json.getBytes();
                                    // Publicar el mensaje con el trabajo a realizar
                                    c.basicPublish(RabbitMQStuff.EXCHANGE, RabbitMQStuff.RK_TIEMPOS, null, messageBody);
                                } else {
                                    System.err.println("No se ha enviado el archivillo");
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {

                        }

                    }
                };
                channel.basicConsume(RabbitMQStuff.COLA_TRABAJOS, true, consumer);

            } catch (IOException ex) {
                Logger.getLogger(Consumidor.class.getName()).log(Level.SEVERE, null, ex);
            } catch (TimeoutException ex) {
                Logger.getLogger(Consumidor.class.getName()).log(Level.SEVERE, null, ex);
            }

        } catch (Exception ex) {
            Logger.getLogger(Consumidor.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
