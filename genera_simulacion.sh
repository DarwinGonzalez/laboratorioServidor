#!/bin/bash
declare -a ops
ops=(edge gray blur)
serverSrcDir=/home/twcam/pls/imagenes-lab
serverDestDir=/home/twcam/pls/imagenes-lab
echo "#!/bin/bash" >> simulacion.sh

for i in $(seq 0 99); do
    indice=$i%3;
    echo "curl -H 'Expect:' -F \"ACTION=${ops[$indice]}\" -F \"IMAGE=@$serverSrcDir/images-$i.png\" http://localhost:8080/images && sleep 0.1;" >> simulacion.sh
done


