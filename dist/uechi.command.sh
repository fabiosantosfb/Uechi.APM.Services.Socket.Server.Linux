if [ -z "$1" ];	then
		printf "/ Parametro do diretorio de origem nao encontrado./\n"
else
	if [ "$1" = "1" ]; then
		# CPU
		#top -bn1 | grep "Cpu(s)" | \sed "s/.*, *\([0-9.]*\)%* id.*/\1/" | \awk '{print "CPU Idle: " 100 - $1"%"}'
		 top -bn 1 | awk '{print $9}' | tail -n +8 | awk '{s+=$1} END {print "CPU Total Usage:"s"%;CPU Total Idle:"(100-s)"%" }'
	elif [ "$1" = "2" ]; then
		# Memory
		#free -m | awk 'NR==2{printf "Memory Usage: %s/%sMB (%.2f%%)\n", $3,$2,$3*100/$2 }'
		free -m | awk 'NR==2{printf "Memory Usage:%sMB;Memory Total:%sMB;%.2f%%;100%\n", $3,$2,$3*100/$2 }'
	elif [ "$1" = "3" ]; then
		# Disk
		#df -h | awk '$NF=="/"{printf "Disk Usage: %d/%dGB (%s)\n", $3,$2,$5}'
		df -h | awk '$NF=="/"{printf "Disk Usage:%dGB;Disk Total:%dGB;%s;100%\n", $3,$2,$5}'
	elif [ "$1" = "4" ]; then
		# Network
		bytestohumanreadable(){
		multiplier="0"
		number="$1"
		while [[ "$number" -ge 1024 ]] ; do
		  multiplier=$(($multiplier+1))
		  number=$(($number/1024))
		done
		case "$multiplier" in
		  1)
		   echo "$number Kb"
		  ;;
		  2)
		   echo "$number Mb"
		  ;;
		  3)
		   echo "$number Gb"
		  ;;
		  4)
		   echo "$number Tb"
		  ;;
		  *)
		   echo "$1 b"
		  ;;
		esac
            }
            infonet=enp2s0
#  	    oldrxbytes=`/sbin/ifconfig "$infonet" | grep "RX bytes" | cut -d: -f2 | awk '{ print $1 }'`
#	    oldtxbytes=`/sbin/ifconfig "$infonet" | grep "TX bytes" | cut -d: -f3 | awk '{ print $1 }'`
#  	    rxbytes=`/sbin/ifconfig "$infonet" | grep "RX bytes" | cut -d: -f2 | awk '{ print $1 }'`
#	    txbytes=`/sbin/ifconfig "$infonet" | grep "TX bytes" | cut -d: -f3 | awk '{ print $1 }'`
            oldrxbytes=`/sbin/ifconfig "$infonet" | grep "RX packets" | cut -d: -f2 | awk '{ print $5 }'`
	    oldtxbytes=`/sbin/ifconfig "$infonet" | grep "TX packets" | cut -d: -f2 | awk '{ print $5 }'`
  	    rxbytes=`/sbin/ifconfig "$infonet" | grep "RX packets" | cut -d: -f2 | awk '{ print $5 }'`
	    txbytes=`/sbin/ifconfig "$infonet" | grep "TX packets" | cut -d: -f2 | awk '{ print $5 }'`
            echo "RXbytes Total: $(bytestohumanreadable $(($rxbytes))); RXbytes: $(bytestohumanreadable $(($rxbytes - $oldrxbytes))) ; TXbytes Total: $(bytestohumanreadable $(($txbytes))); TXbytes: $(bytestohumanreadable $(($txbytes - $oldtxbytes)))"
	fi
fi