#!/bin/bash

cd ~/Programs/altera/quartus/bin/
echo "$1" 
echo "$2"
./quartus_pgm -c "$1" -m JTAG -o "$2"

