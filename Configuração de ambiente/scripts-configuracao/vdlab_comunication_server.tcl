
global usbblaster_location
global device_code

proc select_fpga_location {fpga_order} {

	set cont 1
	set inc 1
	
	foreach device_location [get_hardware_names] {
		if { [string match "USB-Blaster*" $device_location] && $cont == $fpga_order } {
			return $device_location
		}
		set cont [expr $cont + $inc]
	}
	
	
}

proc select_fpga_code {fpga_location} {
	foreach device_code [get_device_names -hardware_name $fpga_location] {
		if { [string match "@1*" $device_code] } {
			return $device_code
		}
	}
}

# Open device 
proc openport {desired_fpga} {
	global usbblaster_location
    global device_code
	puts $desired_fpga
	
	set usbblaster_location [select_fpga_location $desired_fpga]
	set device_code [select_fpga_code $usbblaster_location]
	
	puts $usbblaster_location
	puts $device_code
	open_device -hardware_name $usbblaster_location -device_name $device_code
}


# Close device.  Just used if communication error occurs
proc closeport { } {
	catch {device_unlock}
	catch {close_device}
}

proc exec_conn {send_data} {
	# Connect on the desired fpga
	set fpga_order [string range $send_data 22 22]
	set input_received [string range $send_data 0 21]	
	openport $fpga_order
	
	device_lock -timeout 10000
	# Shift through DR.  Note that -dr_value is unimportant since we're not actually capturing the value inside the part, just seeing what shifts out
	puts "Writing - $input_received"
	device_virtual_ir_shift -instance_index 0 -ir_value 1 -no_captured_ir_value
	#set tdi [device_virtual_dr_shift -dr_value $send_data -instance_index 0  -length 22] #Use this if you want to read back the tdi while you shift in the new value
	device_virtual_dr_shift -dr_value $input_received -instance_index 0  -length 22 -no_captured_dr_value

	# Set IR back to 0, which is bypass mode
	device_virtual_ir_shift -instance_index 0 -ir_value 0 -no_captured_ir_value

  
	closeport

	#return $tdi
}


##############################################################################################
################################# TCP/IP Server ##############################################
##############################################################################################

#Code Dervied from Tcl Developer Exchange - http://www.tcl.tk/about/netserver.html

proc Start_Server {port} {
	set s [socket -server ConnAccept $port]
	puts "Started Socket Server on port - $port"
	vwait forever
}

	
proc ConnAccept {sock addr port} {
    global conn

    # Record the client's information

    puts "Accept $sock from $addr port $port"
    set conn(addr,$sock) [list $addr $port]

    # Ensure that each "puts" by the server
    # results in a network transmission

    fconfigure $sock -buffering line

    # Set up a callback for when the client sends data

    fileevent $sock readable [list IncomingData $sock]
}


proc IncomingData {sock} {
    global conn

    # Check end of file or abnormal connection drop,
    # then write the data to the vJTAG

    if {[eof $sock] || [catch {gets $sock line}]} {
	close $sock
	puts "Close $conn(addr,$sock)"
	unset conn(addr,$sock)
    } else {
	#Before the connection is closed we get an emtpy data transmission. Let's check for it and trap it
	puts $line	
	set data_len [string length $line]
	if {$data_len != "0"} then {
		#Extract the First Bit
		set line [string range $line 0 23]
		#Send the vJTAG Commands to Update the command
		exec_conn $line
	}
    }
}

#Start thet Server at Port 2540
Start_Server 2540

##############################################################################################



