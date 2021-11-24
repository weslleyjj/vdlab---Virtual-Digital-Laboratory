# Script para ser executado pelo servidor
# para obter as placas conectadas a máquina

set cont 0
set inc 1

foreach device [get_hardware_names] {
	puts "$device $cont"
	set cont [expr $cont + $inc]
}


