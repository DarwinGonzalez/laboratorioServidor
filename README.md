# Programación Lado del Servidor: Práctica Laboratorio

## Objetivos
El objetivo principal de esta práctica es la puesta en marcha de un sistema similar al que se puede ver en la imagen:

![](https://github.com/DarwinGonzalez/laboratorioServidor/blob/master/datosEstadisticos/images/esquema.png?raw=true)

Así pues, en este esquema tenemos tres máquinas virtuales interconectadas entre sí. En primer lugar tenemos la máquina TWCAM la cual es la encargada de actuar como servidor principal (*dynamic-http-server*) y como cliente (*ProcesamientoTwcam*) que enviará imágenes mediante peticiones POST a la máquina RabbitMQ. Esta segunda máquina virtual actuará como *broker* de mensajes de RabbitMQ, y contendrá las colas de trabajos que se irán almacenando a medida que se realicen peticiones POST. Finalmente, tendremos la máquina Worker, la cuál es la encargada de obtener los diferentes trabajos de la cola de trabajos e ir realizando cada uno de ellos (*ProcesamientoWorker*).

En este caso práctico el objetivo es enviar una petición al RabbitMQ en la cúal se envian dos parámetros:

* Una ruta con una imagen determinada contenida en la máquina TWCAM.
* Una acción o efecto que será aplicada a esta imagen.

```
curl -H ’Expect:’ -F "ACTION=blur" -F "IMAGE=@/home/twcam/images/borrar.png" http://localhost :8080/images
```

Una vez este esta información almacenada en una de las colas del RabbitMQ el Worker será el encargado de vaciar esta cola e ir realizando cada uno de los trabajos. Para ello el worker cuenta con una librería de procesado de imágenes denominada OpenCV, la cual utilizará para procesar las imágenes aplicando acciones como *blur*, *gray*, o *edge*.

Una vez procesada está imagen el Worker la enviará de vuelta a la máquina origen, no sin antes almacenar una serie de datos estadísticos en una de las colas del RabbitMQ.

Para finalizar el proceso habrá una aplicación(*ProcesamientoEstadisticas*) en la máquina TWCAM a la espera de esta información. Esta generará una serie de ficheros, los cuales serán utilizados para un estudio en profundidad de el rendimiento del sistema.

Además de una sola máquina Worker también se han realizado diferentes pruebas con dos máquinas Worker, trabajando con ellas de forma simúltanea.
