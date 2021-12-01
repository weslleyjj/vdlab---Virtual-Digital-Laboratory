set cont 0
set inc 1

foreach device [get_hardware_names] {
	puts "$device"
	set cont [expr $cont + $inc]
}

#set stringtest "00000000000000000000001"

#puts [string range $stringtest 22 end]

#puts [string range $stringtest 0 21]




